package com.wifi.management.gui;

import com.wifi.management.model.User;
import com.wifi.management.service.PaymentService;
import javax.swing.*;
import java.awt.*;

public class PaymentPanel extends JPanel {
    private UserDashboard parent;
    private User currentUser;
    private PaymentService paymentService;
    private double amountToPay; // পেমেন্টের পরিমাণ রাখার জন্য

    // GUI Components
    private JTextField txtCardNumber;
    private JTextField txtExpiry;
    private JTextField txtCVC;
    private JComboBox<String> comboMethod;
    private JTextField txtPlanId;
    private JTextField txtAmount; // টাকা দেখানোর জন্য নতুন ফিল্ড
    private JButton btnPay;

    // কনস্ট্রাক্টর আপডেট করা হয়েছে (৪টি প্যারামিটার)
    public PaymentPanel(UserDashboard parent, User user, int planId, double amount) {
        this.parent = parent;
        this.currentUser = user;
        this.amountToPay = amount;
        this.paymentService = new PaymentService();

        setLayout(new GridBagLayout());
        setBackground(new Color(245, 246, 250));
        prepareGUI();

        // যদি PlanPanel থেকে ডাটা আসে, তবে সেগুলো ফিল্ডে বসিয়ে দেওয়া
        if (planId > 0) {
            txtPlanId.setText(String.valueOf(planId));
            txtPlanId.setEditable(false); // আইডি পরিবর্তন করা যাবে না
            txtAmount.setText(amount + " BDT");
        }
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

        // Payable Amount (নতুন যোগ করা হয়েছে)
        gbc.gridy = 2; gbc.gridx = 0;
        add(new JLabel("Payable Amount:"), gbc);
        txtAmount = new JTextField(15);
        txtAmount.setEditable(false); // এটি শুধুমাত্র দেখানোর জন্য
        gbc.gridx = 1;
        add(txtAmount, gbc);

        // Payment Method
        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Method:"), gbc);
        comboMethod = new JComboBox<>(new String[]{"Card", "bKash", "Nagad"});
        gbc.gridx = 1;
        add(comboMethod, gbc);

        // Card Number
        gbc.gridx = 0; gbc.gridy = 4;
        add(new JLabel("Card/Account No:"), gbc);
        txtCardNumber = new JTextField(15);
        gbc.gridx = 1;
        add(txtCardNumber, gbc);

        // Expiry Date
        gbc.gridx = 0; gbc.gridy = 5;
        add(new JLabel("Expiry (MM/YY):"), gbc);
        txtExpiry = new JTextField(15);
        gbc.gridx = 1;
        add(txtExpiry, gbc);

        // CVC
        gbc.gridx = 0; gbc.gridy = 6;
        add(new JLabel("CVC:"), gbc);
        txtCVC = new JTextField(15);
        gbc.gridx = 1;
        add(txtCVC, gbc);

        // Pay Button
        btnPay = new JButton("Confirm & Pay Now");
        btnPay.setBackground(new Color(46, 204, 113)); // সবুজ রঙ (Success ভাব আনতে)
        btnPay.setForeground(Color.WHITE);
        btnPay.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnPay.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        add(btnPay, gbc);

        // Footer
        JLabel lblNote = new JLabel("Your payment is encrypted and secure.", SwingConstants.CENTER);
        lblNote.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblNote.setForeground(Color.GRAY);
        gbc.gridy = 8;
        add(lblNote, gbc);

        btnPay.addActionListener(e -> handlePayment());
    }

    private void handlePayment() {
        String planIdStr = txtPlanId.getText().trim();
        String cardNum = txtCardNumber.getText().trim();
        String expiry = txtExpiry.getText().trim();
        String cvc = txtCVC.getText().trim();
        String method = (String) comboMethod.getSelectedItem();

        if (planIdStr.isEmpty() || cardNum.isEmpty() || expiry.isEmpty() || cvc.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int planId = Integer.parseInt(planIdStr);

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
                // পেমেন্ট সফল হলে সাবস্ক্রিপশন স্ট্যাটাস পেজে নিয়ে যাবে
                parent.showPanel(new SubscriptionPanel(currentUser));
            } else {
                JOptionPane.showMessageDialog(this, response, "Payment Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Plan ID must be a number!", "Format Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}