package com.wifi.management.gui;

import com.wifi.management.utils.DBConnection;

import javax.swing.*;
import java.sql.*;

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

            String username = userField.getText();
            String password = new String(passField.getPassword());

            try (Connection con = DBConnection.getConnection()) {

                String sql = "SELECT user_id, role_id FROM users WHERE username=? AND password_hash=?";
                PreparedStatement pst = con.prepareStatement(sql);

                pst.setString(1, username);
                pst.setString(2, password);

                ResultSet rs = pst.executeQuery();

                if (rs.next()) {

                    int userId = rs.getInt("user_id");
                    int roleId = rs.getInt("role_id");

                    dispose();

                    if (roleId == 1) {
                        new AdminDashboard();
                    } else {
                        new UserDashboard(userId); // ✅ FIXED
                    }

                } else {
                    JOptionPane.showMessageDialog(this, "Invalid login!");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        setVisible(true);
    }
}