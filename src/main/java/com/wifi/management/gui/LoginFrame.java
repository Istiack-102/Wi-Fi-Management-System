package com.wifi.management.gui;

import com.wifi.management.model.User;
import com.wifi.management.service.UserService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginFrame extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnRegister;
    private UserService userService;

    public LoginFrame() {
        userService = new UserService();
        prepareGUI();
    }

    private void prepareGUI() {
        setTitle("WiFi Management System - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new java.awt.Insets(10, 10, 10, 10);

        // --- UI Components ---
        JLabel lblTitle = new JLabel("Welcome Back");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(lblTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1; gbc.gridx = 0;
        add(new JLabel("Username:"), gbc);

        txtUsername = new JTextField(15);
        gbc.gridx = 1;
        add(txtUsername, gbc);

        gbc.gridy = 2; gbc.gridx = 0;
        add(new JLabel("Password:"), gbc);

        txtPassword = new JPasswordField(15);
        gbc.gridx = 1;
        add(txtPassword, gbc);

        // --- Buttons ---
        btnLogin = new JButton("Login");
        gbc.gridy = 3; gbc.gridx = 1;
        add(btnLogin, gbc);

        btnRegister = new JButton("No account? Sign Up");
        btnRegister.setBorderPainted(false);
        btnRegister.setContentAreaFilled(false);
        btnRegister.setForeground(Color.BLUE);
        gbc.gridy = 4;
        add(btnRegister, gbc);

        // --- Action Listeners ---
        btnLogin.addActionListener(this::handleLogin);

        btnRegister.addActionListener(e -> {
            this.dispose();
            RegisterFrame regFrame = new RegisterFrame();
            regFrame.setVisible(true);
        });
    }

    private void handleLogin(ActionEvent e) {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Call the service layer (which handles hashing and DB check)
        User user = userService.authenticateUser(username, password);

        if (user != null) {
            this.dispose(); // Close login window

            if (user.getRoleId() == 1) {
                // Open Admin Dashboard
                new AdminDashboard().setVisible(true);
            } else {
                // Open User Dashboard and pass the user object to show their profile
                new UserDashboard(user).setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials!", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}