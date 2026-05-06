package com.wifi.management.gui;

import com.wifi.management.model.User;
import com.wifi.management.database_operation.UserDAO;
import com.wifi.management.service.ConnectionRequestService;

import javax.swing.*;
import java.awt.*;

public class UserDashboard extends JFrame {

    private User currentUser;
    private JPanel contentPanel;
    private UserDAO userDAO;
    private ConnectionRequestService requestService;
    private JLabel lblPlanName; // Already declared in your code
    private JLabel lblStatus;   // Already declared in your code
    private JButton btnRequestConnection;
    private JLabel lblRequestStatus;

    public UserDashboard(User loggedInUser) {
        this.userDAO = new UserDAO();
        this.requestService = new ConnectionRequestService();

        this.currentUser = userDAO.getUserFullProfile(loggedInUser.getUserId());

        if (this.currentUser == null) {
            this.currentUser = loggedInUser;
        }

        prepareGUI();
    }

    private void prepareGUI() {
        setTitle("WiFi Management - Customer Dashboard");
        setSize(1150, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ================= SIDEBAR =================
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(44, 62, 80));
        sidebar.setPreferredSize(new Dimension(250, 750));
        sidebar.setLayout(new GridLayout(11, 1, 0, 5));

        JLabel lblBrand = new JLabel("  WIFI MANAGER", SwingConstants.LEFT);
        lblBrand.setForeground(new Color(52, 152, 219));
        lblBrand.setFont(new Font("Segoe UI", Font.BOLD, 22));
        sidebar.add(lblBrand);

        JButton btnProfile = createSidebarButton("👤 My Profile");
        JButton btnPlans = createSidebarButton("🌐 Internet Plans");
        JButton btnPayment = createSidebarButton("💳 Make Payment");
        JButton btnStatus = createSidebarButton("📊 Subscription Status");
        JButton btnLogout = createSidebarButton("🚪 Logout");

        btnLogout.setBackground(new Color(192, 57, 43));

        sidebar.add(btnProfile);
        sidebar.add(btnPlans);
        sidebar.add(btnPayment);
        sidebar.add(btnStatus);

        btnRequestConnection = createSidebarButton("📡 Request Connection");
        sidebar.add(btnRequestConnection);

        sidebar.add(new JLabel(""));
        sidebar.add(btnLogout);

        add(sidebar, BorderLayout.WEST);

        // ================= HEADER (UPDATED TO SHOW STATUS) =================
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(900, 85)); // Height slightly increased
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(224, 224, 224)));

        // Left side: Welcome Message
        JLabel lblUser = new JLabel("  Welcome, " + currentUser.getUsername() + " (#" + currentUser.getUserId() + ")");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 18));
        header.add(lblUser, BorderLayout.WEST);

        // Right side: Subscription Status Display
        JPanel statusInfoPanel = new JPanel(new GridLayout(2, 1));
        statusInfoPanel.setBackground(Color.WHITE);
        statusInfoPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 20));

        lblPlanName = new JLabel("Current Plan: Loading...", SwingConstants.RIGHT);
        lblPlanName.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        lblStatus = new JLabel("Status: Unknown", SwingConstants.RIGHT);
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 14));

        statusInfoPanel.add(lblPlanName);
        statusInfoPanel.add(lblStatus);
        header.add(statusInfoPanel, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // ================= CONTENT =================
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(241, 242, 246));
        add(contentPanel, BorderLayout.CENTER);

        showPanel(new CustomerPanel(currentUser));

        lblRequestStatus = new JLabel("Request Status: Not Requested");
        lblRequestStatus.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // ================= ACTIONS =================
        btnProfile.addActionListener(e -> showPanel(new CustomerPanel(currentUser)));
        btnPlans.addActionListener(e -> showPanel(new PlanPanel(this, currentUser)));
        btnPayment.addActionListener(e -> showPanel(new PaymentPanel(this, currentUser, 0, 0.0)));
        btnStatus.addActionListener(e -> showPanel(new SubscriptionPanel(currentUser)));

        btnRequestConnection.addActionListener(e -> {
            String status = requestService.getStatus(currentUser.getUserId());
            if (status == null) {
                int planId = 1;
                boolean result = requestService.requestConnection(currentUser.getUserId(), planId);
                if (result) {
                    lblRequestStatus.setText("Request Status: Pending");
                    JOptionPane.showMessageDialog(this, "Connection Requested Successfully!");
                }
            } else if (status.equals("pending")) {
                JOptionPane.showMessageDialog(this, "Already Pending!");
            } else if (status.equals("accepted")) {
                JOptionPane.showMessageDialog(this, "Already Approved ✅");
            } else {
                JOptionPane.showMessageDialog(this, "Rejected. You can request again.");
            }
        });

        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                this.dispose();
                new LoginFrame().setVisible(true);
            }
        });

        // 🔥 INITIAL STATUS LOAD
        displaySubscriptionStatus(this.currentUser);
    }

    public void showPanel(JPanel panel) {
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.repaint();
        contentPanel.revalidate();
    }

    public void loadPaymentPanel(int planId, double amount) {
        PaymentPanel payment = new PaymentPanel(this, this.currentUser, planId, amount);
        showPanel(payment);
    }

    private void displaySubscriptionStatus(User currentUser) {
        String plan = (currentUser.getPlanName() != null) ? currentUser.getPlanName() : "No Active Plan";
        String status;
        Color statusColor;

        if (currentUser.getExpiryDate() == null) {
            status = "Inactive";
            statusColor = Color.GRAY;
        } else {
            java.util.Date today = new java.util.Date();
            if (currentUser.getExpiryDate().after(today)) {
                status = "Active (Expires: " + currentUser.getExpiryDate().toString() + ")";
                statusColor = new Color(46, 204, 113); // সবুজ
            } else {
                status = "Expired on " + currentUser.getExpiryDate().toString();
                statusColor = Color.RED;
            }
        }

        lblPlanName.setText("Current Plan: " + plan);
        lblStatus.setText("Status: " + status);
        lblStatus.setForeground(statusColor);
    }

    private JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(44, 62, 80));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMargin(new Insets(0, 25, 0, 0));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(52, 73, 94));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(44, 62, 80));
            }
        });

        return btn;
    }
}