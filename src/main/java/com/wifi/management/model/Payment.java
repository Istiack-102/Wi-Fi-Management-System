package com.wifi.management.model;

public class Payment {
    private int paymentId;
    private String paymentDate;
    private int subscriptionId;

    public Payment(int paymentId, String paymentDate, int subscriptionId) {
        this.paymentId = paymentId;
        this.paymentDate = paymentDate;
        this.subscriptionId = subscriptionId;
    }

    // Getters
    public int getPaymentId() { return paymentId; }
    public String getPaymentDate() { return paymentDate; }
}