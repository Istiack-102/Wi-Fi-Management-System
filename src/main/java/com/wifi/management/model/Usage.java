package com.wifi.management.model;

import java.sql.Timestamp;

public class Usage {
    private int usageId;
    private double dataAmount; // GB বা MB তে হিসাব রাখার জন্য
    private Timestamp timeUsed; // ডাটাবেসের TIMESTAMP এর সাথে মিল রেখে
    private int userId;

    // Constructor
    public Usage(int usageId, double dataAmount, Timestamp timeUsed, int userId) {
        this.usageId = usageId;
        this.dataAmount = dataAmount;
        this.timeUsed = timeUsed;
        this.userId = userId;
    }

    // Default Constructor
    public Usage() {}

    // Getters
    public int getUsageId() { return usageId; }
    public double getDataAmount() { return dataAmount; }
    public Timestamp getTimeUsed() { return timeUsed; }
    public int getUserId() { return userId; }

    // Setters
    public void setUsageId(int usageId) { this.usageId = usageId; }
    public void setDataAmount(double dataAmount) { this.dataAmount = dataAmount; }
    public void setTimeUsed(Timestamp timeUsed) { this.timeUsed = timeUsed; }
    public void setUserId(int userId) { this.userId = userId; }
}