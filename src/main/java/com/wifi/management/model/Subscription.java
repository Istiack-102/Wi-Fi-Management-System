package wifi.management.model;

import java.sql.Date;

public class Subscription {

    private int subscriptionId;
    private Date startDate;
    private Date endDate;
    private int userId;
    private int planId;

<<<<<<< HEAD
    // Constructor
    public Subscription(int subscriptionId, String startDate, String endDate, String status) {
=======
    public Subscription(int subscriptionId, Date startDate, Date endDate, int userId, int planId) {
>>>>>>> 5c3be305d89b8f2e7752ae4797bda4cf3b7ea84a
        this.subscriptionId = subscriptionId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.userId = userId;
        this.planId = planId;
    }

    public int getSubscriptionId() {
        return subscriptionId;
    }

    // Getters
    public int getSubscriptionId() {
        return subscriptionId;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getStatus() {
        return status;
    }

    // Setters
    public void setSubscriptionId(int subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}