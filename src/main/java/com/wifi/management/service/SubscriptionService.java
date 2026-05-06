package com.wifi.management.service;

import com.wifi.management.model.Subscription;
import com.wifi.management.utils.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class SubscriptionService {

    // ================= 1. DAYS REMAINING =================
    public long getDaysRemaining(Date expiryDate) {

        if (expiryDate == null) return 0;

        LocalDate today = LocalDate.now();
        LocalDate expiry = expiryDate.toLocalDate();

        if (expiry.isBefore(today)) return 0;

        return ChronoUnit.DAYS.between(today, expiry);
    }

    // ================= 2. CHECK ACTIVE =================
    public boolean isSubscriptionActive(Subscription sub) {

        if (sub == null || sub.getStatus() == null) return false;

        if (!"active".equalsIgnoreCase(sub.getStatus())) return false;

        LocalDate today = LocalDate.now();

        return !sub.getExpiryDate().toLocalDate().isBefore(today);
    }

    // ================= 3. RENEWAL WARNING =================
    public boolean needsRenewalNotice(Date expiryDate) {

        long daysLeft = getDaysRemaining(expiryDate);

        return daysLeft >= 0 && daysLeft <= 3;
    }

    // ================= 4. GET SUBSCRIPTION STATUS (IMPORTANT FIX) =================
    public String getSubscriptionStatus(int userId) {

        String sql = """
                SELECT s.status
                FROM subscriptions s
                WHERE s.user_id = ?
                ORDER BY s.sub_id DESC
                LIMIT 1
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("status");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // ================= 5. GET PLAN NAME =================
    public String getPlanName(int userId) {

        String sql = """
                SELECT p.plan_name
                FROM subscriptions s
                JOIN plans p ON s.plan_id = p.plan_id
                WHERE s.user_id = ?
                ORDER BY s.sub_id DESC
                LIMIT 1
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("plan_name");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "N/A";
    }

    // ================= 6. GET PRICE =================
    public String getPrice(int userId) {

        String sql = """
                SELECT p.monthly_price
                FROM subscriptions s
                JOIN plans p ON s.plan_id = p.plan_id
                WHERE s.user_id = ?
                ORDER BY s.sub_id DESC
                LIMIT 1
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return "৳ " + rs.getDouble("monthly_price");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "N/A";
    }

    // ================= 7. GET START DATE =================
    public String getStartDate(int userId) {

        String sql = """
                SELECT s.sub_id, s.expiry_date
                FROM subscriptions s
                WHERE s.user_id = ?
                ORDER BY s.sub_id DESC
                LIMIT 1
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // simple assumption (you can improve later)
                return "Active Subscription";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "N/A";
    }

    // ================= 8. GET EXPIRY DATE =================
    public String getExpiryDate(int userId) {

        String sql = """
                SELECT expiry_date
                FROM subscriptions
                WHERE user_id = ?
                ORDER BY sub_id DESC
                LIMIT 1
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDate("expiry_date").toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "N/A";
    }
}