package com.wifi.management.gui;

import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JFrame {

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(400, 400);
        setLayout(new GridLayout(5, 1));

        JButton planBtn = new JButton("Plans");
        JButton paymentBtn = new JButton("Payments");
        JButton customerBtn = new JButton("Customer Details");
        JButton subBtn = new JButton("Subscriptions");
        JButton usageBtn = new JButton("Usage Logs");

        add(planBtn);
        add(paymentBtn);
        add(customerBtn);
        add(subBtn);
        add(usageBtn);

        planBtn.addActionListener(e -> new PlanFrame());
        paymentBtn.addActionListener(e -> new PaymentFrame());
        customerBtn.addActionListener(e -> new CustomerDetailsFrame());
        subBtn.addActionListener(e -> new SubscriptionFrame());
        usageBtn.addActionListener(e -> new UsageLogFrame());

        setVisible(true);
    }
}