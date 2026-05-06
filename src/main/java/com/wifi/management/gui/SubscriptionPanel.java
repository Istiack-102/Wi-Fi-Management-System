package com.wifi.management.gui;

import com.wifi.management.model.User;
import javax.swing.*;
import java.awt.*;

public class SubscriptionPanel extends JPanel {

    public SubscriptionPanel(User user) {
        setLayout(new BorderLayout());
        setBackground(new Color(241, 242, 246));
        setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // --- Title Section ---
        JLabel lblTitle = new JLabel("Your Subscription Details");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(44, 62, 80));
        add(lblTitle, BorderLayout.NORTH);

        // --- Info Card Section ---
        JPanel card = new JPanel(new GridLayout(5, 2, 10, 20));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(224, 224, 224), 1),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        // Helper to add data rows
        // SubscriptionPanel-এর ভেতর কার্ড সেকশনটি এভাবে আপডেট করুন:
        addDataRow(card, "Current Plan:", user.getPlanName() != null ? user.getPlanName() : "No Active Plan");

// ডাตาবেস থেকে স্পিড এবং প্রাইস দেখানো হচ্ছে
        addDataRow(card, "Internet Speed:", user.getPlanName() != null ? user.getSpeed() + " Mbps" : "N/A");

        addDataRow(card, "Monthly Bill:", user.getPlanName() != null ? "Tk. " + user.getPrice() : "N/A");

        addDataRow(card, "Expiry Date:", user.getExpiryDate() != null ? user.getExpiryDate().toString() : "N/A");

        // Status Logic
        String statusText = "Inactive";
        Color statusColor = Color.RED;
        if (user.getExpiryDate() != null) {
            java.util.Date today = new java.util.Date();
            if (user.getExpiryDate().after(today)) {
                statusText = "ACTIVE ✅";
                statusColor = new Color(46, 204, 113);
            } else {
                statusText = "EXPIRED ❌";
            }
        }

        JLabel lblStatusLabel = new JLabel("Account Status:");
        lblStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        JLabel lblStatusValue = new JLabel(statusText);
        lblStatusValue.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblStatusValue.setForeground(statusColor);

        card.add(lblStatusLabel);
        card.add(lblStatusValue);

        add(card, BorderLayout.CENTER);
    }

    private void addDataRow(JPanel panel, String label, String value) {
        JLabel lblKey = new JLabel(label);
        lblKey.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblKey.setForeground(Color.GRAY);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblValue.setForeground(Color.BLACK);

        panel.add(lblKey);
        panel.add(lblValue);
    }
}