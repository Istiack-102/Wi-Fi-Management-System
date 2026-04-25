package com.wifi.management.database_operation;

import com.wifi.management.db_connection.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class PaymentDAO {
    public void recordPayment(int subId, String date) {
        String sql = "INSERT INTO Payment (subscriptionId, paymentDate) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, subId);
            pstmt.setString(2, date);

            pstmt.executeUpdate();
            System.out.println("Payment recorded successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}