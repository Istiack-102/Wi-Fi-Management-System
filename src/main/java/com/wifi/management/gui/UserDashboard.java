package com.wifi.management.gui;

import javax.swing.*;
import java.awt.*;

public class UserDashboard extends JFrame {

    private int userId;

    JPanel sidebar, mainPanel;

    public UserDashboard(int userId) {

        this.userId = userId;

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
        JButton paymentBtn = createButton("Payment", 110);
        JButton usageBtn = createButton("Usage", 170);

        sidebar.add(planBtn);
        sidebar.add(paymentBtn);
        sidebar.add(usageBtn);

        mainPanel = new JPanel();
        mainPanel.setBounds(200, 0, 700, 550);
        mainPanel.setLayout(new BorderLayout());

        mainPanel.add(new JLabel("Welcome User", SwingConstants.CENTER));

        add(sidebar);
        add(mainPanel);

        planBtn.addActionListener(e ->
                openPanel(new PlanPanel())
        );

        paymentBtn.addActionListener(e ->
                openPanel(new PaymentPanel(userId, false))
        );

        usageBtn.addActionListener(e ->
                openPanel(new UsagePanel(userId,false))
        );

        setVisible(true);
    }

    private JButton createButton(String text, int y) {
        JButton btn = new JButton(text);
        btn.setBounds(20, y, 160, 40);
        return btn;
    }

    private void openPanel(JPanel panel) {
        mainPanel.removeAll();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }
}