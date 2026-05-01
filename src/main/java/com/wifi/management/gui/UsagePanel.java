package com.wifi.management.gui;

import com.wifi.management.utils.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class UsagePanel extends JPanel {

    private JLabel totalLabel;

    public UsagePanel(int userId, boolean isAdmin) {

        setLayout(new BorderLayout());

        totalLabel = new JLabel("Loading total usage...", SwingConstants.CENTER);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 18));
        totalLabel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        add(totalLabel, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);

        model.addColumn("Log ID");
        model.addColumn("User ID");
        model.addColumn("Data GB");
        model.addColumn("Time");

        add(new JScrollPane(table), BorderLayout.CENTER);

        loadTable(model, userId, isAdmin);
        loadTotal(userId);
    }

    private void loadTable(DefaultTableModel model, int userId, boolean isAdmin) {

        try (Connection con = DBConnection.getConnection()) {

            String sql = isAdmin ?
                    "SELECT * FROM usage_logs" :
                    "SELECT * FROM usage_logs WHERE user_id=?";

            PreparedStatement pst = con.prepareStatement(sql);

            if (!isAdmin) {
                pst.setInt(1, userId);
            }

            ResultSet rs = pst.executeQuery();

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

    private void loadTotal(int userId) {

        try (Connection con = DBConnection.getConnection()) {

            String sql = "SELECT total_usage(?)";
            PreparedStatement pst = con.prepareStatement(sql);

            pst.setInt(1, userId);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                totalLabel.setText("Total Usage: " + rs.getDouble(1) + " GB");
            }

        } catch (Exception e) {
            e.printStackTrace();
            totalLabel.setText("Error loading total");
        }
    }
}