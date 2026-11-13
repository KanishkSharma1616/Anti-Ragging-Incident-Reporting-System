package com.antiragging.ui;

import com.antiragging.model.Incident;
import com.antiragging.service.IncidentService;
import com.antiragging.util.SessionManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Admin Dashboard GUI Frame.
 * Author: Kanishk Sharma (PIET25CR023)
 */
public class AdminDashboard extends JFrame {

    private final IncidentService incidentService = new IncidentService();

    private JLabel lblTotal;
    private JLabel lblPending;
    private JLabel lblInvestigating;
    private JLabel lblResolved;
    private JLabel lblRejected;
    private JLabel lblClosed;

    private JTable recentTable;
    private DefaultTableModel tableModel;

    public AdminDashboard() {
        initUI();
        try {
            loadDashboardData();
        } catch (Exception ignored) {}
    }

    private void initUI() {
        setTitle("Anti-Ragging System - Admin Control Panel");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setJMenuBar(createAdminMenuBar());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 247, 250));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(26, 37, 48));
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel lblTitle = new JLabel("ADMINISTRATION DASHBOARD - ANTI-RAGGING CELL");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(Color.BLACK);

        JLabel lblSub = new JLabel("Logged in as: " + SessionManager.getCurrentUser().getFullName() + " (Administrator)");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(189, 195, 199));

        headerPanel.add(lblTitle, BorderLayout.NORTH);
        headerPanel.add(lblSub, BorderLayout.SOUTH);

        // Toolbar
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.setBackground(new Color(236, 240, 241));

        JButton btnManage = new JButton("Manage Incidents");
        JButton btnStats = new JButton("Statistics");
        JButton btnPass = new JButton("Change Password");
        JButton btnLogout = new JButton("Logout");

        toolbar.add(btnManage);
        toolbar.addSeparator();
        toolbar.add(btnStats);
        toolbar.addSeparator();
        toolbar.add(btnPass);
        toolbar.addSeparator();
        toolbar.add(btnLogout);

        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(headerPanel, BorderLayout.NORTH);
        topContainer.add(toolbar, BorderLayout.SOUTH);

        // Center Content
        JPanel centerPanel = new JPanel(new BorderLayout(15, 15));
        centerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        centerPanel.setOpaque(false);

        // Cards Panel (6 counters)
        JPanel statsPanel = new JPanel(new GridLayout(1, 6, 10, 0));
        statsPanel.setOpaque(false);

        lblTotal = new JLabel("0", SwingConstants.CENTER);
        lblPending = new JLabel("0", SwingConstants.CENTER);
        lblInvestigating = new JLabel("0", SwingConstants.CENTER);
        lblResolved = new JLabel("0", SwingConstants.CENTER);
        lblRejected = new JLabel("0", SwingConstants.CENTER);
        lblClosed = new JLabel("0", SwingConstants.CENTER);

        statsPanel.add(createMiniCard("Total", lblTotal, new Color(52, 152, 219)));
        statsPanel.add(createMiniCard("Pending", lblPending, new Color(241, 196, 15)));
        statsPanel.add(createMiniCard("Investigating", lblInvestigating, new Color(230, 126, 34)));
        statsPanel.add(createMiniCard("Resolved", lblResolved, new Color(46, 204, 113)));
        statsPanel.add(createMiniCard("Rejected", lblRejected, new Color(231, 76, 60)));
        statsPanel.add(createMiniCard("Closed", lblClosed, new Color(149, 165, 166)));

        // Recent Incidents Table Panel
        JPanel tableContainer = new JPanel(new BorderLayout(10, 10));
        tableContainer.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), " Recent Incident Reports ", 0, 0, new Font("Segoe UI", Font.BOLD, 14)));
        tableContainer.setOpaque(false);

        String[] cols = {"Incident ID", "Student ID", "Student Name", "Department", "Ragging Type", "Status", "Submission Date"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        recentTable = new JTable(tableModel);
        recentTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        recentTable.setRowHeight(24);
        recentTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        recentTable.getTableHeader().setBackground(new Color(236, 240, 241));

        JScrollPane scrollPane = new JScrollPane(recentTable);
        tableContainer.add(scrollPane, BorderLayout.CENTER);

        // Bottom Action buttons inside table container
        JPanel actionBtnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        actionBtnPanel.setOpaque(false);

        JButton btnOpenManage = new JButton("Open Full Management Console");
        btnOpenManage.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnOpenManage.setBackground(new Color(41, 128, 185));
        btnOpenManage.setForeground(Color.BLACK);

        JButton btnRefresh = new JButton("Refresh Data");
        btnRefresh.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        actionBtnPanel.add(btnRefresh);
        actionBtnPanel.add(btnOpenManage);
        tableContainer.add(actionBtnPanel, BorderLayout.SOUTH);

        centerPanel.add(statsPanel, BorderLayout.NORTH);
        centerPanel.add(tableContainer, BorderLayout.CENTER);

        // Footer Panel
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(new Color(44, 62, 80));
        JLabel lblFooter = new JLabel("Minor Project NSP | Kanishk Sharma | PIET25CR023");
        lblFooter.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblFooter.setForeground(Color.BLACK);
        footerPanel.add(lblFooter);

        mainPanel.add(topContainer, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Event listeners
        btnManage.addActionListener(e -> openManageIncidents());
        btnOpenManage.addActionListener(e -> openManageIncidents());
        btnStats.addActionListener(e -> new StatisticsFrame().setVisible(true));
        btnPass.addActionListener(e -> new ChangePasswordFrame().setVisible(true));
        btnRefresh.addActionListener(e -> loadDashboardData());
        btnLogout.addActionListener(e -> handleLogout());
    }

    private JPanel createMiniCard(String title, JLabel lblValue, Color cardColor) {
        JPanel card = new JPanel(new BorderLayout(2, 2));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createMatteBorder(0, 4, 0, 0, cardColor));

        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblTitle.setForeground(Color.GRAY);

        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblValue.setForeground(cardColor);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);
        return card;
    }

    private JMenuBar createAdminMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuFile = new JMenu("File");
        JMenuItem itemLogout = new JMenuItem("Logout");
        JMenuItem itemExit = new JMenuItem("Exit");
        itemLogout.addActionListener(e -> handleLogout());
        itemExit.addActionListener(e -> System.exit(0));
        menuFile.add(itemLogout);
        menuFile.add(itemExit);

        JMenu menuIncident = new JMenu("Incident Management");
        JMenuItem itemManage = new JMenuItem("View & Manage Incidents");
        JMenuItem itemStats = new JMenuItem("Statistics Report");
        itemManage.addActionListener(e -> openManageIncidents());
        itemStats.addActionListener(e -> new StatisticsFrame().setVisible(true));
        menuIncident.add(itemManage);
        menuIncident.add(itemStats);

        JMenu menuAccount = new JMenu("Account");
        JMenuItem itemPass = new JMenuItem("Change Password");
        itemPass.addActionListener(e -> new ChangePasswordFrame().setVisible(true));
        menuAccount.add(itemPass);

        menuBar.add(menuFile);
        menuBar.add(menuIncident);
        menuBar.add(menuAccount);
        return menuBar;
    }

    public void loadDashboardData() {
        try {
            Map<String, Integer> stats = incidentService.getStatistics();
            lblTotal.setText(String.valueOf(stats.getOrDefault("Total", 0)));
            lblPending.setText(String.valueOf(stats.getOrDefault("Pending", 0)));
            lblInvestigating.setText(String.valueOf(stats.getOrDefault("Under Investigation", 0)));
            lblResolved.setText(String.valueOf(stats.getOrDefault("Resolved", 0)));
            lblRejected.setText(String.valueOf(stats.getOrDefault("Rejected", 0)));
            lblClosed.setText(String.valueOf(stats.getOrDefault("Closed", 0)));

            tableModel.setRowCount(0);
            List<Incident> incidents = incidentService.getAllIncidents();
            for (Incident inc : incidents) {
                tableModel.addRow(new Object[]{
                        inc.getIncidentId(),
                        inc.getStudentId(),
                        inc.getStudentName(),
                        inc.getDepartment(),
                        inc.getRaggingType(),
                        inc.getStatus(),
                        inc.getSubmissionDate()
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openManageIncidents() {
        new ManageIncidentsFrame(this).setVisible(true);
    }

    private void handleLogout() {
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            SessionManager.clearSession();
            this.dispose();
            new LoginFrame().setVisible(true);
        }
    }
}
