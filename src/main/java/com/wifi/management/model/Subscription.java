package wifi.management.model;

import java.sql.Date;

public class Subscription {
    private int subscriptionId;
    private Date startDate;
    private Date endDate;
    private int userId;
    private int planId;

    public Subscription(int subscriptionId, Date startDate, Date endDate, int userId, int planId) {
        this.subscriptionId = subscriptionId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.userId = userId;
        this.planId = planId;
    }

    public int getSubscriptionId() {
        return subscriptionId;
    }
}