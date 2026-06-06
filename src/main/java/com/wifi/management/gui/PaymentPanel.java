package com.wifi.management.gui;

import com.wifi.management.model.User;
import com.wifi.management.service.PaymentService;
import com.wifi.management.service.PDFGeneratorService;
import com.wifi.management.utils.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PaymentPanel extends JPanel {
    private UserDashboard parent;
    private User currentUser;
    private PaymentService paymentService;
    private double amountToPay;

    // GUI Components
    private JTextField txtCardNumber;
    private JTextField txtExpiry;
    private JTextField txtCVC;
    private JComboBox<String> comboMethod;
    private JComboBox<String> comboPlans;
    private JTextField txtAmount;
    private JButton btnPay;

    private class PlanItem {
        int id;
        String name;
        double price;

        public PlanItem(int id, String name, double price) {
            this.id = id;
            this.name = name;
            this.price = price;
        }

        @Override
        public String toString() {
            return name + " (ID: " + id + ")";
        }
    }

    private List<PlanItem> dbPlanList;

    public PaymentPanel(UserDashboard parent, User user, int planId, double amount) {
        this.parent = parent;
        this.currentUser = user;
        this.amountToPay = amount;
        this.paymentService = new PaymentService();
        this.dbPlanList = new ArrayList<>();

        setLayout(new GridBagLayout());
        setBackground(new Color(245, 246, 250));

        prepareGUI();
        loadPlanChartFromDatabase();

        if (planId > 0) {
            setSelectedPlanFromDirectory(planId);
            comboPlans.setEnabled(false);
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

        // ১. Select Plan Chart
        gbc.gridwidth = 1; gbc.gridy = 1; gbc.gridx = 0;
        add(new JLabel("Select Plan Chart:"), gbc);

        comboPlans = new JComboBox<>();
        comboPlans.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1;
        add(comboPlans, gbc);

        comboPlans.addActionListener(e -> {
            int selectedIndex = comboPlans.getSelectedIndex();
            if (selectedIndex >= 0 && selectedIndex < dbPlanList.size()) {
                PlanItem selectedItem = dbPlanList.get(selectedIndex);
                amountToPay = selectedItem.price;
                txtAmount.setText(amountToPay + " BDT");
            }
        });

        // ২. Payable Amount
        gbc.gridy = 2; gbc.gridx = 0;
        add(new JLabel("Payable Amount:"), gbc);
        txtAmount = new JTextField(15);
        txtAmount.setEditable(false);
        txtAmount.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txtAmount.setBackground(new Color(236, 240, 241));
        gbc.gridx = 1;
        add(txtAmount, gbc);

        // ৩. Payment Method
        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Method:"), gbc);
        comboMethod = new JComboBox<>(new String[]{"Card", "bKash", "Nagad"});
        comboMethod.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1;
        add(comboMethod, gbc);

        // ৪. Card Number
        gbc.gridx = 0; gbc.gridy = 4;
        add(new JLabel("Card/Account No:"), gbc);
        txtCardNumber = new JTextField(15);
        gbc.gridx = 1;
        add(txtCardNumber, gbc);

        // ৫. Expiry Date
        gbc.gridx = 0; gbc.gridy = 5;
        add(new JLabel("Expiry (MM/YY):"), gbc);
        txtExpiry = new JTextField(15);
        gbc.gridx = 1;
        add(txtExpiry, gbc);

        // ৬. CVC
        gbc.gridx = 0; gbc.gridy = 6;
        add(new JLabel("CVC:"), gbc);
        txtCVC = new JTextField(15);
        gbc.gridx = 1;
        add(txtCVC, gbc);

        // ৭. Pay Button
        btnPay = new JButton("Confirm & Pay Now");
        btnPay.setBackground(new Color(46, 204, 113));
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

    private void loadPlanChartFromDatabase() {
        String sql = "SELECT plan_id, plan_name, monthly_price FROM plans";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            comboPlans.removeAllItems();
            dbPlanList.clear();

            while (rs.next()) {
                PlanItem item = new PlanItem(
                        rs.getInt("plan_id"),
                        rs.getString("plan_name"),
                        rs.getDouble("monthly_price")
                );
                dbPlanList.add(item);
                comboPlans.addItem(item.toString());
            }

            if (!dbPlanList.isEmpty() && amountToPay == 0.0) {
                comboPlans.setSelectedIndex(0);
                txtAmount.setText(dbPlanList.get(0).price + " BDT");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load plans chart!", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setSelectedPlanFromDirectory(int planId) {
        for (int i = 0; i < dbPlanList.size(); i++) {
            if (dbPlanList.get(i).id == planId) {
                comboPlans.setSelectedIndex(i);
                txtAmount.setText(dbPlanList.get(i).price + " BDT");
                break;
            }
        }
    }

    private void handlePayment() {
        int selectedIndex = comboPlans.getSelectedIndex();
        if (selectedIndex < 0) {
            JOptionPane.showMessageDialog(this, "Please select a plan from the chart first.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String cardNum = txtCardNumber.getText().trim();
        String expiry = txtExpiry.getText().trim();
        String cvc = txtCVC.getText().trim();
        String method = (String) comboMethod.getSelectedItem();

        if (cardNum.isEmpty() || expiry.isEmpty() || cvc.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All account fields are required!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        PlanItem currentSelectedPlan = dbPlanList.get(selectedIndex);

        String response = paymentService.processNewSubscription(
                currentUser.getUserId(),
                currentSelectedPlan.id,
                cardNum,
                expiry,
                cvc,
                method
        );

        if (response.startsWith("Payment Successful")) {
            JOptionPane.showMessageDialog(this, response, "Success", JOptionPane.INFORMATION_MESSAGE);

            // ট্রানজেকশন আইডি মক করা
            String tempTxnId = "TXN" + (int)(Math.random() * 900000 + 100000);
            if (cardNum.length() >= 4) {
                tempTxnId = "TXN" + cardNum.substring(cardNum.length() - 4) + (int)(Math.random() * 9000 + 1000);
            }

            final String transactionId = tempTxnId;

            // 🔥 আপডেট: SwingWorker ব্যবহার করে ব্যাকগ্রাউন্ড থ্রেডে পিডিএফ তৈরি ও পপ-আপ কন্ট্রোল
            SwingWorker<String, Void> worker = new SwingWorker<>() {
                @Override
                protected String doInBackground() throws Exception {
                    return PDFGeneratorService.generatePaymentReceipt(
                            currentUser,
                            transactionId,
                            currentSelectedPlan.name,
                            amountToPay,
                            method
                    );
                }

                @Override
                protected void done() {
                    try {
                        String pdfPath = get();
                        if (pdfPath != null) {
                            int openConfirm = JOptionPane.showConfirmDialog(
                                    PaymentPanel.this,
                                    "📄 PDF Money Receipt saved to Downloads folder.\nDo you want to open it right now?",
                                    "Receipt Generated",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.QUESTION_MESSAGE
                            );

                            if (openConfirm == JOptionPane.YES_OPTION) {
                                Desktop.getDesktop().open(new File(pdfPath));
                            }
                        } else {
                            JOptionPane.showMessageDialog(PaymentPanel.this,
                                    "⚠️ Payment recorded, but failed to build PDF receipt via iText Engine.",
                                    "PDF Error",
                                    JOptionPane.WARNING_MESSAGE);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    // ৪. প্রসেস শেষে ড্যাশবোর্ডে রিডাইরেক্ট (থ্রেড ফ্রেন্ডলি পজিশনে সরানো হয়েছে)
                    parent.showPanel(new SubscriptionPanel(currentUser));
                }
            };

            worker.execute(); // ব্যাকগ্রাউন্ড থ্রেড রান করা

        } else {
            JOptionPane.showMessageDialog(this, response, "Payment Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}