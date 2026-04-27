package com.wifi.management.database_operation;

import com.wifi.management.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class PlanDAO {
    public void addPlan(String name, double price, String speed) {
        String sql = "INSERT INTO Plan (planName, price, speed) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setDouble(2, price);
            pstmt.setString(3, speed);
            pstmt.executeUpdate();
            System.out.println("Plan added successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}