package com.wifi.management.gui;

import com.wifi.management.model.User;
import com.wifi.management.database_operation.UserDAO;
import javax.swing.*;
import java.awt.*;

public class UserDashboard extends JFrame {
    private User currentUser;
    private JPanel contentPanel;
    private UserDAO userDAO;

    public UserDashboard(User loggedInUser) {
        this.userDAO = new UserDAO();
        // ডাটাবেস থেকে ইউজারের লেটেস্ট তথ্য এবং অ্যাড্রেস/ফোন সংগ্রহ করা
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

        // --- SIDEBAR ---
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(44, 62, 80));
        sidebar.setPreferredSize(new Dimension(250, 750));
        sidebar.setLayout(new GridLayout(10, 1, 0, 5));

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
        sidebar.add(new JLabel(""));
        sidebar.add(btnLogout);
        add(sidebar, BorderLayout.WEST);

        // --- HEADER ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(900, 75));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(224, 224, 224)));

        JLabel lblUser = new JLabel("  Welcome, " + currentUser.getUsername() + " (#" + currentUser.getUserId() + ")");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 18));
        header.add(lblUser, BorderLayout.WEST);

        String phone = (currentUser.getPhone() != null) ? currentUser.getPhone() : "No Phone";
        String address = (currentUser.getAddress() != null) ? currentUser.getAddress() : "No Address";
        JLabel lblContact = new JLabel("📞 " + phone + "  |  📍 " + address + "   ");
        lblContact.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblContact.setForeground(new Color(127, 140, 141));
        header.add(lblContact, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // --- CONTENT PANEL ---
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(241, 242, 246));
        add(contentPanel, BorderLayout.CENTER);

        // ডিফল্টভাবে প্রোফাইল দেখাবে
        showPanel(new CustomerPanel(currentUser));

        // --- ACTION LISTENERS ---
        btnProfile.addActionListener(e -> showPanel(new CustomerPanel(currentUser)));

        // এখানে 'this' পাস করা জরুরি যাতে PlanPanel ড্যাশবোর্ডকে চিনতে পারে
        btnPlans.addActionListener(e -> showPanel(new PlanPanel(this, currentUser)));

        // পেমেন্ট প্যানেল লোড করা
        btnPayment.addActionListener(e -> showPanel(new PaymentPanel(this, currentUser)));

        btnStatus.addActionListener(e -> showPanel(new SubscriptionPanel(currentUser)));

        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                this.dispose();
                new LoginFrame().setVisible(true);
            }
        });
    }

    public void showPanel(JPanel panel) {
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.repaint();
        contentPanel.revalidate();
    }

    private JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(44, 62, 80));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMargin(new Insets(0, 25, 0, 0));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { btn.setBackground(new Color(52, 73, 94)); }
            public void mouseExited(java.awt.event.MouseEvent evt) { btn.setBackground(new Color(44, 62, 80)); }
        });

        return btn;
    }
}