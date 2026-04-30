package com.wifi.management.gui;

import com.wifi.management.utils.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class PaymentPanel extends JPanel {

    public PaymentPanel() {

        setLayout(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);

        model.addColumn("Transaction ID");
        model.addColumn("User ID");
        model.addColumn("Amount");
        model.addColumn("Method");

        add(new JScrollPane(table), BorderLayout.CENTER);

        try (Connection con = DBConnection.getConnection()) {

            ResultSet rs = con.createStatement()
                    .executeQuery("SELECT * FROM transactions");

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
}