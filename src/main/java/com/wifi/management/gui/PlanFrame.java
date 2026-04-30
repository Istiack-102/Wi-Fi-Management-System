package com.wifi.management.gui;

import com.wifi.management.utils.DBConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class PlanFrame extends JFrame {

    JTable table;
    DefaultTableModel model;

    public PlanFrame() {
        setTitle("Plans");
        setSize(500, 300);

        model = new DefaultTableModel();
        table = new JTable(model);

        model.addColumn("ID");
        model.addColumn("Plan");
        model.addColumn("Speed");
        model.addColumn("Price");

        loadPlans();

        add(new JScrollPane(table), BorderLayout.CENTER);
        setVisible(true);
    }

    private void loadPlans() {
        try (Connection con = DBConnection.getConnection()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM plans");

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("plan_id"),
                        rs.getString("plan_name"),
                        rs.getInt("speed_limit_mbps"),
                        rs.getDouble("monthly_price")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}