package com.wifi.management.gui;

import com.wifi.management.database_operation.UserDAO;
import com.wifi.management.model.HistoryLog;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class HistoryLogPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private UserDAO userDAO;
    private int targetUserId;
    private boolean isAdminView;
    private JTextField txtSearchUserId;

    public HistoryLogPanel(int userId, boolean isAdminView) {
        this.targetUserId = userId;
        this.isAdminView = isAdminView;
        this.userDAO = new UserDAO();
        prepareGUI();
        loadHistoryData();
    }

    private void prepareGUI() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(241, 242, 246));
        setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setOpaque(false);

        JLabel lblTitle = new JLabel(isAdminView ? "📜 System Audit & Profile Update Logs" : "📝 My Profile Activity Logs");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(44, 62, 80));
        topPanel.add(lblTitle, BorderLayout.WEST);

        if (isAdminView) {
            JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            searchPanel.setOpaque(false);

            JLabel lblSearch = new JLabel("Find by User ID:");
            lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 14));
            searchPanel.add(lblSearch);

            txtSearchUserId = new JTextField(8);
            txtSearchUserId.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            searchPanel.add(txtSearchUserId);

            JButton btnSearch = new JButton("Find 🔍");
            btnSearch.setBackground(new Color(52, 152, 219));
            btnSearch.setForeground(Color.WHITE);
            btnSearch.setFont(new Font("Segoe UI", Font.BOLD, 13));
            btnSearch.addActionListener(e -> handleAdminSearch());
            searchPanel.add(btnSearch);

            JButton btnReset = new JButton("Show All");
            btnReset.setBackground(new Color(149, 165, 166));
            btnReset.setForeground(Color.WHITE);
            btnReset.setFont(new Font("Segoe UI", Font.BOLD, 13));
            btnReset.addActionListener(e -> {
                txtSearchUserId.setText("");
                loadHistoryData();
            });
            searchPanel.add(btnReset);

            topPanel.add(searchPanel, BorderLayout.EAST);
        }

        add(topPanel, BorderLayout.NORTH);

        String[] columns = {"Username", "Modified Field", "Old Value", "New Value", "Change Time"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setGridColor(new Color(230, 233, 237));
        table.setSelectionBackground(new Color(52, 152, 219, 40));
        table.setSelectionForeground(Color.BLACK);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(155, 89, 182)); // প্রফেশনাল পার্পল থিম হেডার
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(210, 215, 223)));
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);

        JButton btnRefresh = new JButton("Refresh Logs 🔄");
        btnRefresh.setBackground(new Color(46, 204, 113));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnRefresh.setPreferredSize(new Dimension(150, 40));
        btnRefresh.setFocusPainted(false);

        btnRefresh.addActionListener(e -> {
            if (isAdminView && txtSearchUserId != null && !txtSearchUserId.getText().trim().isEmpty()) {
                handleAdminSearch();
            } else {
                loadHistoryData();
            }
        });

        bottomPanel.add(btnRefresh);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadHistoryData() {
        tableModel.setRowCount(0);
        List<HistoryLog> logs;

        if (isAdminView) {
            logs = userDAO.getAllProfileHistoryLogs();
        } else {
            logs = userDAO.getIndividualProfileHistory(targetUserId);
        }

        populateTable(logs);
    }

    private void handleAdminSearch() {
        String inputId = txtSearchUserId.getText().trim();
        if (inputId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a User ID first.");
            return;
        }

        try {
            int userId = Integer.parseInt(inputId);
            tableModel.setRowCount(0);

            List<HistoryLog> logs = userDAO.getIndividualProfileHistory(userId);
            populateTable(logs);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid User ID! Please enter a valid number.");
        }
    }

    private void populateTable(List<HistoryLog> logs) {
        if (logs == null || logs.isEmpty()) {
            Object[] emptyRow = {
                    "",
                    "",
                    "No Activity Logs Found ❌",
                    "",
                    ""
            };
            tableModel.addRow(emptyRow);
            return;
        }

        for (HistoryLog log : logs) {
            Object[] row = {
                    log.getUsername(),
                    log.getFieldName().toUpperCase().replace("_", " "),
                    log.getOldValue() != null ? log.getOldValue() : "N/A",
                    log.getNewValue() != null ? log.getNewValue() : "N/A",
                    log.getChangedAt().toString()
            };
            tableModel.addRow(row);
        }
    }
}