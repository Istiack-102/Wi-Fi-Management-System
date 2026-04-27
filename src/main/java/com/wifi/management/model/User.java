package com.wifi.management.model;

public class User {
    private int userId;
    private String name;
    private String password;
    private int usageTrackingId;

    public User(int userId, String name, String password) {
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.usageTrackingId=usageTrackingId;
    }

    public int getUserId() { return userId; }
    public String getName() { return name; }
    public String getPassword() { return password; }
    public int getUsageTrackingId(){
        return usageTrackingId;
    }
}