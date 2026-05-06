package com.wifi.management.database_operation;

import com.wifi.management.model.ConnectionRequest;
import com.wifi.management.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConnectionRequestDAO {

    // ================= INSERT REQUEST =================
    public boolean insertRequest(int userId, int planId) {

        String sql = "INSERT INTO connection_requests (user_id, plan_id) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, planId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ================= GET STATUS =================
    public String getRequestStatusByUser(int userId) {

        String sql = "SELECT status FROM connection_requests " +
                "WHERE user_id = ? ORDER BY request_id DESC LIMIT 1";

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

    // ================= GET ALL PENDING =================
    public List<ConnectionRequest> getAllPendingRequests() {

        List<ConnectionRequest> list = new ArrayList<>();

        String sql = "SELECT * FROM connection_requests WHERE status = 'pending'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                ConnectionRequest r = new ConnectionRequest();

                r.setRequestId(rs.getInt("request_id"));
                r.setUserId(rs.getInt("user_id"));
                r.setPlanId(rs.getInt("plan_id"));
                r.setStatus(rs.getString("status"));

                list.add(r);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ================= UPDATE STATUS =================
    public boolean updateRequestStatus(int requestId, String status) {

        String sql = "UPDATE connection_requests SET status = ? WHERE request_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, requestId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    // ================= UPDATE STATUS WITH MAC =================
    public boolean approveRequestWithMac(int requestId, String macAddress) {
        // ১. connection_requests টেবিলের স্ট্যাটাস এবং MAC আপডেট
        String sqlUpdateReq = "UPDATE connection_requests SET status = 'accepted', mac_address = ? WHERE request_id = ?";

        // ২. customer_details টেবিলে MAC আপডেট (রিকোয়েস্ট আইডির মাধ্যমে ইউজার খুঁজে)
        String sqlUpdateCustomer = "UPDATE customer_details SET mac_address = ? " +
                "WHERE user_id = (SELECT user_id FROM connection_requests WHERE request_id = ?)";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            if (conn == null) return false;

            conn.setAutoCommit(false); // Transaction শুরু

            boolean reqUpdated = false;
            boolean customerUpdated = false;

            // পার্ট ১: Connection Request টেবিল আপডেট করা
            try (PreparedStatement ps1 = conn.prepareStatement(sqlUpdateReq)) {
                ps1.setString(1, macAddress);
                ps1.setInt(2, requestId);
                int rowsAffected = ps1.executeUpdate();
                if (rowsAffected > 0) reqUpdated = true;
            }

            // পার্ট ২: Customer Details টেবিল আপডেট করা
            try (PreparedStatement ps2 = conn.prepareStatement(sqlUpdateCustomer)) {
                ps2.setString(1, macAddress);
                ps2.setInt(2, requestId);
                int rowsAffected = ps2.executeUpdate();
                // কিছু ক্ষেত্রে ইউজারের ডিটেইলস আগে থেকে না থাকলে ০ হতে পারে,
                // তবে সাধারণত এটি ১ হওয়া উচিত।
                if (rowsAffected > 0) customerUpdated = true;
            }

            // দুটি আপডেটই সফল হলে Commit করা হবে
            if (reqUpdated && customerUpdated) {
                conn.commit();
                return true;
            } else {
                // যদি কোনো একটি ফেইল করে তবে রোলব্যাক
                conn.rollback();
                System.err.println("Transaction failed: One or more tables were not updated.");
            }

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    System.err.println("Transaction rolled back due to error.");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Default অবস্থায় ফিরিয়ে নেওয়া
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}