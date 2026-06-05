package com.wifi.management.model;

public class User {

    private int userId;
    private String username;
    private String passwordHash;
    private String fullName;
    private String phone;
    private String address;
    private int roleId;
    private String planName;
    private java.sql.Date expiryDate;
    private int speed;
    private double price;

    public int getSpeed() { return speed; }
    public void setSpeed(int speed) { this.speed = speed; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }

    public java.sql.Date getExpiryDate() { return expiryDate; }
    public void setExpiryDate(java.sql.Date expiryDate) { this.expiryDate = expiryDate; }// ১ = Admin, ২ = Customer

    public User(int userId, String username, String passwordHash, String fullName, String phone, String address, int roleId) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.roleId = roleId;
    }

    public User() {}

    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public String getFullName() { return fullName; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public int getRoleId() { return roleId; }

    public void setUserId(int userId) { this.userId = userId; }
    public void setUsername(String username) { this.username = username; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAddress(String address) { this.address = address; }
    public void setRoleId(int roleId) { this.roleId = roleId; }
}