package com.antiragging.ui;

import com.antiragging.model.Incident;
import com.antiragging.service.IncidentService;
import com.antiragging.util.SessionManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Student Dashboard GUI Frame.
 * Author: Kanishk Sharma (PIET25CR023)
 */
public class StudentDashboard extends JFrame {

    private final IncidentService incidentService = new IncidentService();

    private JLabel lblTotalCount;
    private JLabel lblPendingCount;
    private JLabel lblInvestigatingCount;
    private JLabel lblResolvedCount;

    public StudentDashboard() {
        initUI();
        try {
            loadStudentStatistics();
        } catch (Exception ignored) {}
    }

    private void initUI() {
        setTitle("Anti-Ragging System - Student Dashboard");
        setSize(850, 580);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Menu Bar
        setJMenuBar(createMenuBar());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 247, 250));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(26, 37, 48));
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel lblTitle = new JLabel("STUDENT PORTAL - ANTI-RAGGING CELL");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblWelcome = new JLabel("Welcome, " + SessionManager.getCurrentUser().getFullName() +
                " (" + SessionManager.getCurrentStudentId() + " - " + SessionManager.getCurrentUser().getDepartment() + ")");
        lblWelcome.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblWelcome.setForeground(new Color(189, 195, 199));

        headerPanel.add(lblTitle, BorderLayout.NORTH);
        headerPanel.add(lblWelcome, BorderLayout.SOUTH);

        // Toolbar
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.setBackground(new Color(236, 240, 241));

        JButton btnNavReport = new JButton("Report Incident");
        JButton btnNavMyReports = new JButton("My Reports");
        JButton btnNavPass = new JButton("Change Password");
        JButton btnNavLogout = new JButton("Logout");

        toolbar.add(btnNavReport);
        toolbar.addSeparator();
        toolbar.add(btnNavMyReports);
        toolbar.addSeparator();
        toolbar.add(btnNavPass);
        toolbar.addSeparator();
        toolbar.add(btnNavLogout);

        // Center Dashboard Panel
        JPanel centerPanel = new JPanel(new BorderLayout(15, 15));
        centerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        centerPanel.setOpaque(false);

        // Statistics Cards Panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        statsPanel.setOpaque(false);

        lblTotalCount = new JLabel("0", SwingConstants.CENTER);
        lblPendingCount = new JLabel("0", SwingConstants.CENTER);
        lblInvestigatingCount = new JLabel("0", SwingConstants.CENTER);
        lblResolvedCount = new JLabel("0", SwingConstants.CENTER);

        statsPanel.add(createCard("Total Reports", lblTotalCount, new Color(52, 152, 219)));
        statsPanel.add(createCard("Pending", lblPendingCount, new Color(241, 196, 15)));
        statsPanel.add(createCard("Under Investigation", lblInvestigatingCount, new Color(230, 126, 34)));
        statsPanel.add(createCard("Resolved", lblResolvedCount, new Color(46, 204, 113)));

        // Action Buttons Grid
        JPanel actionsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        actionsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), " Quick Actions ", 0, 0, new Font("Segoe UI", Font.BOLD, 14)));
        actionsPanel.setOpaque(false);

        JButton btnReportBig = new JButton("Report New Incident");
        btnReportBig.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnReportBig.setBackground(new Color(41, 128, 185));
        btnReportBig.setForeground(Color.WHITE);

        JButton btnMyReportsBig = new JButton("View My Reports");
        btnMyReportsBig.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnMyReportsBig.setBackground(new Color(39, 174, 96));
        btnMyReportsBig.setForeground(Color.WHITE);

        JButton btnChangePassBig = new JButton("Change Password");
        btnChangePassBig.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JButton btnLogoutBig = new JButton("Logout Account");
        btnLogoutBig.setFont(new Font("Segoe UI", Font.BOLD, 14));

        actionsPanel.add(btnReportBig);
        actionsPanel.add(btnMyReportsBig);
        actionsPanel.add(btnChangePassBig);
        actionsPanel.add(btnLogoutBig);

        centerPanel.add(statsPanel, BorderLayout.NORTH);
        centerPanel.add(actionsPanel, BorderLayout.CENTER);

        // Footer Panel
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(new Color(44, 62, 80));
        JLabel lblFooter = new JLabel("Minor Project NSP | Kanishk Sharma | PIET25CR023");
        lblFooter.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblFooter.setForeground(Color.WHITE);
        footerPanel.add(lblFooter);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(toolbar, BorderLayout.AFTER_LAST_LINE); // below header
        
        // Wrap header & toolbar in a single top container
        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(headerPanel, BorderLayout.NORTH);
        topContainer.add(toolbar, BorderLayout.SOUTH);

        mainPanel.add(topContainer, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Button Event Handlers
        btnNavReport.addActionListener(e -> openReportFrame());
        btnReportBig.addActionListener(e -> openReportFrame());

        btnNavMyReports.addActionListener(e -> openMyReportsFrame());
        btnMyReportsBig.addActionListener(e -> openMyReportsFrame());

        btnNavPass.addActionListener(e -> new ChangePasswordFrame().setVisible(true));
        btnChangePassBig.addActionListener(e -> new ChangePasswordFrame().setVisible(true));

        btnNavLogout.addActionListener(e -> handleLogout());
        btnLogoutBig.addActionListener(e -> handleLogout());
    }

    private JPanel createCard(String title, JLabel lblValue, Color cardColor) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, cardColor));

        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTitle.setForeground(Color.GRAY);

        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblValue.setForeground(cardColor);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);
        return card;
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuFile = new JMenu("File");
        JMenuItem itemLogout = new JMenuItem("Logout");
        JMenuItem itemExit = new JMenuItem("Exit");
        itemLogout.addActionListener(e -> handleLogout());
        itemExit.addActionListener(e -> System.exit(0));
        menuFile.add(itemLogout);
        menuFile.add(itemExit);

        JMenu menuIncident = new JMenu("Incident");
        JMenuItem itemReport = new JMenuItem("Report Incident");
        JMenuItem itemMyReports = new JMenuItem("My Reports");
        itemReport.addActionListener(e -> openReportFrame());
        itemMyReports.addActionListener(e -> openMyReportsFrame());
        menuIncident.add(itemReport);
        menuIncident.add(itemMyReports);

        JMenu menuAccount = new JMenu("Account");
        JMenuItem itemPass = new JMenuItem("Change Password");
        itemPass.addActionListener(e -> new ChangePasswordFrame().setVisible(true));
        menuAccount.add(itemPass);

        menuBar.add(menuFile);
        menuBar.add(menuIncident);
        menuBar.add(menuAccount);
        return menuBar;
    }

    private void openReportFrame() {
        new ReportIncidentFrame(this).setVisible(true);
    }

    private void openMyReportsFrame() {
        new MyReportsFrame().setVisible(true);
    }

    public void loadStudentStatistics() {
        try {
            int studentUserId = SessionManager.getCurrentUserId();
            List<Incident> studentList = incidentService.getStudentIncidents(studentUserId);
            int total = studentList.size();
            int pending = 0, investigating = 0, resolved = 0;

            for (Incident inc : studentList) {
                if ("Pending".equalsIgnoreCase(inc.getStatus())) pending++;
                else if ("Under Investigation".equalsIgnoreCase(inc.getStatus())) investigating++;
                else if ("Resolved".equalsIgnoreCase(inc.getStatus())) resolved++;
            }

            lblTotalCount.setText(String.valueOf(total));
            lblPendingCount.setText(String.valueOf(pending));
            lblInvestigatingCount.setText(String.valueOf(investigating));
            lblResolvedCount.setText(String.valueOf(resolved));

        } catch (SQLException e) {
            e.printStackTrace();
        }
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
