package com.wifi.management.gui;

import com.wifi.management.utils.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class SubscriptionPanel extends JPanel {

    public SubscriptionPanel() {

        setLayout(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);

        model.addColumn("Sub ID");
        model.addColumn("User ID");
        model.addColumn("Plan ID");
        model.addColumn("Status");

        add(new JScrollPane(table), BorderLayout.CENTER);

        try (Connection con = DBConnection.getConnection()) {

            ResultSet rs = con.createStatement()
                    .executeQuery("SELECT * FROM subscriptions");

            model.setRowCount(0);

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("sub_id"),
                        rs.getInt("user_id"),
                        rs.getInt("plan_id"),
                        rs.getString("status")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}