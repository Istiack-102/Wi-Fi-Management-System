package com.wifi.management.gui;

import com.wifi.management.model.User;
import javax.swing.*;
import java.awt.*;

public class UsagePanel extends JPanel {

    private User currentUser;

    public UsagePanel(User user) {
        this.currentUser = user;
        prepareGUI();
    }

    private void prepareGUI() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(241, 242, 246));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // --- 1. Header ---
        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setOpaque(false);

        JLabel lblTitle = new JLabel("Data Usage Statistics");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel lblSubtitle = new JLabel("Real-time monitoring for User ID: " + currentUser.getUserId());
        lblSubtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        lblSubtitle.setForeground(Color.GRAY);

        header.add(lblTitle);
        header.add(lblSubtitle);
        add(header, BorderLayout.NORTH);

        // --- 2. Usage Cards Container ---
        JPanel cardsContainer = new JPanel(new GridLayout(1, 2, 20, 0));
        cardsContainer.setOpaque(false);

        // Card 1: Data Consumed Progress
        JPanel progressCard = createCard("Monthly Limit Usage");
        JProgressBar usageBar = new JProgressBar(0, 100);
        usageBar.setValue(65); // Example data: 65% used
        usageBar.setStringPainted(true);
        usageBar.setFont(new Font("Arial", Font.BOLD, 14));
        usageBar.setForeground(new Color(46, 204, 113));
        usageBar.setPreferredSize(new Dimension(300, 30));

        JLabel lblDetail = new JLabel("Used: 65GB / Total: 100GB");
        lblDetail.setHorizontalAlignment(SwingConstants.CENTER);

        progressCard.add(Box.createVerticalStrut(20));
        progressCard.add(usageBar);
        progressCard.add(Box.createVerticalStrut(10));
        progressCard.add(lblDetail);

        // Card 2: Speed/Session Info
        JPanel infoCard = createCard("Connection Stats");
        infoCard.add(new JLabel("Current Speed: 20 Mbps"));
        infoCard.add(new JLabel("Active Devices: 3"));
        infoCard.add(new JLabel("Last Login: Today, 10:30 AM"));
        infoCard.add(new JLabel("Status: Connected"));

        cardsContainer.add(progressCard);
        cardsContainer.add(infoCard);

        add(cardsContainer, BorderLayout.CENTER);
    }

    private JPanel createCard(String title) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(223, 228, 234), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblCardTitle = new JLabel(title);
        lblCardTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblCardTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lblCardTitle);

        return card;
    }
}