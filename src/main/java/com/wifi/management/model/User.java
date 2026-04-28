package com.wifi.management.model;

public class User {

    private int userId;
    private String name;
    private String password;
<<<<<<< HEAD
    private String number;
    private String address;
=======
    private int usageTrackingId;
>>>>>>> 5c3be305d89b8f2e7752ae4797bda4cf3b7ea84a

    // Constructor (updated, structure kept)
    public User(int userId, String name, String password, String number, String address) {
        this.userId = userId;
        this.name = name;
        this.password = password;
<<<<<<< HEAD
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
=======
        this.usageTrackingId=usageTrackingId;
    }

    public int getUserId() { return userId; }
    public String getName() { return name; }
    public String getPassword() { return password; }
    public int getUsageTrackingId(){
        return usageTrackingId;
>>>>>>> 5c3be305d89b8f2e7752ae4797bda4cf3b7ea84a
    }
}