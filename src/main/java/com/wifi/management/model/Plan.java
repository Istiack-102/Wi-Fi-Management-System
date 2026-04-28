package com.wifi.management.model;

public class Plan {

    private int planId;
    private String duration;
    private double dataLimit;
    private double price;

    // Constructor
    public Plan(int planId, String duration, double dataLimit, double price) {
        this.planId = planId;
        this.duration = duration;
        this.dataLimit = dataLimit;
        this.price = price;
    }

<<<<<<< HEAD
    // Getters
    public int getPlanId() {
        return planId;
    }

    public String getDuration() {
        return duration;
    }

    public double getDataLimit() {
        return dataLimit;
    }

    public double getPrice() {
        return price;
    }

    // Setters
    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setDataLimit(double dataLimit) {
        this.dataLimit = dataLimit;
    }

    public void setPrice(double price) {
        this.price = price;
    }
=======
    public double getdataLimited(){
        return dataLimit;
    }
    public int getPlanId() { return planId; }
    public double getPrice() { return price; }
>>>>>>> 5c3be305d89b8f2e7752ae4797bda4cf3b7ea84a
}