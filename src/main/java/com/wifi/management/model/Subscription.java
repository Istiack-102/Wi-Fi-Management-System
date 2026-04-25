package com.wifi.management.model;

public class Subscription {
    private int subscriptionId;
    private String startDate;
    private String endDate;
    private String status;

    public Subscription(int subscriptionId, String startDate, String endDate, String status) {
        this.subscriptionId = subscriptionId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }
}