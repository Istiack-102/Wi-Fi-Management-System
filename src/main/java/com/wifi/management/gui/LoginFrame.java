package com.wifi.management.gui;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    public LoginFrame() {

        setTitle("Login");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 50, 100, 25);

        JTextField userField = new JTextField();
        userField.setBounds(150, 50, 180, 25);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 100, 100, 25);

        JPasswordField passField = new JPasswordField();
        passField.setBounds(150, 100, 180, 25);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(150, 150, 100, 30);

        add(userLabel);
        add(userField);
        add(passLabel);
        add(passField);
        add(loginBtn);

        loginBtn.addActionListener(e -> {
            // simple demo login
            if (userField.getText().equals("admin")) {
                dispose();
                new AdminDashboard();
            } else {
                dispose();
                new UserDashboard();
            }
        });

        setVisible(true);
    }
}