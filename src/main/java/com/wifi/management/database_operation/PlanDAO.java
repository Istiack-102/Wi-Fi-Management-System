package com.wifi.management.database_operation;

import com.wifi.management.model.Plan;
import com.wifi.management.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlanDAO {

    public List<Plan> getAllPlans() {
        List<Plan> plans = new ArrayList<>();
        String sql = "SELECT * FROM plans";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Plan plan = new Plan(
                        rs.getInt("plan_id"),
                        rs.getString("plan_name"),
                        rs.getInt("speed_limit_mbps"),
                        rs.getDouble("monthly_price")
                );
                plans.add(plan);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plans;
    }

    public Plan getPlanById(int planId) {
        String sql = "SELECT * FROM plans WHERE plan_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, planId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Plan(
                            rs.getInt("plan_id"),
                            rs.getString("plan_name"),
                            rs.getInt("speed_limit_mbps"),
                            rs.getDouble("monthly_price")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean updatePlan(Plan plan) {
        if (plan == null || plan.getPlanId() <= 0) {
            System.err.println("Update failed: Invalid Plan object or ID.");
            return false;
        }

        String sql = "UPDATE plans SET plan_name = ?, speed_limit_mbps = ?, monthly_price = ? WHERE plan_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, plan.getPlanName());
            pstmt.setInt(2, plan.getSpeedLimitMbps());
            pstmt.setDouble(3, plan.getMonthlyPrice());
            pstmt.setInt(4, plan.getPlanId());

            int rowsUpdated = pstmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Plan ID " + plan.getPlanId() + " updated successfully.");
                return true;
            } else {
                System.err.println("Update failed: No plan found with ID " + plan.getPlanId());
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Database Error during plan update: " + e.getMessage());
            return false;
        }
    }

}