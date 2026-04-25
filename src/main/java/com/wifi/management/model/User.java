package com.wifi.management.model;

public class User {
    private int userId;
    private String name;
    private String password;

    public User(int userId, String name, String password) {
        this.userId = userId;
        this.name = name;
        this.password = password;
    }

    public int getUserId() { return userId; }
    public String getName() { return name; }
    public String getPassword() { return password; }
}