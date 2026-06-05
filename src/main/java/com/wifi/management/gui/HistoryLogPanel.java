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
    private JTextField txtSearchUserId; // এডমিনের জন্য আইডি দিয়ে সার্চ করার ফিল্ড

    /**
     * @param userId লগইন থাকা ইউজারের আইডি (অ্যাডমিনের জন্য 0 বা যেকোনো আইডি দিলেই হবে)
     * @param isAdminView যদি true হয় তবে সবার লগ দেখাবে, false হলে শুধু নির্দিষ্ট ইউজারের লগ দেখাবে
     */
    public HistoryLogPanel(int userId, boolean isAdminView) {
        this.targetUserId = userId;
        this.isAdminView = isAdminView;
        this.userDAO = new UserDAO();
        prepareGUI();
        loadHistoryData(); // প্যানেল লোড হওয়ার সাথে সাথে ডাটা চলে আসবে
    }

    private void prepareGUI() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(241, 242, 246));
        setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // --- ১. টপ সেকশন: টাইটেল এবং সার্চ বার ---
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setOpaque(false);

        JLabel lblTitle = new JLabel(isAdminView ? "📜 System Audit & Profile Update Logs" : "📝 My Profile Activity Logs");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(44, 62, 80));
        topPanel.add(lblTitle, BorderLayout.WEST);

        // ভিউটি যদি এডমিন প্যানেল থেকে ওপেন হয়, তবেই কেবল সার্চ এবং রিসেট বাটন দেখাবে
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
                loadHistoryData(); // সার্চ ক্লিয়ার করে আবার সব ডাটা লোড করবে
            });
            searchPanel.add(btnReset);

            topPanel.add(searchPanel, BorderLayout.EAST);
        }

        add(topPanel, BorderLayout.NORTH);

        // --- ২. টেবিল কলাম কনফিগারেশন ---
        String[] columns = {"Username", "Modified Field", "Old Value", "New Value", "Change Time"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // অডিট লগ যেন কেউ এডিট করতে না পারে
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setGridColor(new Color(230, 233, 237));
        table.setSelectionBackground(new Color(52, 152, 219, 40));
        table.setSelectionForeground(Color.BLACK);

        // টেবিল হেডার স্টাইল
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(155, 89, 182)); // প্রফেশনাল পার্পল থিম হেডার
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));

        // টেক্সট সেন্টার অ্যালাইনমেন্ট রেন্ডারার
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(210, 215, 223)));
        add(scrollPane, BorderLayout.CENTER);

        // --- ৩. বটম সেকশন: রিফ্রেশ বাটন ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);

        JButton btnRefresh = new JButton("Refresh Logs 🔄");
        btnRefresh.setBackground(new Color(46, 204, 113)); // সবুজ কালার
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnRefresh.setPreferredSize(new Dimension(150, 40));
        btnRefresh.setFocusPainted(false);

        btnRefresh.addActionListener(e -> {
            // যদি সার্চ বক্সে কিছু লেখা থাকে তবে রিফ্রেশ করলে সার্চ রেজাল্টই আপডেট হবে, অন্যথায় সব আসবে
            if (isAdminView && txtSearchUserId != null && !txtSearchUserId.getText().trim().isEmpty()) {
                handleAdminSearch();
            } else {
                loadHistoryData();
            }
        });

        bottomPanel.add(btnRefresh);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // ডিফল্টভাবে সব ডাটা (এডমিন) বা কাস্টমারের নিজস্ব ডাটা লোড করার মেথড
    private void loadHistoryData() {
        tableModel.setRowCount(0); // আগের রো ক্লিয়ার করা
        List<HistoryLog> logs;

        // ভিউ মুডের ওপর ভিত্তি করে সঠিক DAO মেথড কল করা
        if (isAdminView) {
            logs = userDAO.getAllProfileHistoryLogs();
        } else {
            logs = userDAO.getIndividualProfileHistory(targetUserId);
        }

        populateTable(logs);
    }

    // অ্যাডমিন যখন নির্দিষ্ট আইডি দিয়ে সার্চ বক্সে সার্চ করবে
    private void handleAdminSearch() {
        String inputId = txtSearchUserId.getText().trim();
        if (inputId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a User ID first.");
            return;
        }

        try {
            int userId = Integer.parseInt(inputId);
            tableModel.setRowCount(0); // টেবিল ক্লিয়ার করা

            // নির্দিষ্ট ইউজারের ডাটা তুলে আনা
            List<HistoryLog> logs = userDAO.getIndividualProfileHistory(userId);
            populateTable(logs);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid User ID! Please enter a valid number.");
        }
    }

    // টেবিলে ডাটা পুশ করার মেথড (ডাটা না থাকলে টেবিলে নোটিশ শো করবে)
    private void populateTable(List<HistoryLog> logs) {
        // যদি ডাটাবেস থেকে কোনো হিস্ট্রি ডাটা না আসে (List খালি থাকে)
        if (logs == null || logs.isEmpty()) {
            Object[] emptyRow = {
                    "",
                    "",
                    "No Activity Logs Found ❌", // ঠিক মাঝখানের কলামে মেসেজটি দেখাবে
                    "",
                    ""
            };
            tableModel.addRow(emptyRow);
            return; // মেথড এখানেই শেষ
        }

        // আর যদি ডাটা থাকে, তবে লুপ ঘুরে সব ডাটা টেবিলে শো করবে
        for (HistoryLog log : logs) {
            Object[] row = {
                    log.getUsername(),
                    log.getFieldName().toUpperCase().replace("_", " "), // full_name কে FULL NAME দেখাবে
                    log.getOldValue() != null ? log.getOldValue() : "N/A",
                    log.getNewValue() != null ? log.getNewValue() : "N/A",
                    log.getChangedAt().toString()
            };
            tableModel.addRow(row);
        }
    }
}