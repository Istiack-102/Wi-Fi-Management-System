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
            // Disable auto-commit to start a manual transaction
            conn.setAutoCommit(false);

            // Insert payment record into transactions table
            String insertPaymentSQL = "INSERT INTO transactions (transaction_id, user_id, amount, payment_method) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmtPayment = conn.prepareStatement(insertPaymentSQL)) {
                pstmtPayment.setString(1, payment.getTransactionId());
                pstmtPayment.setInt(2, payment.getUserId());
                pstmtPayment.setDouble(3, payment.getAmount());
                pstmtPayment.setString(4, payment.getPaymentMethod());
                pstmtPayment.executeUpdate();
            }

            // Update subscription with new plan and add 30 days to expiry
            String updateSubscriptionSQL = "UPDATE subscriptions SET plan_id = ?, expiry_date = DATE_ADD(CURDATE(), INTERVAL 30 DAY), status = 'active' WHERE user_id = ?";
            try (PreparedStatement pstmtSub = conn.prepareStatement(updateSubscriptionSQL)) {
                pstmtSub.setInt(1, planId);
                pstmtSub.setInt(2, payment.getUserId());
                int rowsUpdated = pstmtSub.executeUpdate();

                if (rowsUpdated == 0) {
                    throw new SQLException("Update failed");
                }
            }

            // Commit transaction to save changes permanently
            conn.commit();
            return true;

        } catch (SQLException e) {
            try {
                // Rollback all changes if any error occurs
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) {
                    // Restore default connection behavior and close
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}