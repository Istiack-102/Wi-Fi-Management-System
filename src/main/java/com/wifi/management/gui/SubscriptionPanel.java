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

        // ================= GET STATUS =================
        String status = subService.getSubscriptionStatus(currentUser.getUserId());

        // ================= TOP SECTION =================
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);

        JLabel lblHeader = new JLabel("Current Subscription Status: ");
        lblHeader.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel lblStatus = new JLabel();

        if (status == null) {
            lblStatus.setText(" NO SUBSCRIPTION ");
            lblStatus.setBackground(Color.GRAY);
        }
        else if (status.equals("pending")) {
            lblStatus.setText(" PENDING ");
            lblStatus.setBackground(new Color(241, 196, 15));
        }
        else if (status.equals("rejected")) {
            lblStatus.setText(" REJECTED ");
            lblStatus.setBackground(new Color(231, 76, 60));
        }
        else {
            lblStatus.setText(" ACTIVE ");
            lblStatus.setBackground(new Color(46, 204, 113));
        }

        lblStatus.setOpaque(true);
        lblStatus.setForeground(Color.WHITE);
        lblStatus.setFont(new Font("Arial", Font.BOLD, 14));

        topPanel.add(lblHeader);
        topPanel.add(lblStatus);

        add(topPanel, BorderLayout.NORTH);

        // ================= CENTER SECTION =================
        JPanel detailsCard = new JPanel();
        detailsCard.setLayout(new GridLayout(4, 2, 10, 20));
        detailsCard.setBackground(Color.WHITE);
        detailsCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        if (status != null && status.equals("accepted")) {

            // REAL DATA FROM SERVICE
            addDetailRow(detailsCard, "Plan Name:", subService.getPlanName(currentUser.getUserId()));
            addDetailRow(detailsCard, "Monthly Cost:", subService.getPrice(currentUser.getUserId()));
            addDetailRow(detailsCard, "Activation Date:", subService.getStartDate(currentUser.getUserId()));
            addDetailRow(detailsCard, "Expiry Date:", subService.getExpiryDate(currentUser.getUserId()));

        } else {

            addDetailRow(detailsCard, "Message:", "No Active Subscription");
            addDetailRow(detailsCard, "Action:", "Request Connection First");
            addDetailRow(detailsCard, "Status Info:", (status == null ? "Not Requested" : status));
            addDetailRow(detailsCard, "Hint:", "Wait for admin approval");
        }

        add(detailsCard, BorderLayout.CENTER);

        // ================= BOTTOM SECTION =================
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);

        JLabel lblDaysLeft = new JLabel("⏳ Days Remaining: --");
        lblDaysLeft.setFont(new Font("Arial", Font.ITALIC, 16));
        lblDaysLeft.setForeground(new Color(44, 62, 80));

        JButton btnRenew = new JButton("Renew / Upgrade Plan");
        btnRenew.setBackground(new Color(52, 152, 219));
        btnRenew.setForeground(Color.WHITE);
        btnRenew.setFocusPainted(false);

        bottomPanel.add(lblDaysLeft, BorderLayout.WEST);
        bottomPanel.add(btnRenew, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    // ================= HELPER =================
    private void addDetailRow(JPanel panel, String label, String value) {

        JLabel lblKey = new JLabel(label);
        lblKey.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Arial", Font.PLAIN, 14));

        panel.add(lblKey);
        panel.add(lblValue);
    }
}