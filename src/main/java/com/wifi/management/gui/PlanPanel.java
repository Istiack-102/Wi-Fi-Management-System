package com.wifi.management.gui;

import com.wifi.management.model.User;
import com.wifi.management.model.Plan;
import com.wifi.management.database_operation.PlanDAO;
import com.wifi.management.service.UserService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;

public class PlanPanel extends JPanel {
    private UserDashboard parent;
    private User user;
    private PlanDAO planDAO;
    private UserService userService;

    public PlanPanel(UserDashboard parent, User user) {
        this.parent = parent;
        this.user = user;
        this.planDAO = new PlanDAO();
        this.userService = new UserService();

        setLayout(new BorderLayout());
        setBackground(new Color(245, 246, 250));

        prepareGUI();
    }

    private void prepareGUI() {
        JLabel lblTitle = new JLabel("Available Internet Plans", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(Color.BLACK);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);

        String[] columns = {"Plan ID", "Plan Name", "Speed", "Price", "Action"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };

        List<Plan> allPlans = planDAO.getAllPlans();
        for (Plan plan : allPlans) {
            Object[] row = {
                    plan.getPlanId(),
                    plan.getPlanName(),
                    plan.getSpeedLimitMbps() + " Mbps",
                    plan.getMonthlyPrice() + " BDT",
                    "Buy Now"
            };
            model.addRow(row);
        }

        JTable table = new JTable(model);
        table.setRowHeight(50);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        table.setGridColor(new Color(200, 200, 200));

        // --- হেডার কালার: Black Background, White Text ---
        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(100, 45));
        header.setBackground(Color.BLACK);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.setReorderingAllowed(false);

        // রেন্ডারার এবং এডিটর সেট করা
        table.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox(), table, userService, user));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 50, 30, 50));
        add(scrollPane, BorderLayout.CENTER);

        JLabel lblFooter = new JLabel("Note: You must have an active connection to purchase.");
        lblFooter.setHorizontalAlignment(SwingConstants.CENTER);
        lblFooter.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblFooter.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
        add(lblFooter, BorderLayout.SOUTH);
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setBackground(Color.BLACK);
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText("Buy Now");
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private JTable table;
        private UserService userService;
        private User currentUser;

        public ButtonEditor(JCheckBox checkBox, JTable table, UserService userService, User user) {
            super(checkBox);
            this.table = table;
            this.userService = userService;
            this.currentUser = user;

            button = new JButton("Buy Now");
            button.setOpaque(true);
            button.setBackground(Color.BLACK);
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Segoe UI", Font.BOLD, 14));
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            int row = table.getSelectedRow();
            if (row != -1) {
                int planId = (int) table.getValueAt(row, 0);
                String planName = table.getValueAt(row, 1).toString();

                if (!userService.isVerifiedCustomer(currentUser.getUserId())) {
                    JOptionPane.showMessageDialog(button, "Verification failed! Contact admin for MAC setup.", "New Customer", JOptionPane.ERROR_MESSAGE);
                } else {
                    int confirm = JOptionPane.showConfirmDialog(button, "Subscribe to " + planName + "?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        JOptionPane.showMessageDialog(button, "Successfully Subscribed!");
                    }
                }
            }
            return "Buy Now";
        }
    }
}