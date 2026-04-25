package com.wifi.management.model;

public class Usage {
    private int usageId;
    private double dataAmount;
    private String timeUsed;
    private int userId;

    public Usage(int usageId, double dataAmount, String timeUsed, int userId) {
        this.usageId = usageId;
        this.dataAmount = dataAmount;
        this.timeUsed = timeUsed;
        this.userId = userId;
    }

    // Getters
    public int getUsageId() { return usageId; }
    public double getDataAmount() { return dataAmount; }
    public String getTimeUsed() { return timeUsed; }
}