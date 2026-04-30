package com.wifi.management.gui;

import com.wifi.management.utils.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class CustomerPanel extends JPanel {

    public CustomerPanel() {

        setLayout(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);

        model.addColumn("User ID");
        model.addColumn("Name");
        model.addColumn("Phone");
        model.addColumn("Plan");

        add(new JScrollPane(table), BorderLayout.CENTER);

        try (Connection con = DBConnection.getConnection()) {

            ResultSet rs = con.createStatement()
                    .executeQuery("SELECT * FROM view_admin_search_user");

            model.setRowCount(0);

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("user_id"),
                        rs.getString("full_name"),
                        rs.getString("phone"),
                        rs.getString("plan_name")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}