package com.wifi.management.gui;

import com.wifi.management.model.User;
import com.wifi.management.model.Plan;
import com.wifi.management.model.ConnectionRequest;
import com.wifi.management.service.UserService;
import com.wifi.management.service.ConnectionRequestService;
import com.wifi.management.database_operation.PlanDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminDashboard extends JFrame {

    private JPanel mainContent;
    private JTextField txtSearch;
    private UserService userService;
    private PlanDAO planDAO;
    private ConnectionRequestService requestService;

    // আধুনিক কালার স্কিম
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

        // Sidebar Setup
        add(createSidebar(), BorderLayout.WEST);

        // Top Bar Setup
        add(createTopBar(), BorderLayout.NORTH);

        // Main Content Area
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
        JButton btnPlans = createSidebarButton("Plan Management");
        JButton btnRequests = createSidebarButton("Active Requests");
        JButton btnLogout = createSidebarButton("Logout System");

        btnLogout.setBackground(DANGER_COLOR);

        sidebar.add(btnUsers);
        sidebar.add(btnPlans);
        sidebar.add(btnRequests);
        sidebar.add(Box.createVerticalStrut(200));
        sidebar.add(btnLogout);

        // Listeners
        btnUsers.addActionListener(e -> showWelcome());
        btnPlans.addActionListener(e -> showPlanEditor());
        btnRequests.addActionListener(e -> showConnectionRequests());
        btnLogout.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

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
        if (row == -1) return;

        int id = (int) table.getValueAt(row, 0);
        boolean success = approve ? requestService.approveRequest(id) : requestService.rejectRequest(id);

        if (success) {
            JOptionPane.showMessageDialog(this, "Request processed successfully.");
            showConnectionRequests();
        }
    }

    private void handleSearch() {
        String keyword = txtSearch.getText().trim();
        List<User> users = userService.searchUsers(keyword);
        String[] cols = {"ID", "Username", "Name", "Phone", "Address"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);

        for (User u : users) {
            model.addRow(new Object[]{u.getUserId(), u.getUsername(), u.getFullName(), u.getPhone(), u.getAddress()});
        }

        JTable table = new JTable(model);
        styleTable(table);
        updateMainContent(new JScrollPane(table), null, "User Search Results: " + keyword);
    }

    // Helper: UI Styling & Updates
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