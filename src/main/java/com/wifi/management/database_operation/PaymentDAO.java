package com.wifi.management.database_operation;

import com.wifi.management.model.Payment;
import com.wifi.management.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PaymentDAO {

    public boolean processPlanPurchase(Payment payment, int planId) {
        Connection conn = DBConnection.getConnection();
        if (conn == null) return false;

        try {
            conn.setAutoCommit(false); // ট্রানজেকশন শুরু

            // ১. Transactions টেবিলে পেমেন্ট ডাটা ইনসার্ট করা
            String insertPaymentSQL = "INSERT INTO transactions (transaction_id, user_id, amount, payment_method) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmtPayment = conn.prepareStatement(insertPaymentSQL)) {
                pstmtPayment.setString(1, payment.getTransactionId()); // Index 1
                pstmtPayment.setInt(2, payment.getUserId());          // Index 2
                pstmtPayment.setDouble(3, payment.getAmount());        // Index 3
                pstmtPayment.setString(4, payment.getPaymentMethod()); // Index 4
                pstmtPayment.executeUpdate();
            }

            // ২. Subscriptions টেবিল আপডেট বা ইনসার্ট করা (Upsert লজিক)
            // এটি চেক করবে ইউজারের সাবস্ক্রিপশন আছে কি না, না থাকলে নতুন তৈরি করবে
            String upsertSubscriptionSQL = "INSERT INTO subscriptions (user_id, plan_id, expiry_date, status) " +
                    "VALUES (?, ?, DATE_ADD(CURDATE(), INTERVAL 30 DAY), 'active') " +
                    "ON DUPLICATE KEY UPDATE " +
                    "plan_id = VALUES(plan_id), " +
                    "expiry_date = DATE_ADD(CURDATE(), INTERVAL 30 DAY), " +
                    "status = 'active'";

            try (PreparedStatement pstmtSub = conn.prepareStatement(upsertSubscriptionSQL)) {
                pstmtSub.setInt(1, payment.getUserId()); // প্রথম '?' এর জন্য user_id (Index 1)
                pstmtSub.setInt(2, planId);              // দ্বিতীয় '?' এর জন্য plan_id (Index 2)

                int rowsAffected = pstmtSub.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Subscription update/insert failed.");
                }
            }

            conn.commit(); // সব কাজ সফল হলে সেভ হবে
            return true;

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback(); // এরর হলে আগের অবস্থায় ফিরে যাবে
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
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