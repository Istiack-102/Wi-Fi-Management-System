package com.wifi.management.gui;

import com.wifi.management.utils.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class PaymentPanel extends JPanel {

    private JLabel totalLabel;
    private JTable table;
    private DefaultTableModel model;

    private int userId;
    private boolean isAdmin;

    public PaymentPanel(int userId, boolean isAdmin) {

        this.userId = userId;
        this.isAdmin = isAdmin;

        setLayout(new BorderLayout());

        totalLabel = new JLabel("Loading...", SwingConstants.CENTER);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(totalLabel, BorderLayout.NORTH);

        model = new DefaultTableModel();
        table = new JTable(model);

        model.addColumn("Transaction ID");
        model.addColumn("User ID");
        model.addColumn("Amount");
        model.addColumn("Payment Method");

        add(new JScrollPane(table), BorderLayout.CENTER);

        loadData();
        loadTotal();
    }
    private void loadData() {

        try (Connection con = DBConnection.getConnection()) {

            String sql;

            if (isAdmin) {
                sql = "SELECT * FROM transactions";
            } else {
                sql = "SELECT * FROM transactions WHERE user_id = ?";
            }

            PreparedStatement pst = con.prepareStatement(sql);

            if (!isAdmin) {
                pst.setInt(1, userId);
            }

            ResultSet rs = pst.executeQuery();

            model.setRowCount(0);

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("transaction_id"),
                        rs.getInt("user_id"),
                        rs.getDouble("amount"),
                        rs.getString("payment_method")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadTotal() {

        try (Connection con = DBConnection.getConnection()) {

            if (isAdmin) {

                String sql = "SELECT SUM(amount) FROM transactions";
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql);

                if (rs.next()) {
                    totalLabel.setText("Total Revenue: " + rs.getDouble(1) + " BDT");
                }

            } else {

                String sql = "SELECT total_payment(?)";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setInt(1, userId);

                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    totalLabel.setText("Your Total Payment: " + rs.getDouble(1) + " BDT");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}