package com.wifi.management.gui;

import com.wifi.management.model.User;
import com.wifi.management.service.SubscriptionService;
import javax.swing.*;
import java.awt.*;

public class SubscriptionPanel extends JPanel {

    private User currentUser;
    private SubscriptionService subService;

    public SubscriptionPanel(User user) {
        this.currentUser = user;
        this.subService = new SubscriptionService();
        prepareGUI();
    }

    private void prepareGUI() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(241, 242, 246));
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // --- 1. Top Section: Status Badge ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);

        JLabel lblHeader = new JLabel("Current Subscription Status: ");
        lblHeader.setFont(new Font("Arial", Font.BOLD, 18));

        // Example Status Badge (This should ideally come from a DB query)
        JLabel lblStatus = new JLabel("  ACTIVE  ");
        lblStatus.setOpaque(true);
        lblStatus.setBackground(new Color(46, 204, 113)); // Emerald Green
        lblStatus.setForeground(Color.WHITE);
        lblStatus.setFont(new Font("Arial", Font.BOLD, 14));

        topPanel.add(lblHeader);
        topPanel.add(lblStatus);
        add(topPanel, BorderLayout.NORTH);

        // --- 2. Center Section: Details Card ---
        JPanel detailsCard = new JPanel();
        detailsCard.setLayout(new GridLayout(4, 2, 10, 20));
        detailsCard.setBackground(Color.WHITE);
        detailsCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        // Data Rows
        addDetailRow(detailsCard, "Plan Name:", "Ultra Speed - 50Mbps");
        addDetailRow(detailsCard, "Monthly Cost:", "৳ 1200.00");
        addDetailRow(detailsCard, "Activation Date:", "2023-10-01");
        addDetailRow(detailsCard, "Expiry Date:", "2023-11-01");

        add(detailsCard, BorderLayout.CENTER);

        // --- 3. Bottom Section: Countdown & Actions ---
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);

        JLabel lblDaysLeft = new JLabel("⏳ Days Remaining: 14 Days");
        lblDaysLeft.setFont(new Font("Arial", Font.ITALIC, 16));
        lblDaysLeft.setForeground(new Color(44, 62, 80));

        JButton btnRenew = new JButton("Renew or Upgrade Plan");
        btnRenew.setBackground(new Color(52, 152, 219));
        btnRenew.setForeground(Color.WHITE);
        btnRenew.setFocusPainted(false);
        btnRenew.setPreferredSize(new Dimension(200, 40));

        bottomPanel.add(lblDaysLeft, BorderLayout.WEST);
        bottomPanel.add(btnRenew, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void addDetailRow(JPanel panel, String label, String value) {
        JLabel lblKey = new JLabel(label);
        lblKey.setFont(new Font("Arial", Font.BOLD, 14));
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Arial", Font.PLAIN, 14));

        panel.add(lblKey);
        panel.add(lblValue);
    }
}