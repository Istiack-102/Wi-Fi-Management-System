package com.wifi.management.service;

import com.wifi.management.database_operation.ConnectionRequestDAO;
import com.wifi.management.model.ConnectionRequest;

import java.util.List;

public class ConnectionRequestService {

    private ConnectionRequestDAO dao = new ConnectionRequestDAO();

    // ================= USER REQUEST =================
    public boolean requestConnection(int userId, int planId) {
        return dao.insertRequest(userId, planId);
    }

    // ================= USER STATUS =================
    public String getStatus(int userId) {
        return dao.getRequestStatusByUser(userId);
    }

    // ================= ADMIN - GET REQUESTS =================
    public List<ConnectionRequest> getAllPendingRequests() {
        return dao.getAllPendingRequests();
    }

    // ================= ADMIN - APPROVE =================
    public boolean approveRequest(int requestId) {
        return dao.updateRequestStatus(requestId, "accepted");
    }

    // ================= ADMIN - REJECT =================
    public boolean rejectRequest(int requestId) {
        return dao.updateRequestStatus(requestId, "rejected");
    }
}