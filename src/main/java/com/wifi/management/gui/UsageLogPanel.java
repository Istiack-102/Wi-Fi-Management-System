package com.wifi.management.gui;

import com.wifi.management.utils.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class UsageLogPanel extends JPanel {

    public UsageLogPanel() {

        setLayout(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);

        model.addColumn("Log ID");
        model.addColumn("User ID");
        model.addColumn("Data GB");
        model.addColumn("Time");

        add(new JScrollPane(table), BorderLayout.CENTER);

        try (Connection con = DBConnection.getConnection()) {

            ResultSet rs = con.createStatement()
                    .executeQuery("SELECT * FROM usage_logs");

            model.setRowCount(0);

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("usage_id"),
                        rs.getInt("user_id"),
                        rs.getDouble("data_amount_gb"),
                        rs.getTimestamp("time_used")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}