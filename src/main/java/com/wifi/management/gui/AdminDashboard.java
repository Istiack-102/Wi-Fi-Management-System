package com.wifi.management.gui;

import com.wifi.management.model.User;
import com.wifi.management.service.UserService;
import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JFrame {

    private JPanel mainContent;
    private UserService userService;
    private JTextField txtSearch;

    public AdminDashboard() {
        this.userService = new UserService();
        prepareGUI();
    }

    private void prepareGUI() {
        setTitle("WiFi Management - Admin Control Panel");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- 1. Sidebar (Admin Navigation) ---
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(34, 47, 62)); // Darker slate color
        sidebar.setPreferredSize(new Dimension(220, 700));
        sidebar.setLayout(new GridLayout(10, 1, 5, 5));

        JLabel lblAdmin = new JLabel("  ADMIN PANEL", SwingConstants.LEFT);
        lblAdmin.setForeground(Color.WHITE);
        lblAdmin.setFont(new Font("Arial", Font.BOLD, 16));
        sidebar.add(lblAdmin);

        JButton btnUserSearch = createSidebarButton("Manage Users");
        JButton btnTransactions = createSidebarButton("All Payments");
        JButton btnPlans = createSidebarButton("Edit Plans");
        JButton btnLogout = createSidebarButton("System Logout");
        btnLogout.setBackground(new Color(231, 60, 60));

        sidebar.add(btnUserSearch);
        sidebar.add(btnTransactions);
        sidebar.add(btnPlans);
        sidebar.add(new JLabel("")); // Spacer
        sidebar.add(btnLogout);

        add(sidebar, BorderLayout.WEST);

        // --- 2. Top Bar (Search & Stats) ---
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        topBar.setBackground(Color.WHITE);
        topBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

        txtSearch = new JTextField(20);
        txtSearch.setToolTipText("Enter User ID to Search");
        JButton btnSearch = new JButton("Search User");

        topBar.add(new JLabel("Quick Search (ID):"));
        topBar.add(txtSearch);
        topBar.add(btnSearch);

        add(topBar, BorderLayout.NORTH);

        // --- 3. Main Content Area ---
        mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(new Color(236, 240, 241));
        add(mainContent, BorderLayout.CENTER);

        // Default Welcome View
        showDefaultAdminView();

        // --- Action Listeners ---
        btnSearch.addActionListener(e -> handleSearch());

        btnLogout.addActionListener(e -> {
            this.dispose();
            new LoginFrame().setVisible(true);
        });

        // Sidebar Actions
        btnUserSearch.addActionListener(e -> showDefaultAdminView());
        btnTransactions.addActionListener(e -> {
            mainContent.removeAll();
            mainContent.add(new JLabel("Transaction History Table - Coming Soon", SwingConstants.CENTER));
            mainContent.repaint();
            mainContent.revalidate();
        });
    }

    private void handleSearch() {
        String idStr = txtSearch.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a User ID");
            return;
        }

        try {
            int userId = Integer.parseInt(idStr);
            User foundUser = userService.findUserForAdmin(userId);

            if (foundUser != null) {
                // আমরা আগে বানানো CustomerPanel টি এখানে রি-ইউজ করছি!
                mainContent.removeAll();
                mainContent.add(new CustomerPanel(foundUser), BorderLayout.CENTER);
                mainContent.repaint();
                mainContent.revalidate();
            } else {
                JOptionPane.showMessageDialog(this, "User not found with ID: " + userId);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid ID format. Use numbers only.");
        }
    }

    private void showDefaultAdminView() {
        mainContent.removeAll();
        JPanel welcome = new JPanel(new GridBagLayout());
        welcome.setOpaque(false);
        welcome.add(new JLabel("<html><div style='text-align: center;'><h2>Welcome, Admin</h2>" +
                "<p>Use the search bar or sidebar to manage the system.</p></div></html>"));
        mainContent.add(welcome, BorderLayout.CENTER);
        mainContent.repaint();
        mainContent.revalidate();
    }

    private JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBackground(new Color(34, 47, 62));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        return btn;
    }
}