package com.wifi.management.model;

public class User {

    private int userId;
    private String username;      // ডাটাবেসের 'username' কলামের জন্য
    private String passwordHash;  // ডাটাবেসের 'password_hash' কলামের জন্য
    private String fullName;      // ডাটাবেসের 'full_name' কলামের জন্য
    private String phone;         // ডাটাবেসের 'phone' কলামের জন্য
    private String address;       // ডাটাবেসের 'installation_address' কলামের জন্য
    private int roleId;           // ১ = Admin, ২ = Customer

    // Constructor
    public User(int userId, String username, String passwordHash, String fullName, String phone, String address, int roleId) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.roleId = roleId;
    }

    // Default Constructor
    public User() {}

    // Getters
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public String getFullName() { return fullName; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public int getRoleId() { return roleId; }

    // Setters
    public void setUserId(int userId) { this.userId = userId; }
    public void setUsername(String username) { this.username = username; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAddress(String address) { this.address = address; }
    public void setRoleId(int roleId) { this.roleId = roleId; }
}