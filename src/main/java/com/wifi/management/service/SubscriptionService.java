package com.wifi.management.service;

import com.wifi.management.model.Subscription;
import com.wifi.management.utils.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class SubscriptionService {

    public boolean hasUserPurchasedThisMonth(int userId) {
        String sql = """
                SELECT COUNT(*) 
                FROM transactions 
                WHERE user_id = ? 
                  AND MONTH(payment_date) = MONTH(CURRENT_DATE()) 
                  AND YEAR(payment_date) = YEAR(CURRENT_DATE())
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // এন্ট্রি থাকলে true রিটার্ন করবে
                }
            }
        } catch (Exception e) {
            System.err.println("🔴 Error checking monthly purchase limit in SubscriptionService: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public long getDaysRemaining(Date expiryDate) {
        if (expiryDate == null) return 0;

        LocalDate today = LocalDate.now();
        LocalDate expiry = expiryDate.toLocalDate();

        if (expiry.isBefore(today)) return 0;

        return ChronoUnit.DAYS.between(today, expiry);
    }

    public boolean isSubscriptionActive(Subscription sub) {
        if (sub == null || sub.getStatus() == null || sub.getExpiryDate() == null) return false;

        if (!"active".equalsIgnoreCase(sub.getStatus())) return false;

        LocalDate today = LocalDate.now();
        return !sub.getExpiryDate().toLocalDate().isBefore(today);
    }

    public boolean needsRenewalNotice(Date expiryDate) {
        long daysLeft = getDaysRemaining(expiryDate);
        return daysLeft >= 0 && daysLeft <= 3;
    }

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
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("status");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "inactive"; // ডিফল্ট স্ট্যাটাস
    }

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
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("plan_name");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "No Active Plan ❌";
    }

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
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return "৳ " + rs.getDouble("monthly_price");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "N/A";
    }

    public String getStartDate(int userId) {
        String sql = """
                SELECT subscription_date 
                FROM subscriptions 
                WHERE user_id = ? 
                ORDER BY sub_id DESC 
                LIMIT 1
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getDate("subscription_date") != null) {
                    return rs.getDate("subscription_date").toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "N/A";
    }

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
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getDate("expiry_date") != null) {
                    return rs.getDate("expiry_date").toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "N/A";
    }
}