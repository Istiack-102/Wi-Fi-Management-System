package com.wifi.management.gui;

import com.wifi.management.model.User;
import com.wifi.management.service.UserService;
import javax.swing.*;
import java.awt.*;

public class CustomerPanel extends JPanel {

    private User currentUser;
    private UserService userService;

    private JLabel lblFullNameValue;
    private JLabel lblPhoneValue;
    private JLabel lblAddressValue;

    public CustomerPanel(User user) {
        this.currentUser = user;
        this.userService = new UserService();
        prepareGUI();
    }

    private void prepareGUI() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(241, 242, 246));
        setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

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

        JPanel infoCard = new JPanel(new GridBagLayout());
        infoCard.setBackground(Color.WHITE);
        infoCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.anchor = GridBagConstraints.WEST;

        lblFullNameValue = new JLabel(currentUser.getFullName() != null ? currentUser.getFullName() : "N/A");
        lblPhoneValue = new JLabel(currentUser.getPhone() != null ? currentUser.getPhone() : "N/A");
        lblAddressValue = new JLabel(currentUser.getAddress() != null ? currentUser.getAddress() : "N/A");

        String roleStr = (currentUser.getRoleId() == 2) ? "Customer" : "Admin";
        JLabel lblRoleValue = new JLabel(roleStr);

        Font valueFont = new Font("Arial", Font.PLAIN, 14);
        lblFullNameValue.setFont(valueFont);
        lblPhoneValue.setFont(valueFont);
        lblAddressValue.setFont(valueFont);
        lblRoleValue.setFont(new Font("Arial", Font.ITALIC, 14));

        addInfoRow(infoCard, "Full Name:", lblFullNameValue, 0, gbc);
        addInfoRow(infoCard, "Phone Number:", lblPhoneValue, 1, gbc);
        addInfoRow(infoCard, "Home Address:", lblAddressValue, 2, gbc);

        gbc.gridy = 3; gbc.gridx = 0;
        JLabel lblRoleKey = new JLabel("Account Type:");
        lblRoleKey.setFont(new Font("Arial", Font.BOLD, 14));
        infoCard.add(lblRoleKey, gbc);
        gbc.gridx = 1;
        infoCard.add(lblRoleValue, gbc);

        add(infoCard, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setOpaque(false);

        JButton btnEdit = new JButton("Update Profile Info");
        btnEdit.setBackground(new Color(52, 152, 219)); // ব্লু কালার
        btnEdit.setForeground(Color.WHITE);
        btnEdit.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEdit.setFocusPainted(false);
        btnEdit.setPreferredSize(new Dimension(180, 40));

        actionPanel.add(btnEdit);
        add(actionPanel, BorderLayout.SOUTH);

        btnEdit.addActionListener(e -> {
            openUpdateDialog();
        });
    }

    private void addInfoRow(JPanel panel, String labelText, JLabel valueLabel, int row, GridBagConstraints gbc) {
        gbc.gridy = row;

        gbc.gridx = 0;
        gbc.weightx = 0.3;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(valueLabel, gbc);
    }

    private void openUpdateDialog() {
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        JDialog updateDialog = new JDialog(parentWindow, "Update Profile Information", Dialog.ModalityType.APPLICATION_MODAL);
        updateDialog.setSize(420, 350);
        updateDialog.setLocationRelativeTo(this);
        updateDialog.setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        GridBagConstraints dialogGbc = new GridBagConstraints();
        dialogGbc.insets = new Insets(12, 5, 12, 5);
        dialogGbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtFullName = new JTextField(currentUser.getFullName(), 20);
        JTextField txtPhone = new JTextField(currentUser.getPhone(), 20);
        JTextField txtAddress = new JTextField(currentUser.getAddress(), 20);

        Font fieldFont = new Font("Arial", Font.PLAIN, 14);
        txtFullName.setFont(fieldFont);
        txtPhone.setFont(fieldFont);
        txtAddress.setFont(fieldFont);

        dialogGbc.gridy = 0; dialogGbc.gridx = 0; dialogGbc.weightx = 0.3;
        formPanel.add(new JLabel("Full Name:"), dialogGbc);
        dialogGbc.gridx = 1; dialogGbc.weightx = 0.7;
        formPanel.add(txtFullName, dialogGbc);

        dialogGbc.gridy = 1; dialogGbc.gridx = 0; dialogGbc.weightx = 0.3;
        formPanel.add(new JLabel("Phone Number:"), dialogGbc);
        dialogGbc.gridx = 1; dialogGbc.weightx = 0.7;
        formPanel.add(txtPhone, dialogGbc);

        dialogGbc.gridy = 2; dialogGbc.gridx = 0; dialogGbc.weightx = 0.3;
        formPanel.add(new JLabel("Home Address:"), dialogGbc);
        dialogGbc.gridx = 1; dialogGbc.weightx = 0.7;
        formPanel.add(txtAddress, dialogGbc);

        updateDialog.add(formPanel, BorderLayout.CENTER);

        JButton btnSave = new JButton("Save Changes");
        btnSave.setBackground(new Color(46, 204, 113)); // গ্রিন কালার
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setPreferredSize(new Dimension(140, 38));
        btnSave.setFocusPainted(false);

        btnSave.addActionListener(evt -> {
            String newName = txtFullName.getText().trim();
            String newPhone = txtPhone.getText().trim();
            String newAddress = txtAddress.getText().trim();

            if (newName.isEmpty() || newPhone.isEmpty() || newAddress.isEmpty()) {
                JOptionPane.showMessageDialog(updateDialog, "Fields cannot be empty!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            currentUser.setFullName(newName);
            currentUser.setPhone(newPhone);
            currentUser.setAddress(newAddress);

            String response = userService.updateProfile(currentUser);

            if (response.equals("Success")) {
                JOptionPane.showMessageDialog(updateDialog, "Profile Updated Successfully! ✅");

                lblFullNameValue.setText(newName);
                lblPhoneValue.setText(newPhone);
                lblAddressValue.setText(newAddress);

                updateDialog.dispose();
            } else {
                JOptionPane.showMessageDialog(updateDialog, "Failed to update profile: " + response, "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        bottomPanel.add(btnSave);
        updateDialog.add(bottomPanel, BorderLayout.SOUTH);

        updateDialog.setVisible(true);
    }
}