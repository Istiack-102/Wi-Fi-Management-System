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
    public boolean updatePlan(Plan plan) {
        // ১. বেসিক ভ্যালিডেশন: প্ল্যান অবজেক্ট বা আইডি ইনভ্যালিড হলে আগেই রিটার্ন করবে
        if (plan == null || plan.getPlanId() <= 0) {
            System.err.println("Update failed: Invalid Plan object or ID.");
            return false;
        }

        // SQL কোয়েরি
        String sql = "UPDATE plans SET plan_name = ?, speed_limit_mbps = ?, monthly_price = ? WHERE plan_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // ২. প্যারামিটার সেট করা
            pstmt.setString(1, plan.getPlanName());
            pstmt.setInt(2, plan.getSpeedLimitMbps());
            pstmt.setDouble(3, plan.getMonthlyPrice());
            pstmt.setInt(4, plan.getPlanId());

            // ৩. এক্সেকিউশন
            int rowsUpdated = pstmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Plan ID " + plan.getPlanId() + " updated successfully.");
                return true;
            } else {
                System.err.println("Update failed: No plan found with ID " + plan.getPlanId());
                return false;
            }

        } catch (SQLException e) {
            // ৪. ডিটেইলড এরর হ্যান্ডলিং
            System.err.println("Database Error during plan update: " + e.getMessage());
            // প্রোডাকশন লেভেলে e.printStackTrace() এর বদলে Logger ব্যবহার করা ভালো
            return false;
        }
    }

}