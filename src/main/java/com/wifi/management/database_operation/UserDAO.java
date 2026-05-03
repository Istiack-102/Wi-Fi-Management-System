package com.wifi.management.database_operation;

import com.wifi.management.model.User;
import com.wifi.management.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public boolean registerUser(User user) {
        String sqlUser = "INSERT INTO users (username, password_hash, role_id) VALUES (?, ?, ?)";
        String sqlDetails = "INSERT INTO customer_details (user_id, full_name, phone, installation_address) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            // 1. Insert into users table
            int userId = -1;
            try (PreparedStatement pstmt = conn.prepareStatement(sqlUser, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, user.getUsername());
                pstmt.setString(2, user.getPasswordHash());
                pstmt.setInt(3, 2); // Default role_id = 2 (Customer)
                pstmt.executeUpdate();

                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) userId = rs.getInt(1);
            }

            // 2. Insert into customer_details table
            try (PreparedStatement pstmt = conn.prepareStatement(sqlDetails)) {
                pstmt.setInt(1, userId);
                pstmt.setString(2, user.getFullName());
                pstmt.setString(3, user.getPhone());
                pstmt.setString(4, user.getAddress());
                pstmt.executeUpdate();
            }

            conn.commit(); // Save both if successful
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

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setRoleId(rs.getInt("role_id"));
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public User getUserFullProfile(int userId) {
        // view_customer_dashboard এ যদি role_id না থাকে তবে সরাসরি users টেবিল থেকে নিতে হবে
        String sql = "SELECT v.*, u.role_id FROM view_customer_dashboard v " +
                "JOIN users u ON v.user_id = u.user_id WHERE v.user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setFullName(rs.getString("full_name"));
                    user.setPhone(rs.getString("phone"));
                    user.setAddress(rs.getString("installation_address"));

                    // নিশ্চিত করুন যে সঠিক রোল আইডি সেট হচ্ছে
                    user.setRoleId(rs.getInt("role_id"));

                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User searchUserById(int userId) {
        // Uses the database View we created for Admin Search
        String sql = "SELECT * FROM view_admin_search_user WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setFullName(rs.getString("full_name"));
                    user.setPhone(rs.getString("phone"));
                    // Note: view also contains plan_name and expiry_date which you can display in GUI
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isExistingCustomer(int userId) {
        String sql = "SELECT mac_address FROM customer_details WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String mac = rs.getString("mac_address");
                    // যদি MAC অ্যাড্রেস null না হয় এবং খালি না থাকে, তবে সে পুরাতন কাস্টমার
                    return (mac != null && !mac.trim().isEmpty());
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking customer status: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}