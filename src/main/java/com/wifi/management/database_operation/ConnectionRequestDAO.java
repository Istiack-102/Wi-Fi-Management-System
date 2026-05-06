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
}