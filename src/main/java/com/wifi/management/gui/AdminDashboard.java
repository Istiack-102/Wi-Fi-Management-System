package com.wifi.management.gui;

import com.wifi.management.model.User;
import com.wifi.management.model.Plan;
import com.wifi.management.service.UserService;
import com.wifi.management.database_operation.PlanDAO;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AdminDashboard extends JFrame {

    private JPanel mainContent;
    private JTextField txtSearch;

    private UserService userService;
    private PlanDAO planDAO;

    public AdminDashboard() {
        userService = new UserService();
        planDAO = new PlanDAO();
        prepareGUI();
    }

    private void prepareGUI() {

        setTitle("WiFi Management - Admin Panel");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ================= SIDEBAR =================
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(34, 47, 62));
        sidebar.setPreferredSize(new Dimension(220, 700));
        sidebar.setLayout(new GridLayout(10, 1, 5, 5));

        JLabel lblAdmin = new JLabel("  ADMIN PANEL");
        lblAdmin.setForeground(Color.WHITE);
        lblAdmin.setFont(new Font("Arial", Font.BOLD, 16));
        sidebar.add(lblAdmin);

        JButton btnUsers = createSidebarButton("Manage Users");
        JButton btnPlans = createSidebarButton("Edit Plans");
        JButton btnLogout = createSidebarButton("Logout");

        // Logout styling
        btnLogout.setBackground(new Color(231, 76, 60));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFont(new Font("Arial", Font.BOLD, 14));

        sidebar.add(btnUsers);
        sidebar.add(btnPlans);
        sidebar.add(new JLabel(""));
        sidebar.add(btnLogout);

        add(sidebar, BorderLayout.WEST);

        // ================= TOP BAR =================
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        txtSearch = new JTextField(20);
        JButton btnSearch = new JButton("Search");

        btnSearch.setFont(new Font("Arial", Font.BOLD, 13));

        topBar.add(new JLabel("Search User: "));
        topBar.add(txtSearch);
        topBar.add(btnSearch);

        add(topBar, BorderLayout.NORTH);

        // ================= MAIN CONTENT =================
        mainContent = new JPanel(new BorderLayout());
        add(mainContent, BorderLayout.CENTER);

        showWelcome();

        // ================= ACTIONS =================
        btnSearch.addActionListener(e -> handleSearch());
        btnUsers.addActionListener(e -> showWelcome());
        btnPlans.addActionListener(e -> showPlanEditor());

        btnLogout.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
    }

    // ================= MULTI USER SEARCH =================
    private void handleSearch() {

        String keyword = txtSearch.getText().trim();

        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter ID or username");
            return;
        }

        List<User> users = userService.searchUsers(keyword);

        if (users.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No users found");
            return;
        }

        String[] cols = {"ID", "Username", "Name", "Phone", "Address"};
        String[][] data = new String[users.size()][5];

        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            data[i][0] = String.valueOf(u.getUserId());
            data[i][1] = u.getUsername();
            data[i][2] = u.getFullName();
            data[i][3] = u.getPhone();
            data[i][4] = u.getAddress();
        }

        JTable table = new JTable(data, cols);
        table.setRowHeight(25);

        JScrollPane scroll = new JScrollPane(table);

        mainContent.removeAll();
        mainContent.add(scroll);
        mainContent.revalidate();
        mainContent.repaint();
    }

    // ================= PLAN EDITOR =================
    private void showPlanEditor() {

        mainContent.removeAll();

        List<Plan> plans = planDAO.getAllPlans();

        String[] cols = {"ID", "Plan Name", "Speed", "Price"};
        String[][] data = new String[plans.size()][4];

        for (int i = 0; i < plans.size(); i++) {
            Plan p = plans.get(i);
            data[i][0] = String.valueOf(p.getPlanId());
            data[i][1] = p.getPlanName();
            data[i][2] = String.valueOf(p.getSpeedLimitMbps());
            data[i][3] = String.valueOf(p.getMonthlyPrice());
        }

        JTable table = new JTable(data, cols);
        table.setRowHeight(25);

        JScrollPane scroll = new JScrollPane(table);

        // ===== EDIT PANEL =====
        JPanel editPanel = new JPanel();

        JTextField txtId = new JTextField(5);
        JTextField txtSpeed = new JTextField(5);
        JTextField txtPrice = new JTextField(5);

        JButton btnUpdate = new JButton("Update");
        btnUpdate.setFont(new Font("Arial", Font.BOLD, 13));

        editPanel.add(new JLabel("ID:"));
        editPanel.add(txtId);

        editPanel.add(new JLabel("Speed:"));
        editPanel.add(txtSpeed);

        editPanel.add(new JLabel("Price:"));
        editPanel.add(txtPrice);

        editPanel.add(btnUpdate);

        // ===== AUTO FILL =====
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                txtId.setText(table.getValueAt(row, 0).toString());
                txtSpeed.setText(table.getValueAt(row, 2).toString());
                txtPrice.setText(table.getValueAt(row, 3).toString());
            }
        });

        // ===== UPDATE ACTION =====
        btnUpdate.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtId.getText());
                int speed = Integer.parseInt(txtSpeed.getText());
                double price = Double.parseDouble(txtPrice.getText());

                boolean success = planDAO.updatePlan(id, speed, price);

                if (success) {
                    JOptionPane.showMessageDialog(this, "Updated successfully");
                    showPlanEditor();
                } else {
                    JOptionPane.showMessageDialog(this, "Update failed");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input");
            }
        });

        JPanel container = new JPanel(new BorderLayout());
        container.add(scroll, BorderLayout.CENTER);
        container.add(editPanel, BorderLayout.SOUTH);

        mainContent.add(container);
        mainContent.revalidate();
        mainContent.repaint();
    }

    // ================= WELCOME =================
    private void showWelcome() {
        mainContent.removeAll();
        JLabel lbl = new JLabel("Welcome Admin", SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 20));
        mainContent.add(lbl);
        mainContent.revalidate();
        mainContent.repaint();
    }

    // ================= IMPROVED BUTTON STYLE =================
    private JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);

        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(false);

        btn.setBackground(new Color(52, 73, 94));
        btn.setForeground(Color.WHITE);

        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 10));

        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(41, 128, 185));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(52, 73, 94));
            }
        });

        return btn;
    }
}