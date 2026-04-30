package com.wifi.management.gui;

import com.wifi.management.utils.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class PaymentFrame extends JFrame {

    JTextField transactionIdField, userIdField, amountField, methodField;

    public PaymentFrame() {
        setTitle("Payment");
        setSize(400, 250);
        setLayout(new GridLayout(5,2));

        add(new JLabel("Transaction ID:"));
        transactionIdField = new JTextField();
        add(transactionIdField);

        add(new JLabel("User ID:"));
        userIdField = new JTextField();
        add(userIdField);

        add(new JLabel("Amount:"));
        amountField = new JTextField();
        add(amountField);

        add(new JLabel("Method:"));
        methodField = new JTextField();
        add(methodField);

        JButton payBtn = new JButton("Submit");
        add(payBtn);

        payBtn.addActionListener(e -> savePayment());

        setVisible(true);
    }

    private void savePayment() {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "INSERT INTO transactions(transaction_id,user_id,amount,payment_method) VALUES(?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, transactionIdField.getText());
            ps.setInt(2, Integer.parseInt(userIdField.getText()));
            ps.setDouble(3, Double.parseDouble(amountField.getText()));
            ps.setString(4, methodField.getText());

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Payment Successful");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}