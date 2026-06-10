package com.wifi.management.database_operation;

import com.wifi.management.model.User;
import com.wifi.management.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public boolean registerUser(User user) {
        String sqlUser = "INSERT INTO users (username, password_hash, role_id) VALUES (?, ?, ?)";
        String sqlDetails = "INSERT INTO customer_details (user_id, full_name, phone, installation_address) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            int userId = -1;

            try (PreparedStatement pstmt = conn.prepareStatement(sqlUser, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, user.getUsername());
                pstmt.setString(2, user.getPasswordHash());
                pstmt.setInt(3, 2); // Customer role
                pstmt.executeUpdate();

                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    userId = rs.getInt(1);
                }
            }

            try (PreparedStatement pstmt = conn.prepareStatement(sqlDetails)) {
                pstmt.setInt(1, userId);
                pstmt.setString(2, user.getFullName());
                pstmt.setString(3, user.getPhone());
                pstmt.setString(4, user.getAddress());
                pstmt.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User login(String username, String passwordHash) {
        String sql = "SELECT * FROM users WHERE username = ? AND password_hash = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, passwordHash);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setRoleId(rs.getInt("role_id"));
                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public User getUserFullProfile(int userId) {
        // এখানে view_customer_dashboard ব্যবহার করা হয়েছে যা আপনি আগে তৈরি করেছিলেন
        String sql = "SELECT * FROM view_customer_dashboard WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setRoleId(rs.getInt("role_id"));
                user.setUsername(rs.getString("username"));
                user.setFullName(rs.getString("full_name"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("installation_address"));
                user.setSpeed(rs.getInt("speed_limit_mbps"));
                user.setPrice(rs.getDouble("monthly_price"));
                user.setPlanName(rs.getString("plan_name"));
                user.setExpiryDate(rs.getDate("expiry_date"));

                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User searchUserById(int userId) {
        // আপডেটেড ভিউ থেকে সব ডাটা সিলেক্ট করা হচ্ছে
        String sql = "SELECT * FROM view_admin_search_user WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                User user = new User();

                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setFullName(rs.getString("full_name"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("installation_address"));
                user.setMacAddress(rs.getString("mac_address"));
                user.setPlanName(rs.getString("plan_name"));
                user.setExpiryDate(rs.getDate("expiry_date")); // java.sql.Date বা java.util.Date অনুযায়ী

                return user;
            }

        } catch (SQLException e) {
            System.err.println("Error while searching user by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public List<User> searchUsers(String keyword) {

        List<User> list = new ArrayList<>();

        String sql = "SELECT * FROM view_customer_dashboard " +
                "WHERE CAST(user_id AS CHAR) LIKE ? " +
                "OR username LIKE ? " +
                "OR full_name LIKE ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String search = "%" + keyword + "%";

            ps.setString(1, search);
            ps.setString(2, search);
            ps.setString(3, search);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                User u = new User();

                u.setUserId(rs.getInt("user_id"));
                u.setUsername(rs.getString("username"));
                u.setFullName(rs.getString("full_name"));
                u.setPhone(rs.getString("phone"));
                u.setAddress(rs.getString("installation_address"));

                list.add(u);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean isExistingCustomer(int userId) {

        String sql = "SELECT mac_address FROM customer_details WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String mac = rs.getString("mac_address");
                return mac != null && !mac.trim().isEmpty();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
    public List<User> getAllCustomers() {
        List<User> customers = new ArrayList<>();
        String sql = "SELECT v.user_id, v.username, v.full_name, v.phone, v.installation_address, u.role_id " +
                "FROM view_customer_dashboard v " +
                "JOIN users u ON v.user_id = u.user_id " +
                "WHERE u.role_id = 2";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                User user = new User();

                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));

                String fullName = rs.getString("full_name");
                user.setFullName(fullName != null ? fullName.trim() : "N/A");

                String phone = rs.getString("phone");
                user.setPhone(phone != null ? phone.trim() : "N/A");

                String address = rs.getString("installation_address");
                user.setAddress(address != null ? address.trim() : "N/A");

                user.setRoleId(rs.getInt("role_id"));

                customers.add(user);
            }

        } catch (SQLException e) {
            System.err.println("CRITICAL ERROR: Failed to fetch customers from database.");
            e.printStackTrace();
        }

        return customers;
    }

    public int getTotalCustomerCount() {
        String sql = "SELECT COUNT(*) FROM users WHERE role_id = 2";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting customers: " + e.getMessage());
        }
        return 0;
    }
    public boolean updateCustomerProfile(User user) {
        String sql = "UPDATE customer_details SET full_name = ?, phone = ?, installation_address = ? WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getFullName());
            pstmt.setString(2, user.getPhone());
            pstmt.setString(3, user.getAddress());
            pstmt.setInt(4, user.getUserId());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public java.util.List<com.wifi.management.model.HistoryLog> getAllProfileHistoryLogs() {
        java.util.List<com.wifi.management.model.HistoryLog> list = new java.util.ArrayList<>();
        String sql = "SELECT * FROM view_profile_history_logs ORDER BY changed_at DESC";

        try (java.sql.Connection conn = com.wifi.management.utils.DBConnection.getConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql);
             java.sql.ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                list.add(new com.wifi.management.model.HistoryLog(
                        rs.getInt("log_id"),
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("field_name"),
                        rs.getNString("old_value"), // TEXT ফিল্ডের জন্য নিরাপদ
                        rs.getNString("new_value"),
                        rs.getTimestamp("changed_at")
                ));
            }
        } catch (java.sql.SQLException e) {
            System.err.println("Error fetching all history logs: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public java.util.List<com.wifi.management.model.HistoryLog> getIndividualProfileHistory(int userId) {
        java.util.List<com.wifi.management.model.HistoryLog> list = new java.util.ArrayList<>();
        String sql = "SELECT * FROM view_profile_history_logs WHERE user_id = ? ORDER BY changed_at DESC";

        try (java.sql.Connection conn = com.wifi.management.utils.DBConnection.getConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new com.wifi.management.model.HistoryLog(
                            rs.getInt("log_id"),
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("field_name"),
                            rs.getNString("old_value"),
                            rs.getNString("new_value"),
                            rs.getTimestamp("changed_at")
                    ));
                }
            }
        } catch (java.sql.SQLException e) {
            System.err.println("Error fetching individual history logs: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
}