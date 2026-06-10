package com.wifi.management.model;

public class ConnectionRequest {

    private int requestId;
    private int userId;
    private int planId;
    private String status;



    public ConnectionRequest() {
    }

    public ConnectionRequest(int requestId, int userId, int planId, String status) {
        this.requestId = requestId;
        this.userId = userId;
        this.planId = planId;
        this.status = status;
    }

    public ConnectionRequest(int userId, int planId, String status) {
        this.userId = userId;
        this.planId = planId;
        this.status = status;
    }



    public int getRequestId() {
        return requestId;
    }

    public int getUserId() {
        return userId;
    }

    public int getPlanId() {
        return planId;
    }

    public String getStatus() {
        return status;
    }



    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return "ConnectionRequest{" +
                "requestId=" + requestId +
                ", userId=" + userId +
                ", planId=" + planId +
                ", status='" + status + '\'' +
                '}';
    }
}