package com.wifi.management.model;

import java.sql.Timestamp;

public class Payment {

    private String transactionId;
    private int userId;
    private double amount;
    private Timestamp paymentDate;
    private String paymentMethod;

    // Constructor
    public Payment(String transactionId, int userId, double amount, Timestamp paymentDate, String paymentMethod) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
    }

    // Default Constructor
    public Payment() {}

    // Getters
    public String getTransactionId() {
        return transactionId;
    }

    public int getUserId() {
        return userId;
    }

    public double getAmount() {
        return amount;
    }

    public Timestamp getPaymentDate() {
        return paymentDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    // Setters
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setPaymentDate(Timestamp paymentDate) {
        this.paymentDate = paymentDate;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}