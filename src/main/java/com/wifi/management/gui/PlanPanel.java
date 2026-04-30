package com.wifi.management.gui;

import com.wifi.management.utils.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class PlanPanel extends JPanel {

    public PlanPanel() {

        setLayout(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);

        model.addColumn("ID");
        model.addColumn("Plan");
        model.addColumn("Speed");
        model.addColumn("Price");

        add(new JScrollPane(table), BorderLayout.CENTER);

        try (Connection con = DBConnection.getConnection()) {

            ResultSet rs = con.createStatement()
                    .executeQuery("SELECT * FROM plans");

            model.setRowCount(0);

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