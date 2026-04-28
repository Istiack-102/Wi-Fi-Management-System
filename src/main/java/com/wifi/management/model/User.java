package com.wifi.management.model;

public class User {

    private int userId;
    private String name;
    private String password;
    private String number;
    private String address;

    // Constructor (updated, structure kept)
    public User(int userId, String name, String password, String number, String address) {
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.number = number;
        this.address = address;
    }

    // Getters
    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getNumber() {
        return number;
    }

    public String getAddress() {
        return address;
    }
}