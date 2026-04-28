package com.wifi.management.model;

import java.sql.Date;

public class Subscription {

    private int subId;
    private int userId;
    private int planId;
    private Date expiryDate;
    private String status;

    // Constructor
    public Subscription(int subId, int userId, int planId, Date expiryDate, String status) {
        this.subId = subId;
        this.userId = userId;
        this.planId = planId;
        this.expiryDate = expiryDate;
        this.status = status;
    }

    // Default Constructor
    public Subscription() {}

    // Getters
    public int getSubId() {
        return subId;
    }

    public int getUserId() {
        return userId;
    }

    public int getPlanId() {
        return planId;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public String getStatus() {
        return status;
    }

    // Setters
    public void setSubId(int subId) {
        this.subId = subId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}