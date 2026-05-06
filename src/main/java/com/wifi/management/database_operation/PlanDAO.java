package com.wifi.management.database_operation;

import com.wifi.management.model.Plan;
import com.wifi.management.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlanDAO {

    // ================= GET ALL PLANS =================
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

    // ================= GET PLAN BY ID =================
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

    // ================= OLD METHOD (KEEP) =================
    public boolean updatePlanPrice(int planId, double newPrice) {
        String sql = "UPDATE plans SET monthly_price = ? WHERE plan_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, newPrice);
            pstmt.setInt(2, planId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ================= 🔥 NEW METHOD (MAIN UPDATE) =================
    public boolean updatePlan(int planId, int newSpeed, double newPrice) {

        String sql = "UPDATE plans SET speed_limit_mbps = ?, monthly_price = ? WHERE plan_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, newSpeed);
            pstmt.setDouble(2, newPrice);
            pstmt.setInt(3, planId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}