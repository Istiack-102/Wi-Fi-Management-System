package com.wifi.management.model;

public class Admin {

    private int adminId;
    private String password;
    private String email;

    // Constructor
    public Admin(int adminId, String password, String email) {
        this.adminId = adminId;
        this.password = password;
        this.email = email;
    }

    // Getters
    public int getAdminId() {
        return adminId;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    // Setters (important for model objects)
    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}