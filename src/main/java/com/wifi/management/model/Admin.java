package com.wifi.management.model;

public class Admin {
    private int adminId;
    private String password;
    private String email;

    public Admin(int adminId, String password, String email) {
        this.adminId = adminId;
        this.password = password;
        this.email = email;
    }

    public int getAdminId() { return adminId; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
}