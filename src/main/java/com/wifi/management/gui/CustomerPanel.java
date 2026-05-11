package com.wifi.management.gui;

import com.wifi.management.model.User;
import com.wifi.management.service.UserService;
import javax.swing.*;
import java.awt.*;

public class CustomerPanel extends JPanel {

    private User currentUser;
    private UserService userService;

    // এডিট করার জন্য টেক্সট ফিল্ডগুলো ডিক্লেয়ার করা হলো
    private JTextField txtFullName, txtPhone, txtAddress;

    public CustomerPanel(User user) {
        this.currentUser = user;
        this.userService = new UserService();
        prepareGUI();
    }

    private void prepareGUI() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(241, 242, 246));
        setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        // --- 1. Top Section: Profile Icon ---
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);

        JLabel lblAvatar = new JLabel("👤");
        lblAvatar.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblAvatar.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 80));
        lblAvatar.setForeground(new Color(52, 152, 219));

        JLabel lblUsername = new JLabel(currentUser.getUsername().toUpperCase());
        lblUsername.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblUsername.setFont(new Font("Arial", Font.BOLD, 22));

        headerPanel.add(lblAvatar);
        headerPanel.add(lblUsername);
        add(headerPanel, BorderLayout.NORTH);

        // --- 2. Information Form (Editable) ---
        JPanel infoCard = new JPanel(new GridBagLayout());
        infoCard.setBackground(Color.WHITE);
        infoCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ইনপুট ফিল্ডগুলো ইউজারের বর্তমান ডাটা দিয়ে ফিলআপ করা
        txtFullName = new JTextField(currentUser.getFullName(), 20);
        txtPhone = new JTextField(currentUser.getPhone(), 20);
        txtAddress = new JTextField(currentUser.getAddress(), 20);

        // Account Type এডিট করা যাবে না, তাই এটি শুধু লেবেল
        String roleStr = (currentUser.getRoleId() == 2) ? "Customer" : "Admin";
        JLabel lblRole = new JLabel(roleStr);
        lblRole.setFont(new Font("Arial", Font.ITALIC, 14));

        // রো যোগ করা
        addEditRow(infoCard, "Full Name:", txtFullName, 0, gbc);
        addEditRow(infoCard, "Phone Number:", txtPhone, 1, gbc);
        addEditRow(infoCard, "Home Address:", txtAddress, 2, gbc);

        // Account Type row
        gbc.gridy = 3; gbc.gridx = 0;
        infoCard.add(new JLabel("Account Type:"), gbc);
        gbc.gridx = 1;
        infoCard.add(lblRole, gbc);

        add(infoCard, BorderLayout.CENTER);

        // --- 3. Bottom Actions: Update Button ---
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setOpaque(false);

        JButton btnSave = new JButton("Save Changes");
        btnSave.setBackground(new Color(46, 204, 113)); // সবুজ কালার
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setFocusPainted(false);
        btnSave.setPreferredSize(new Dimension(180, 40));

        actionPanel.add(btnSave);
        add(actionPanel, BorderLayout.SOUTH);

        // ================= UPDATE LOGIC =================
        btnSave.addActionListener(e -> {
            String newName = txtFullName.getText().trim();
            String newPhone = txtPhone.getText().trim();
            String newAddress = txtAddress.getText().trim();

            if (newName.isEmpty() || newPhone.isEmpty() || newAddress.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fields cannot be empty!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // মডেল অবজেক্ট আপডেট
            currentUser.setFullName(newName);
            currentUser.setPhone(newPhone);
            currentUser.setAddress(newAddress);

            // ডাটাবেস আপডেট কল (UserService এর মাধ্যমে)
            String response = userService.updateProfile(currentUser);

            if (response.equals("Success")) {
                JOptionPane.showMessageDialog(this, "Profile Updated Successfully! ✅");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update profile: " + response, "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void addEditRow(JPanel panel, String labelText, JTextField textField, int row, GridBagConstraints gbc) {
        gbc.gridy = row;

        // Label
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(label, gbc);

        // TextField
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(textField, gbc);
    }
}