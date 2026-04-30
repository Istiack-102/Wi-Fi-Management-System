package com.wifi.management.gui;

import com.wifi.management.utils.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginFrame extends JFrame {

    JTextField usernameField;
    JPasswordField passwordField;

    public LoginFrame() {

        setTitle("WiFi Management Login");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setLayout(null);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font inputFont = new Font("Arial", Font.PLAIN, 13);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(labelFont);
        userLabel.setBounds(50, 40, 100, 25);
        add(userLabel);

        usernameField = new JTextField();
        usernameField.setFont(inputFont);
        usernameField.setBounds(150, 40, 180, 25);
        add(usernameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(labelFont);
        passLabel.setBounds(50, 80, 100, 25);
        add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setFont(inputFont);
        passwordField.setBounds(150, 80, 180, 25);
        add(passwordField);

        JButton loginBtn = new JButton("Login");
        loginBtn.setFont(labelFont);
        loginBtn.setBounds(150, 130, 100, 30);
        add(loginBtn);

        loginBtn.addActionListener(e -> login());

        setVisible(true);
    }

    private void login() {

        try (Connection con = DBConnection.getConnection()) {

            String sql = "SELECT role_id FROM users WHERE username=? AND password_hash=?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, usernameField.getText());
            ps.setString(2, new String(passwordField.getPassword()));

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int role = rs.getInt("role_id");
                dispose();

                if (role == 1)
                    new AdminDashboard();
                else
                    new UserDashboard();

            } else {
                JOptionPane.showMessageDialog(this, "Invalid Login!");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}