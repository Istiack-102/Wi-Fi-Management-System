package com.wifi.management.gui;

import com.wifi.management.model.User;
import com.wifi.management.database_operation.UserDAO;
import com.wifi.management.service.ConnectionRequestService;
import com.wifi.management.service.SubscriptionService;

import javax.swing.*;
import java.awt.*;

public class UserDashboard extends JFrame {

    private User currentUser;
    private JPanel contentPanel;
    private UserDAO userDAO;
    private ConnectionRequestService requestService;
    private SubscriptionService subscriptionService;
    private JLabel lblPlanName;
    private JLabel lblStatus;
    private JButton btnRequestConnection;
    private JLabel lblRequestStatus;

    public UserDashboard(User loggedInUser) {
        this.userDAO = new UserDAO();
        this.requestService = new ConnectionRequestService();
        this.subscriptionService = new SubscriptionService();

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
        sidebar.setLayout(new GridLayout(12, 1, 0, 5));

        JLabel lblBrand = new JLabel("  WIFI MANAGER", SwingConstants.LEFT);
        lblBrand.setForeground(new Color(52, 152, 219));
        lblBrand.setFont(new Font("Segoe UI", Font.BOLD, 22));
        sidebar.add(lblBrand);

        JButton btnProfile = createSidebarButton("👤 My Profile");
        JButton btnPlans = createSidebarButton("🌐 Internet Plans");
        JButton btnPayment = createSidebarButton("💳 Make Payment");
        JButton btnStatus = createSidebarButton("📊 Subscription Status");
        JButton btnActivityLog = createSidebarButton("📜 My Activity Log");

        JButton btnLogout = createSidebarButton("🚪 Logout");
        btnLogout.setBackground(new Color(192, 57, 43));

        sidebar.add(btnProfile);
        sidebar.add(btnPlans);
        sidebar.add(btnPayment);
        sidebar.add(btnStatus);
        sidebar.add(btnActivityLog);

        btnRequestConnection = createSidebarButton("📡 Request Connection");
        sidebar.add(btnRequestConnection);

        sidebar.add(Box.createGlue());
        sidebar.add(btnLogout);

        add(sidebar, BorderLayout.WEST);

        // ================= HEADER =================
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(900, 85));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(224, 224, 224)));

        JLabel lblUser = new JLabel("  Welcome, " + currentUser.getUsername() + " (#" + currentUser.getUserId() + ")");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 18));
        header.add(lblUser, BorderLayout.WEST);

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

        // ================= CONTENT PANEL =================
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(241, 242, 246));
        add(contentPanel, BorderLayout.CENTER);

        showPanel(new CustomerPanel(currentUser));

        lblRequestStatus = new JLabel("Request Status: Not Requested");
        lblRequestStatus.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // ================= ACTIONS & EVENT LISTENERS =================
        btnProfile.addActionListener(e -> showPanel(new CustomerPanel(currentUser)));

        // 🔥 আপডেট: ইউজার এখন কোনো বাধা ছাড়াই সব ইন্টারনেট প্ল্যান দেখতে পারবেন
        btnPlans.addActionListener(e -> {
            showPanel(new PlanPanel(this, currentUser));
        });

        btnPayment.addActionListener(e -> {
            // 🛑 প্রোটেকশন: সরাসরি ম্যানুয়াল পেমেন্ট ট্যাবে যেতে চাইলে ব্লক করবে
            if (subscriptionService.hasUserPurchasedThisMonth(currentUser.getUserId())) {
                showMonthlyLimitWarning();
                return;
            }
            showPanel(new PaymentPanel(this, currentUser, 0, 0.0));
        });

        btnStatus.addActionListener(e -> showPanel(new SubscriptionPanel(currentUser)));

        btnActivityLog.addActionListener(e -> {
            showPanel(new HistoryLogPanel(currentUser.getUserId(), false));
        });

        btnRequestConnection.addActionListener(e -> {
            if (subscriptionService.hasUserPurchasedThisMonth(currentUser.getUserId())) {
                showMonthlyLimitWarning();
                return;
            }

            String status = requestService.getStatus(currentUser.getUserId());
            if (status == null) {
                int planId = 1;
                boolean result = requestService.requestConnection(currentUser.getUserId(), planId);
                if (result) {
                    lblRequestStatus.setText("Request Status: Pending");
                    JOptionPane.showMessageDialog(this, "Connection Requested Successfully! 🎉", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            } else if (status.equals("pending")) {
                JOptionPane.showMessageDialog(this, "Your previous request is already Pending! ⏳", "Status Notice", JOptionPane.WARNING_MESSAGE);
            } else if (status.equals("accepted")) {
                JOptionPane.showMessageDialog(this, "Your connection is already Approved & Active! ✅", "Status Notice", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Your previous request was rejected. You can apply again.");
            }
        });

        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                this.dispose();
                new LoginFrame().setVisible(true);
            }
        });

        displaySubscriptionStatus(this.currentUser);
    }

    // 🛑 ১ মাসে ১টি মাত্র প্ল্যান কেনার ওয়ার্নিং পপ-আপ মেসেজ
    private void showMonthlyLimitWarning() {
        JOptionPane.showMessageDialog(this,
                "🛑 Access Denied!\n" +
                        "You have already purchased an internet package this month.\n" +
                        "According to system rules, multiple plan swaps are restricted within the same month.",
                "Purchase Blocked",
                JOptionPane.ERROR_MESSAGE);
    }

    public void showPanel(JPanel panel) {
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.repaint();
        contentPanel.revalidate();
    }

    // 🔥 ইন্টারনেট প্ল্যান প্যানেলের "Buy" বাটনে ক্লিক করলে এই মেথডটি কল হয়
    public void loadPaymentPanel(int planId, double amount) {
        // 🛑 সিকিউরিটি চেক: প্ল্যান দেখার পর "Buy" বাটনে ক্লিক করলেই এখানে Access Denied দেখাবে
        if (subscriptionService.hasUserPurchasedThisMonth(currentUser.getUserId())) {
            showMonthlyLimitWarning();
            return;
        }
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
                statusColor = new Color(46, 204, 113);
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
                if(btn.getBackground() != new Color(192, 57, 43)) {
                    btn.setBackground(new Color(52, 73, 94));
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if(btn.getBackground() != new Color(192, 57, 43)) {
                    btn.setBackground(new Color(44, 62, 80));
                }
            }
        });

        return btn;
    }
}