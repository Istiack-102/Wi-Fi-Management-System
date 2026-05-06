package com.wifi.management.service;

import com.wifi.management.database_operation.ConnectionRequestDAO;
import com.wifi.management.model.ConnectionRequest;
import com.wifi.management.utils.UniqueMacAddress;

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

    // ================= ADMIN - APPROVE (UPDATED) =================
    /**
     * This method now handles both updating the status AND
     * generating/storing the MAC address.
     */
    public boolean approveRequest(int requestId) {
        // 1. Generate the MAC address
        String generatedMac = UniqueMacAddress.generateRandomMac();

        // Fallback if MAC cannot be retrieved
        if (generatedMac == null) {
            generatedMac = "00-00-00-00-00-00";
        }

        // 2. Call the DAO method that saves both status and MAC
        // Note: Make sure you added approveRequestWithMac to your ConnectionRequestDAO!
        return dao.approveRequestWithMac(requestId, generatedMac);
    }

    // ================= ADMIN - REJECT =================
    public boolean rejectRequest(int requestId) {
        return dao.updateRequestStatus(requestId, "rejected");
    }
}