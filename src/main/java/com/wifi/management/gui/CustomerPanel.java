package com.wifi.management.gui;

import com.wifi.management.model.User;
import com.wifi.management.service.UserService;
import javax.swing.*;
import java.awt.*;

public class CustomerPanel extends JPanel {

    private User currentUser;
    private UserService userService;

    public CustomerPanel(User user) {
        this.currentUser = user;
        this.userService = new UserService();
        prepareGUI();
    }

    private void prepareGUI() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(241, 242, 246));
        setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        // --- 1. Top Section: Profile Icon & Title ---
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setOpaque(false);

        // Profile Avatar Placeholder
        JLabel lblAvatar = new JLabel("👤"); // Unicode icon or you can use an ImageIcon
        lblAvatar.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 80));
        lblAvatar.setForeground(new Color(52, 152, 219));

        JLabel lblName = new JLabel(currentUser.getUsername().toUpperCase());
        lblName.setFont(new Font("Arial", Font.BOLD, 22));

        headerPanel.add(lblAvatar);
        headerPanel.add(Box.createVerticalStrut(10));
        add(headerPanel, BorderLayout.NORTH);

        // --- 2. Information Form ---
        JPanel infoCard = new JPanel(new GridBagLayout());
        infoCard.setBackground(Color.WHITE);
        infoCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Display Data Rows
        addInfoRow(infoCard, "Full Name:", currentUser.getUsername(), 0, gbc);
        addInfoRow(infoCard, "Phone Number:", currentUser.getPhone(), 1, gbc);
        addInfoRow(infoCard, "Home Address:", currentUser.getAddress(), 2, gbc);
        addInfoRow(infoCard, "Account Type:", (currentUser.getRoleId() == 2 ? "Customer" : "Admin"), 3, gbc);

        add(infoCard, BorderLayout.CENTER);

        // --- 3. Bottom Actions ---
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setOpaque(false);

        JButton btnEdit = new JButton("Update Profile Info");
        btnEdit.setBackground(new Color(52, 152, 219));
        btnEdit.setForeground(Color.WHITE);
        btnEdit.setFocusPainted(false);
        btnEdit.setPreferredSize(new Dimension(180, 40));

        actionPanel.add(btnEdit);
        add(actionPanel, BorderLayout.SOUTH);

        // Logic for Update
        btnEdit.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Profile Update feature coming soon!");
        });
    }

    private void addInfoRow(JPanel panel, String label, String value, int row, GridBagConstraints gbc) {
        gbc.gridy = row;

        gbc.gridx = 0;
        JLabel lblKey = new JLabel(label);
        lblKey.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(lblKey, gbc);

        gbc.gridx = 1;
        JLabel lblVal = new JLabel(value);
        lblVal.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(lblVal, gbc);
    }
}