package com.wifi.management.model;

public class Admin {

    private int userId;
    private String username;
    private String passwordHash;
    private int roleId;

    // Constructor
    public Admin(int userId, String username, String passwordHash, int roleId) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.roleId = roleId;
    }

    public Admin() {}

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
}