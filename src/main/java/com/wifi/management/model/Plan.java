package com.wifi.management.model;

public class Plan {

    private int planId;
    private String planName;
    private int speedLimitMbps;
    private double monthlyPrice;

    // Constructor
    public Plan(int planId, String planName, int speedLimitMbps, double monthlyPrice) {
        this.planId = planId;
        this.planName = planName;
        this.speedLimitMbps = speedLimitMbps;
        this.monthlyPrice = monthlyPrice;
    }

    // Default Constructor
    public Plan() {}

    // Getters
    public int getPlanId() {
        return planId;
    }

    public String getPlanName() {
        return planName;
    }

    public int getSpeedLimitMbps() {
        return speedLimitMbps;
    }

    public double getMonthlyPrice() {
        return monthlyPrice;
    }

    // Setters
    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public void setSpeedLimitMbps(int speedLimitMbps) {
        this.speedLimitMbps = speedLimitMbps;
    }

    public void setMonthlyPrice(double monthlyPrice) {
        this.monthlyPrice = monthlyPrice;
    }
}