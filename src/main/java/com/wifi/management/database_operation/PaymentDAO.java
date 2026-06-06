package com.wifi.management.database_operation;

import com.wifi.management.model.Payment;
import com.wifi.management.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PaymentDAO {

    public boolean processPlanPurchase(Payment payment, int planId) {
        Connection conn = DBConnection.getConnection();
        if (conn == null) return false;

        try {
            conn.setAutoCommit(false);

            // =====================================================================
            // 🛑 নতুন লজিক: চলতি মাসে অলরেডি কোনো প্ল্যান কেনা হয়েছে কি না তা চেক করা
            // =====================================================================
            String checkSQL = "SELECT COUNT(*) FROM transactions " +
                    "WHERE user_id = ? " +
                    "AND MONTH(payment_date) = MONTH(CURRENT_DATE()) " +
                    "AND YEAR(payment_date) = YEAR(CURRENT_DATE())";

            try (PreparedStatement pstmtCheck = conn.prepareStatement(checkSQL)) {
                pstmtCheck.setInt(1, payment.getUserId());
                try (ResultSet rs = pstmtCheck.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        // চলতি মাসে অলরেডি ট্রানজেকশন থাকলে রিকোয়েস্ট ব্লক করা হবে
                        throw new SQLException("LIMIT_EXCEEDED: You have already purchased a package this month!");
                    }
                }
            }

            // =====================================================================
            // ১. পেমেন্ট ট্রানজেকশন ইনসার্ট করা
            // =====================================================================
            String insertPaymentSQL = "INSERT INTO transactions (transaction_id, user_id, amount, payment_method) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmtPayment = conn.prepareStatement(insertPaymentSQL)) {
                pstmtPayment.setString(1, payment.getTransactionId());
                pstmtPayment.setInt(2, payment.getUserId());
                pstmtPayment.setDouble(3, payment.getAmount());
                pstmtPayment.setString(4, payment.getPaymentMethod());
                pstmtPayment.executeUpdate();
            }

            // =====================================================================
            // ২. সাবস্ক্রিপশন টেবিল আপগ্রেড/ইনসার্ট (Upsert) করা
            // =====================================================================
            String upsertSubscriptionSQL = "INSERT INTO subscriptions (user_id, plan_id, expiry_date, status) " +
                    "VALUES (?, ?, DATE_ADD(CURDATE(), INTERVAL 30 DAY), 'active') " +
                    "ON DUPLICATE KEY UPDATE " +
                    "plan_id = VALUES(plan_id), " +
                    "expiry_date = DATE_ADD(CURDATE(), INTERVAL 30 DAY), " +
                    "status = 'active'";

            try (PreparedStatement pstmtSub = conn.prepareStatement(upsertSubscriptionSQL)) {
                pstmtSub.setInt(1, payment.getUserId());
                pstmtSub.setInt(2, planId);

                int rowsAffected = pstmtSub.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Subscription update/insert failed.");
                }
            }

            // সব কোয়েরি সফল হলে ডেটাবেসে পার্মানেন্টলি সেভ হবে
            conn.commit();
            return true;

        } catch (SQLException e) {
            // কোনো একটি কোয়েরি ফেইল করলে বা লিমিট এক্সিড হলে পুরো প্রসেস রোলব্যাক হবে
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }

            // যদি আমাদের কাস্টম লিমিট এরর হয়, তবে মেসেজটি প্রিন্ট করবে
            if (e.getMessage().contains("LIMIT_EXCEEDED")) {
                System.err.println("❌ Purchase Blocked: " + e.getMessage());
            } else {
                e.printStackTrace();
            }
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}