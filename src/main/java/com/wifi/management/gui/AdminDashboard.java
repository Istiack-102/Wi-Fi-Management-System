package com.wifi.management.gui;

import com.wifi.management.model.User;
import com.wifi.management.model.Plan;
import com.wifi.management.model.ConnectionRequest;
import com.wifi.management.service.UserService;
import com.wifi.management.service.ConnectionRequestService;
import com.wifi.management.database_operation.PlanDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class AdminDashboard extends JFrame {

    private JPanel mainContent;
    private JTextField txtSearch;
    private UserService userService;
    private PlanDAO planDAO;
    private ConnectionRequestService requestService;

    private final Color SIDEBAR_COLOR = new Color(44, 62, 80);
    private final Color ACCENT_COLOR = new Color(52, 152, 219);
    private final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private final Color DANGER_COLOR = new Color(231, 76, 60);

    public AdminDashboard() {
        userService = new UserService();
        planDAO = new PlanDAO();
        requestService = new ConnectionRequestService();
        prepareGUI();
    }

    private void prepareGUI() {
        setTitle("WiFi Management System - Admin Control Center");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(createSidebar(), BorderLayout.WEST);

        add(createTopBar(), BorderLayout.NORTH);

        mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(Color.WHITE);
        mainContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(mainContent, BorderLayout.CENTER);

        showWelcome();
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(SIDEBAR_COLOR);
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));

        JLabel lblTitle = new JLabel("ADMIN DASHBOARD");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));
        sidebar.add(lblTitle);

        JButton btnUsers = createSidebarButton("Manage Users");
        btnUsers.addActionListener(e -> showWelcome());
        sidebar.add(btnUsers);

        JButton btnAllCustomers = createSidebarButton("View All Customers");
        btnAllCustomers.addActionListener(e -> showAllCustomers());
        sidebar.add(btnAllCustomers);

        JButton btnPlans = createSidebarButton("Plan Management");
        btnPlans.addActionListener(e -> showPlanEditor());
        sidebar.add(btnPlans);

        JButton btnRequests = createSidebarButton("Active Requests");
        btnRequests.addActionListener(e -> showConnectionRequests());
        sidebar.add(btnRequests);

        // ================= NEW FEATURE BUTTON: AUDIT LOGS =================
        JButton btnAuditLogs = createSidebarButton("Audit Log System 📜");
        btnAuditLogs.setBackground(new Color(155, 89, 182)); // প্রফেশনাল পার্পল কালার
        btnAuditLogs.addActionListener(e -> {
            mainContent.removeAll();
            // userId = 0 কারণ এটি এডমিন ভিউ, এবং isAdminView = true যাতে সবার লগ দেখায়
            HistoryLogPanel adminLogPanel = new HistoryLogPanel(0, true);
            mainContent.add(adminLogPanel, BorderLayout.CENTER);
            mainContent.revalidate();
            mainContent.repaint();
        });
        sidebar.add(btnAuditLogs);

        sidebar.add(Box.createVerticalStrut(100)); // স্ট্রাট সামান্য কমানো হয়েছে নতুন বাটনের জায়গার জন্য

        JButton btnLogout = createSidebarButton("Logout System");
        btnLogout.setBackground(DANGER_COLOR);
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new LoginFrame().setVisible(true);
            }
        });
        sidebar.add(btnLogout);

        return sidebar;
    }

    private JPanel createTopBar() {
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        topBar.setBackground(Color.WHITE);
        topBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

        txtSearch = new JTextField(25);
        txtSearch.setPreferredSize(new Dimension(0, 35));
        JButton btnSearch = new JButton("Search User");
        btnSearch.setBackground(ACCENT_COLOR);
        btnSearch.setForeground(Color.WHITE);

        topBar.add(new JLabel("Quick Search: "));
        topBar.add(txtSearch);
        topBar.add(btnSearch);

        btnSearch.addActionListener(e -> handleSearch());
        return topBar;
    }

    private void showPlanEditor() {
        List<Plan> plans = planDAO.getAllPlans();
        String[] cols = {"ID", "Plan Name", "Speed (Mbps)", "Price (BDT)"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);

        for (Plan p : plans) {
            model.addRow(new Object[]{p.getPlanId(), p.getPlanName(), p.getSpeedLimitMbps(), p.getMonthlyPrice()});
        }

        JTable table = new JTable(model);
        styleTable(table);

        JButton btnEdit = new JButton("Update Selected Plan");
        btnEdit.setBackground(ACCENT_COLOR);
        btnEdit.setForeground(Color.WHITE);
        btnEdit.addActionListener(e -> handlePlanUpdate(table));

        updateMainContent(new JScrollPane(table), btnEdit, "Subscription Plans Management");
    }

    private void handlePlanUpdate(JTable table) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a plan to edit.");
            return;
        }

        int id = (int) table.getValueAt(row, 0);
        String name = JOptionPane.showInputDialog("New Name:", table.getValueAt(row, 1));
        String speed = JOptionPane.showInputDialog("New Speed (Mbps):", table.getValueAt(row, 2));
        String price = JOptionPane.showInputDialog("New Price (BDT):", table.getValueAt(row, 3));

        if (name != null && speed != null && price != null) {
            try {
                Plan p = new Plan();
                p.setPlanId(id);
                p.setPlanName(name);
                p.setSpeedLimitMbps(Integer.parseInt(speed));
                p.setMonthlyPrice(Double.parseDouble(price));

                if (planDAO.updatePlan(p)) {
                    JOptionPane.showMessageDialog(this, "Plan updated successfully!");
                    showPlanEditor();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: Invalid data format.");
            }
        }
    }

    private void showConnectionRequests() {
        List<ConnectionRequest> requests = requestService.getAllPendingRequests();
        String[] cols = {"Request ID", "User ID", "Plan ID", "Status"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);

        for (ConnectionRequest r : requests) {
            model.addRow(new Object[]{r.getRequestId(), r.getUserId(), r.getPlanId(), r.getStatus()});
        }

        JTable table = new JTable(model);
        styleTable(table);

        JButton btnAccept = new JButton("Approve");
        JButton btnReject = new JButton("Reject");
        btnAccept.setBackground(SUCCESS_COLOR);
        btnReject.setBackground(DANGER_COLOR);
        btnAccept.setForeground(Color.WHITE);
        btnReject.setForeground(Color.WHITE);

        btnAccept.addActionListener(e -> processRequest(table, true));
        btnReject.addActionListener(e -> processRequest(table, false));

        JPanel btnPanel = new JPanel();
        btnPanel.add(btnAccept);
        btnPanel.add(btnReject);

        updateMainContent(new JScrollPane(table), btnPanel, "Pending Connection Requests");
    }

    private void processRequest(JTable table, boolean approve) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a request first.");
            return;
        }

        int id = (int) table.getValueAt(row, 0);
        boolean success;

        if (approve) {
            success = requestService.approveRequest(id);
        } else {
            success = requestService.rejectRequest(id);
        }

        if (success) {
            String msg = approve ? "Request Approved and MAC Assigned!" : "Request Rejected.";
            JOptionPane.showMessageDialog(this, msg);
            showConnectionRequests(); // Refresh the table
        } else {
            JOptionPane.showMessageDialog(this, "Operation failed. Check database connection.");
        }
    }

    private void handleSearch() {
        String keyword = txtSearch.getText().trim();

        // যদি সার্চ বক্স একদম খালি রেখে সার্চ বাটনে ক্লিক করা হয়
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a User ID or Username to search.", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<User> users = userService.searchUsers(keyword);

        // 🔥 নতুন ভ্যালিডেশন: যদি এই আইডি বা নামে কোনো ইউজার ডাটাবেসে না থাকে
        if (users == null || users.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No user found with ID or Username: '" + keyword + "' ❌", "User Not Found", JOptionPane.ERROR_MESSAGE);
            return; // নিচে আর যাবে না, ফলে আগের টেবিল ভিউ যেমন ছিল তেমনই থাকবে
        }

        // ইউজার পাওয়া গেলে আগের মতোই টেবিলে ডাটা শো করবে
        String[] cols = {"ID", "Username", "Name", "Phone", "Address"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);

        for (User u : users) {
            model.addRow(new Object[]{u.getUserId(), u.getUsername(), u.getFullName(), u.getPhone(), u.getAddress()});
        }

        JTable table = new JTable(model);
        styleTable(table);
        updateMainContent(new JScrollPane(table), null, "User Search Results: " + keyword);
    }

    private void showAllCustomers() {
        List<User> customers = userService.getAllCustomers();
        int totalCount = userService.getTotalCustomers();

        String[] cols = {"User ID", "Username", "Full Name", "Phone", "Address"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        for (User u : customers) {
            model.addRow(new Object[]{
                    u.getUserId(),
                    u.getUsername(),
                    u.getFullName(),
                    u.getPhone(),
                    u.getAddress()
            });
        }

        JTable table = new JTable(model);
        applyTableStyle(table);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setOpaque(false);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(15, 5, 0, 5));

        JLabel lblTotal = new JLabel("Total Registered Customers: " + totalCount);
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTotal.setForeground(new Color(44, 62, 80));

        footerPanel.add(lblTotal, BorderLayout.WEST);

        updateView(scrollPane, footerPanel, "Customer Management Directory");
    }

    private void applyTableStyle(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(35);
        table.setSelectionBackground(new Color(52, 152, 219));
        table.setSelectionForeground(Color.WHITE);
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 230, 230));

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(44, 62, 80));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(0, 40));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private void updateView(Component content, Component footer, String title) {
        mainContent.removeAll();

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        mainContent.add(lblTitle, BorderLayout.NORTH);
        mainContent.add(content, BorderLayout.CENTER);

        if (footer != null) {
            mainContent.add(footer, BorderLayout.SOUTH);
        }

        mainContent.revalidate();
        mainContent.repaint();
    }

    private void updateMainContent(Component comp, Component actions, String title) {
        mainContent.removeAll();
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        mainContent.add(lblTitle, BorderLayout.NORTH);
        mainContent.add(comp, BorderLayout.CENTER);
        if (actions != null) mainContent.add(actions, BorderLayout.SOUTH);

        mainContent.revalidate();
        mainContent.repaint();
    }

    private void styleTable(JTable table) {
        table.setRowHeight(35);
        table.setSelectionBackground(ACCENT_COLOR);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }

    private void showWelcome() {
        mainContent.removeAll();
        JLabel lbl = new JLabel("System Overview & Admin Tools", SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 24));
        mainContent.add(lbl, BorderLayout.CENTER);
        mainContent.revalidate();
        mainContent.repaint();
    }

    private JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(220, 45));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(new Color(52, 73, 94));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        return btn;
    }
}