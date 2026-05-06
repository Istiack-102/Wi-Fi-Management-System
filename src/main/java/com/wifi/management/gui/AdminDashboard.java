package com.wifi.management.gui;

import com.wifi.management.model.User;
import com.wifi.management.model.Plan;
import com.wifi.management.model.ConnectionRequest;
import com.wifi.management.service.UserService;
import com.wifi.management.service.ConnectionRequestService;
import com.wifi.management.database_operation.PlanDAO;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AdminDashboard extends JFrame {

    private JPanel mainContent;
    private JTextField txtSearch;

    private UserService userService;
    private PlanDAO planDAO;
    private ConnectionRequestService requestService;

    private JTable requestTable;

    public AdminDashboard() {
        userService = new UserService();
        planDAO = new PlanDAO();
        requestService = new ConnectionRequestService();

        prepareGUI();
    }

    // ================= GUI =================
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
        sidebar.setLayout(new GridLayout(12, 1, 5, 5));

        JLabel lblAdmin = new JLabel("  ADMIN PANEL");
        lblAdmin.setForeground(Color.WHITE);
        lblAdmin.setFont(new Font("Arial", Font.BOLD, 16));
        sidebar.add(lblAdmin);

        JButton btnUsers = createSidebarButton("Manage Users");
        JButton btnPlans = createSidebarButton("Edit Plans");
        JButton btnRequests = createSidebarButton("Connection Requests");
        JButton btnLogout = createSidebarButton("Logout");

        btnLogout.setBackground(new Color(231, 76, 60));
        btnLogout.setForeground(Color.WHITE);

        sidebar.add(btnUsers);
        sidebar.add(btnPlans);
        sidebar.add(btnRequests);
        sidebar.add(new JLabel(""));
        sidebar.add(btnLogout);

        add(sidebar, BorderLayout.WEST);

        // ================= TOP BAR =================
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        txtSearch = new JTextField(20);
        JButton btnSearch = new JButton("Search");

        topBar.add(new JLabel("Search User: "));
        topBar.add(txtSearch);
        topBar.add(btnSearch);

        add(topBar, BorderLayout.NORTH);

        // ================= MAIN CONTENT =================
        mainContent = new JPanel(new BorderLayout());
        add(mainContent, BorderLayout.CENTER);

        showWelcome();

        // ================= ACTIONS =================
        btnUsers.addActionListener(e -> showWelcome());
        btnPlans.addActionListener(e -> showPlanEditor());
        btnRequests.addActionListener(e -> showConnectionRequests());

        btnSearch.addActionListener(e -> handleSearch());

        btnLogout.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
    }

    // ================= CONNECTION REQUEST PANEL =================
    private void showConnectionRequests() {

        mainContent.removeAll();

        List<ConnectionRequest> requests = requestService.getAllPendingRequests();

        String[] cols = {"Request ID", "User ID", "Plan ID", "Status"};

        if (requests == null || requests.isEmpty()) {
            mainContent.add(new JLabel("No Pending Requests", SwingConstants.CENTER));
            mainContent.revalidate();
            mainContent.repaint();
            return;
        }

        String[][] data = new String[requests.size()][4];

        for (int i = 0; i < requests.size(); i++) {

            ConnectionRequest r = requests.get(i);

            data[i][0] = String.valueOf(r.getRequestId());
            data[i][1] = String.valueOf(r.getUserId());
            data[i][2] = String.valueOf(r.getPlanId());
            data[i][3] = r.getStatus();
        }

        requestTable = new JTable(data, cols);
        requestTable.setRowHeight(25);

        JScrollPane scroll = new JScrollPane(requestTable);

        // ================= ACTION PANEL =================
        JPanel actionPanel = new JPanel();

        JButton btnAccept = new JButton("Accept");
        JButton btnReject = new JButton("Reject");

        actionPanel.add(btnAccept);
        actionPanel.add(btnReject);

        // ================= ACCEPT =================
        btnAccept.addActionListener(e -> {

            int row = requestTable.getSelectedRow();

            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a request");
                return;
            }

            int requestId = Integer.parseInt(requestTable.getValueAt(row, 0).toString());

            boolean ok = requestService.approveRequest(requestId);

            if (ok) {
                JOptionPane.showMessageDialog(this,
                        "Request Accepted & Subscription Created");

                showConnectionRequests();
            }
        });

        // ================= REJECT =================
        btnReject.addActionListener(e -> {

            int row = requestTable.getSelectedRow();

            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a request");
                return;
            }

            int requestId = Integer.parseInt(requestTable.getValueAt(row, 0).toString());

            boolean ok = requestService.rejectRequest(requestId);

            if (ok) {
                JOptionPane.showMessageDialog(this, "Request Rejected");

                showConnectionRequests();
            }
        });

        JPanel container = new JPanel(new BorderLayout());
        container.add(scroll, BorderLayout.CENTER);
        container.add(actionPanel, BorderLayout.SOUTH);

        mainContent.add(container);
        mainContent.revalidate();
        mainContent.repaint();
    }

    // ================= SEARCH =================
    private void handleSearch() {

        String keyword = txtSearch.getText().trim();

        List<User> users = userService.searchUsers(keyword);

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

        mainContent.removeAll();
        mainContent.add(new JScrollPane(table));
        mainContent.revalidate();
        mainContent.repaint();
    }

    // ================= PLAN EDITOR =================
    private void showPlanEditor() {

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

        mainContent.removeAll();
        mainContent.add(new JScrollPane(table));
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

    // ================= BUTTON STYLE =================
    private JButton createSidebarButton(String text) {

        JButton btn = new JButton(text);

        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setBackground(new Color(52, 73, 94));
        btn.setForeground(Color.WHITE);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);

        return btn;
    }
}