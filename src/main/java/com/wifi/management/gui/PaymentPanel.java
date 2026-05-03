package com.wifi.management.gui;

import com.wifi.management.model.User;
import com.wifi.management.service.PaymentService;
import javax.swing.*;
import java.awt.*;

public class PaymentPanel extends JPanel {
    private UserDashboard parent;
    private User currentUser;
    private PaymentService paymentService;

    // GUI Components
    private JTextField txtCardNumber;
    private JTextField txtExpiry; // নতুন ফিল্ড
    private JTextField txtCVC;    // নতুন ফিল্ড
    private JComboBox<String> comboMethod;
    private JTextField txtPlanId;
    private JButton btnPay;

    public PaymentPanel(UserDashboard parent, User user) {
        this.parent = parent;
        this.currentUser = user;
        this.paymentService = new PaymentService();

        setLayout(new GridBagLayout());
        setBackground(new Color(245, 246, 250));
        prepareGUI();
    }

    private void prepareGUI() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel lblTitle = new JLabel("💳 Secure Payment Gateway", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(lblTitle, gbc);

        // Plan ID
        gbc.gridwidth = 1; gbc.gridy = 1; gbc.gridx = 0;
        add(new JLabel("Plan ID:"), gbc);
        txtPlanId = new JTextField(15);
        gbc.gridx = 1;
        add(txtPlanId, gbc);

        // Payment Method
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Method:"), gbc);
        comboMethod = new JComboBox<>(new String[]{"Card", "bKash", "Nagad"});
        gbc.gridx = 1;
        add(comboMethod, gbc);

        // Card Number
        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Card/Account No:"), gbc);
        txtCardNumber = new JTextField(15);
        gbc.gridx = 1;
        add(txtCardNumber, gbc);

        // Expiry Date (MM/YY)
        gbc.gridx = 0; gbc.gridy = 4;
        add(new JLabel("Expiry (MM/YY):"), gbc);
        txtExpiry = new JTextField(15);
        gbc.gridx = 1;
        add(txtExpiry, gbc);

        // CVC
        gbc.gridx = 0; gbc.gridy = 5;
        add(new JLabel("CVC:"), gbc);
        txtCVC = new JTextField(15);
        gbc.gridx = 1;
        add(txtCVC, gbc);

        // Pay Button
        btnPay = new JButton("Confirm & Pay Now");
        btnPay.setBackground(Color.BLACK);
        btnPay.setForeground(Color.WHITE);
        btnPay.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnPay.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        add(btnPay, gbc);

        // Footer
        JLabel lblNote = new JLabel("Your payment is encrypted and secure.", SwingConstants.CENTER);
        lblNote.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblNote.setForeground(Color.GRAY);
        gbc.gridy = 7;
        add(lblNote, gbc);

        btnPay.addActionListener(e -> handlePayment());
    }

    private void handlePayment() {
        String planIdStr = txtPlanId.getText().trim();
        String cardNum = txtCardNumber.getText().trim();
        String expiry = txtExpiry.getText().trim(); // নতুন ডাটা
        String cvc = txtCVC.getText().trim();       // নতুন ডাটা
        String method = (String) comboMethod.getSelectedItem();

        // ভ্যালিডেশন
        if (planIdStr.isEmpty() || cardNum.isEmpty() || expiry.isEmpty() || cvc.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int planId = Integer.parseInt(planIdStr);

            // আপডেটেড PaymentService কল করা (৬টি প্যারামিটারসহ)
            String response = paymentService.processNewSubscription(
                    currentUser.getUserId(),
                    planId,
                    cardNum,
                    expiry,
                    cvc,
                    method
            );

            if (response.startsWith("Payment Successful")) {
                JOptionPane.showMessageDialog(this, response, "Success", JOptionPane.INFORMATION_MESSAGE);
                parent.showPanel(new SubscriptionPanel(currentUser));
            } else {
                JOptionPane.showMessageDialog(this, response, "Payment Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Plan ID must be a number!", "Format Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}