package wifi.management.model;

import java.sql.Date;

public class Subscription {

    private int subscriptionId;
    private Date startDate;
    private Date endDate;
    private int userId;
    private int planId;
    private String status; // added missing field

    // Constructor
    public Subscription(int subscriptionId, Date startDate, Date endDate, int userId, int planId, String status) {
        this.subscriptionId = subscriptionId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.userId = userId;
        this.planId = planId;
        this.status = status;
    }

    // Getters
    public int getSubscriptionId() {
        return subscriptionId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public int getUserId() {
        return userId;
    }

    public int getPlanId() {
        return planId;
    }

    public String getStatus() {
        return status;
    }

    // Setters
    public void setSubscriptionId(int subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}