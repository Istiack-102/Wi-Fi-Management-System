package com.wifi.management.gui;

import javax.swing.*;
import java.awt.*;

public class UserDashboard extends JFrame {

    public UserDashboard() {
        setTitle("User Dashboard");
        setSize(350, 300);
        setLayout(new GridLayout(3,1));

        JButton planBtn = new JButton("My Plan");
        JButton paymentBtn = new JButton("Payment History");
        JButton usageBtn = new JButton("Usage");

        add(planBtn);
        add(paymentBtn);
        add(usageBtn);

        planBtn.addActionListener(e -> new PlanFrame());
        paymentBtn.addActionListener(e -> new PaymentFrame());
        usageBtn.addActionListener(e -> new UsageLogFrame());

        setVisible(true);
    }
}