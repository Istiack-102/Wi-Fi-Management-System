package com.wifi.management.database_operation;

import com.wifi.management.model.User;
import com.wifi.management.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    // ================= REGISTER USER =================
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

    // ================= LOGIN =================
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

    // ================= FULL PROFILE =================
    public User getUserFullProfile(int userId) {

        String sql = "SELECT v.*, u.role_id FROM view_customer_dashboard v " +
                "JOIN users u ON v.user_id = u.user_id WHERE v.user_id = ?";

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
                user.setRoleId(rs.getInt("role_id"));

                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // ================= ADMIN SEARCH BY ID (VIEW) =================
    public User searchUserById(int userId) {

        String sql = "SELECT * FROM view_admin_search_user WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                User user = new User();

                user.setUserId(rs.getInt("user_id"));
                user.setFullName(rs.getString("full_name"));
                user.setPhone(rs.getString("phone"));

                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // ================= NEW: MULTI USER SEARCH (FOR TABLE) =================
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

    // ================= CHECK EXISTING CUSTOMER =================
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
}