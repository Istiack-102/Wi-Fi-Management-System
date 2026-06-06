package com.wifi.management.service;

import com.wifi.management.database_operation.ConnectionRequestDAO;
import com.wifi.management.model.ConnectionRequest;
import com.wifi.management.utils.UniqueMacAddress;

import java.util.List;

public class ConnectionRequestService {

    private ConnectionRequestDAO dao = new ConnectionRequestDAO();

    public boolean requestConnection(int userId, int planId) {
        return dao.insertRequest(userId, planId);
    }

    public String getStatus(int userId) {
        return dao.getRequestStatusByUser(userId);
    }

    public List<ConnectionRequest> getAllPendingRequests() {
        return dao.getAllPendingRequests();
    }

    public boolean approveRequest(int requestId) {
        String generatedMac = UniqueMacAddress.generateRandomMac();

        if (generatedMac == null) {
            generatedMac = "00-00-00-00-00-00";
        }

        return dao.approveRequestWithMac(requestId, generatedMac);
    }

    public boolean rejectRequest(int requestId) {
        return dao.updateRequestStatus(requestId, "rejected");
    }
}