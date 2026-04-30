package com.wifi.management.gui;

import javax.swing.*;
import java.awt.*;

public class UserDashboard extends JFrame {

    JPanel sidebar, mainPanel;

    public UserDashboard() {

        setTitle("User Dashboard");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        sidebar = new JPanel();
        sidebar.setBounds(0, 0, 200, 550);
        sidebar.setBackground(new Color(30, 30, 30));
        sidebar.setLayout(null);

        JButton planBtn = createButton("My Plan", 50);
        JButton paymentBtn = createButton("Payment History", 110);
        JButton usageBtn = createButton("Usage", 170);

        sidebar.add(planBtn);
        sidebar.add(paymentBtn);
        sidebar.add(usageBtn);

        mainPanel = new JPanel();
        mainPanel.setBounds(200, 0, 700, 550);
        mainPanel.setLayout(new BorderLayout());

        JLabel welcome = new JLabel("Welcome User Dashboard", SwingConstants.CENTER);
        welcome.setFont(new Font("Arial", Font.BOLD, 22));

        mainPanel.add(welcome, BorderLayout.CENTER);

        add(sidebar);
        add(mainPanel);

        planBtn.addActionListener(e -> openPanel("MyPlan"));
        paymentBtn.addActionListener(e -> openPanel("PaymentHistory"));
        usageBtn.addActionListener(e -> openPanel("Usage"));

        setVisible(true);
    }

    private JButton createButton(String text, int y) {
        JButton btn = new JButton(text);
        btn.setBounds(20, y, 160, 40);
        btn.setBackground(new Color(60, 63, 65));
        btn.setForeground(Color.WHITE);
        return btn;
    }

    private void openPanel(String name) {

        mainPanel.removeAll();
        mainPanel.setLayout(new BorderLayout());

        switch (name) {

            case "MyPlan":
                mainPanel.add(new PlanPanel());
                break;

            case "PaymentHistory":
                mainPanel.add(new PaymentPanel());
                break;

            case "Usage":
                mainPanel.add(new UsageLogPanel());
                break;
        }

        mainPanel.revalidate();
        mainPanel.repaint();
    }
}