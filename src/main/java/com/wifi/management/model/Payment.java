package com.wifi.management.model;

public class Payment {

    private int paymentId;
    private String paymentDate;
    private int subscriptionId;

    // Constructor
    public Payment(int paymentId, String paymentDate, int subscriptionId) {
        this.paymentId = paymentId;
        this.paymentDate = paymentDate;
        this.subscriptionId = subscriptionId;
    }

<<<<<<< HEAD
    // Getters
    public int getPaymentId() {
        return paymentId;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public int getSubscriptionId() {
        return subscriptionId;
    }

    // Setters
    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public void setSubscriptionId(int subscriptionId) {
        this.subscriptionId = subscriptionId;
    }
=======
    public int getPaymentId() { return paymentId; }
    public String getPaymentDate() { return paymentDate; }
>>>>>>> 5c3be305d89b8f2e7752ae4797bda4cf3b7ea84a
}