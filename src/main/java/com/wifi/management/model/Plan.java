package com.wifi.management.model;

public class Plan {
    private int planId;
    private String duration;
    private double dataLimit;
    private double price;

    public Plan(int planId, String duration, double dataLimit, double price) {
        this.planId = planId;
        this.duration = duration;
        this.dataLimit = dataLimit;
        this.price = price;
    }
    // Getters
    public int getPlanId() { return planId; }
    public double getPrice() { return price; }
}