package com.wifi.management.gui;

import com.wifi.management.model.User;
import com.wifi.management.service.UserService;
import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {

    private JTextField txtUsername, txtPhone, txtAddress, txtFullName;
    private JPasswordField txtPassword;
    private JButton btnRegister, btnBack;
    private UserService userService;

    public RegisterFrame() {
        this.userService = new UserService();
        prepareGUI();
    }

    private void prepareGUI() {
        setTitle("Create WiFi Account");
        setSize(450, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- Header ---
        JPanel header = new JPanel();
        header.setBackground(new Color(52, 152, 219));
        JLabel lblTitle = new JLabel("New User Registration");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        header.add(lblTitle);
        add(header, BorderLayout.NORTH);

        // --- Form Body ---
        JPanel form = new JPanel(new GridLayout(6, 2, 10, 20));
        form.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        form.add(new JLabel("Full Name:"));
        txtFullName = new JTextField();
        form.add(txtFullName);

        form.add(new JLabel("Phone (01xxxxxxxxx):"));
        txtPhone = new JTextField();
        form.add(txtPhone);

        form.add(new JLabel("Address:"));
        txtAddress = new JTextField();
        form.add(txtAddress);

        form.add(new JLabel("Username:"));
        txtUsername = new JTextField();
        form.add(txtUsername);

        form.add(new JLabel("Password:"));
        txtPassword = new JPasswordField();
        form.add(txtPassword);

        add(form, BorderLayout.CENTER);

        // --- Footer Buttons ---
        JPanel footer = new JPanel();
        btnRegister = new JButton("Register Now");
        btnRegister.setBackground(new Color(46, 204, 113));
        btnRegister.setForeground(Color.WHITE);

        btnBack = new JButton("Back to Login");

        footer.add(btnRegister);
        footer.add(btnBack);
        add(footer, BorderLayout.SOUTH);

        // --- Logic ---
        btnRegister.addActionListener(e -> handleRegistration());
        btnBack.addActionListener(e -> {
            this.dispose();
            new LoginFrame().setVisible(true);
        });
    }

    private void handleRegistration() {
        // 1. Collect Data
        String fullName = txtFullName.getText().trim();
        String phone = txtPhone.getText().trim();
        String address = txtAddress.getText().trim();
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        // Basic Empty Check
        if (username.isEmpty() || password.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all mandatory fields!");
            return;
        }

        // 2. Create Model Object
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPhone(phone);
        newUser.setAddress(address);
        // FullName can be added to your model if not there yet

        // Default role for new signups is 'Customer' (ID: 2)
        newUser.setRoleId(2);

        // 3. Call Service Layer (Handles BD Number, Password Strength, and Hashing)
        String result = userService.registerNewCustomer(newUser, password);

        if (result.equals("Registration Successful")) {
            JOptionPane.showMessageDialog(this, "Account Created Successfully!");
            this.dispose();
            new LoginFrame().setVisible(true);
        } else {
            // Show the specific error (e.g., "Invalid BD Number" or "Weak Password")
            JOptionPane.showMessageDialog(this, result, "Registration Error", JOptionPane.WARNING_MESSAGE);
        }
    }
}