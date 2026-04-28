package com.wifi.management.service;

import com.wifi.management.database_operation.UserDAO; // To check user info
import com.wifi.management.model.Subscription;
import com.wifi.management.utils.DBConnection;
import java.sql.Date;
import java.time.LocalDate;

public class SubscriptionService {

    // Returns the number of days remaining until the subscription expires
    public long getDaysRemaining(Date expiryDate) {
        if (expiryDate == null) return 0;

        LocalDate today = LocalDate.now();
        LocalDate expiry = expiryDate.toLocalDate();

        if (expiry.isBefore(today)) return 0;

        return java.time.temporal.ChronoUnit.DAYS.between(today, expiry);
    }

    // Checks if the user is currently active or needs a recharge
    public boolean isSubscriptionActive(Subscription sub) {
        if (sub == null || !"active".equalsIgnoreCase(sub.getStatus())) {
            return false;
        }

        // Even if status is active, double check the date
        LocalDate today = LocalDate.now();
        return !sub.getExpiryDate().toLocalDate().isBefore(today);
    }

    // Logic to determine if a user deserves a 'Warning' notification soon
    public boolean needsRenewalNotice(Date expiryDate) {
        long daysLeft = getDaysRemaining(expiryDate);
        // Notify user if only 3 days or less are remaining
        return daysLeft >= 0 && daysLeft <= 3;
    }
}