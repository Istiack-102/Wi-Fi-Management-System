package com.wifi.management.gui;

import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JFrame {

    JPanel sidebar, mainPanel;

    public AdminDashboard() {

        setTitle("Admin Dashboard");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        sidebar = new JPanel();
        sidebar.setBounds(0, 0, 200, 600);
        sidebar.setBackground(new Color(30, 30, 30));
        sidebar.setLayout(null);

        JButton planBtn = btn("Plans", 20);
        JButton payBtn = btn("Payments", 70);
        JButton custBtn = btn("Customers", 120);
        JButton subBtn = btn("Subscriptions", 170);
        JButton logBtn = btn("Usage Logs", 220);

        sidebar.add(planBtn);
        sidebar.add(payBtn);
        sidebar.add(custBtn);
        sidebar.add(subBtn);
        sidebar.add(logBtn);

        mainPanel = new JPanel();
        mainPanel.setBounds(200, 0, 800, 600);
        mainPanel.setLayout(new BorderLayout());

        mainPanel.add(new JLabel("Admin Dashboard", SwingConstants.CENTER));
        add(sidebar);
        add(mainPanel);

        planBtn.addActionListener(e -> open(new PlanPanel()));
        payBtn.addActionListener(e -> open(new PaymentPanel()));
        custBtn.addActionListener(e -> open(new CustomerPanel()));
        subBtn.addActionListener(e -> open(new SubscriptionPanel()));
        logBtn.addActionListener(e -> open(new UsageLogPanel()));

        setVisible(true);
    }

    private JButton btn(String t, int y) {
        JButton b = new JButton(t);
        b.setBounds(20, y, 160, 30);
        return b;
    }

    private void open(JPanel p) {
        mainPanel.removeAll();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(p, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }
}