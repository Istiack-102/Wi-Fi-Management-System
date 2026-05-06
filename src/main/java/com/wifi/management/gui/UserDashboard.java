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

    // NEW UI COMPONENTS
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

        // ================= NEW BUTTON =================
        btnRequestConnection = createSidebarButton("📡 Request Connection");
        sidebar.add(btnRequestConnection);

        sidebar.add(new JLabel(""));
        sidebar.add(btnLogout);

        add(sidebar, BorderLayout.WEST);

        // ================= HEADER =================
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(900, 75));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(224, 224, 224)));

        JLabel lblUser = new JLabel("  Welcome, " + currentUser.getUsername() + " (#" + currentUser.getUserId() + ")");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 18));
        header.add(lblUser, BorderLayout.WEST);

        add(header, BorderLayout.NORTH);

        // ================= CONTENT =================
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(241, 242, 246));
        add(contentPanel, BorderLayout.CENTER);

        showPanel(new CustomerPanel(currentUser));

        // ================= STATUS LABEL =================
        lblRequestStatus = new JLabel("Request Status: Not Requested");
        lblRequestStatus.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // ================= ACTIONS =================
        btnProfile.addActionListener(e -> showPanel(new CustomerPanel(currentUser)));

        btnPlans.addActionListener(e -> showPanel(new PlanPanel(this, currentUser)));

        btnPayment.addActionListener(e -> showPanel(new PaymentPanel(this, currentUser)));

        btnStatus.addActionListener(e -> showPanel(new SubscriptionPanel(currentUser)));

        // ================= REQUEST CONNECTION LOGIC =================
        btnRequestConnection.addActionListener(e -> {

            String status = requestService.getStatus(currentUser.getUserId());

            if (status == null) {

                // Example planId (তুমি UI থেকে select করাবে later)
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

        // ================= LOGOUT =================
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to logout?",
                    "Logout",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                this.dispose();
                new LoginFrame().setVisible(true);
            }
        });
    }

    // ================= PANEL SWITCH =================
    public void showPanel(JPanel panel) {
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.repaint();
        contentPanel.revalidate();
    }

    // ================= BUTTON STYLE =================
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