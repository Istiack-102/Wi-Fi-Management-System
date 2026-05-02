package com.wifi.management.gui;

import com.wifi.management.model.Plan;
import com.wifi.management.model.User;
import com.wifi.management.utils.Card_Checker;
import javax.swing.*;
import java.awt.*;

public class PaymentPanel extends JPanel {

    private UserDashboard parentDashboard;
    private User currentUser;
    private Plan selectedPlan;

    private JTextField txtCardNumber, txtExpiry, txtCVV;
    private JButton btnPay, btnCancel;

    public PaymentPanel(UserDashboard dashboard, User user, Plan plan) {
        this.parentDashboard = dashboard;
        this.currentUser = user;
        this.selectedPlan = plan;
        prepareGUI();
    }

    private void prepareGUI() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(241, 242, 246));
        setBorder(BorderFactory.createEmptyBorder(40, 100, 40, 100));

        // --- 1. Summary Header ---
        JPanel summaryPanel = new JPanel(new GridLayout(2, 1));
        summaryPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("Complete Your Payment");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblAmount = new JLabel("Total Amount to Pay: ৳ " + selectedPlan.getMonthlyPrice());
        lblAmount.setFont(new Font("Arial", Font.PLAIN, 18));
        lblAmount.setForeground(new Color(192, 57, 43)); // Red for urgency/amount
        lblAmount.setHorizontalAlignment(SwingConstants.CENTER);

        summaryPanel.add(lblTitle);
        summaryPanel.add(lblAmount);
        add(summaryPanel, BorderLayout.NORTH);

        // --- 2. Card Form ---
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Card Number
        gbc.gridx = 0; gbc.gridy = 0;
        form.add(new JLabel("Card Number:"), gbc);
        txtCardNumber = new JTextField(20);
        gbc.gridx = 1;
        form.add(txtCardNumber, gbc);

        // Expiry & CVV Row
        gbc.gridx = 0; gbc.gridy = 1;
        form.add(new JLabel("Expiry (MM/YY):"), gbc);
        txtExpiry = new JTextField(5);
        gbc.gridx = 1;
        form.add(txtExpiry, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        form.add(new JLabel("CVV:"), gbc);
        txtCVV = new JPasswordField(3);
        gbc.gridx = 1;
        form.add(txtCVV, gbc);

        add(form, BorderLayout.CENTER);

        // --- 3. Action Buttons ---
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        footer.setOpaque(false);

        btnPay = new JButton("Confirm & Pay");
        btnPay.setBackground(new Color(39, 174, 96));
        btnPay.setForeground(Color.WHITE);
        btnPay.setPreferredSize(new Dimension(150, 40));

        btnCancel = new JButton("Cancel");
        btnCancel.setPreferredSize(new Dimension(100, 40));

        footer.add(btnPay);
        footer.add(btnCancel);
        add(footer, BorderLayout.SOUTH);

        // --- Event Handling ---
        btnPay.addActionListener(e -> processPayment());

        btnCancel.addActionListener(e -> {
            parentDashboard.showPanel(new PlanPanel(parentDashboard, currentUser));
        });
    }

    private void processPayment() {
        String cardNumber = txtCardNumber.getText().trim();

        // 1. Validate using your Utils class (Luhn Algorithm)
        if (!Card_Checker.isValidCard(cardNumber)) {
            JOptionPane.showMessageDialog(this, "Invalid Card Number! Please check again.",
                    "Payment Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2. Here you would normally call PaymentDAO to save the transaction
        // For now, we simulate success
        JOptionPane.showMessageDialog(this, "Payment Successful! \nPlan Activated: " + selectedPlan.getPlanName());

        // 3. Return to Subscription view to see the new active plan
        parentDashboard.showPanel(new SubscriptionPanel(currentUser));
    }
}