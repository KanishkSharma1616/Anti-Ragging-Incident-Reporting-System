package com.antiragging.ui;

import com.antiragging.service.IncidentService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;
import java.util.Map;

/**
 * Statistics Analytics & Summary Frame.
 * Author: Kanishk Sharma (PIET25CR023)
 */
public class StatisticsFrame extends JFrame {

    private final IncidentService incidentService = new IncidentService();

    private JLabel lblTotal;
    private JLabel lblPending;
    private JLabel lblInvestigating;
    private JLabel lblResolved;
    private JLabel lblRejected;
    private JLabel lblClosed;

    public StatisticsFrame() {
        initUI();
        try {
            loadStats();
        } catch (Exception ignored) {}
    }

    private void initUI() {
        setTitle("Anti-Ragging System - Incident Statistics & Analytics");
        setSize(550, 420);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Header Panel
        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        headerPanel.setBackground(new Color(26, 37, 48));
        headerPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblTitle = new JLabel("INCIDENT STATISTICAL REPORT", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Live aggregate database metrics for NSP Minor Project Evaluation", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(189, 195, 199));

        headerPanel.add(lblTitle);
        headerPanel.add(lblSub);

        // Stats Grid Panel
        JPanel gridPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        gridPanel.setBackground(Color.WHITE);
        gridPanel.setBorder(new EmptyBorder(25, 25, 25, 25));

        lblTotal = new JLabel("0", SwingConstants.CENTER);
        lblPending = new JLabel("0", SwingConstants.CENTER);
        lblInvestigating = new JLabel("0", SwingConstants.CENTER);
        lblResolved = new JLabel("0", SwingConstants.CENTER);
        lblRejected = new JLabel("0", SwingConstants.CENTER);
        lblClosed = new JLabel("0", SwingConstants.CENTER);

        gridPanel.add(createBox("Total Reports", lblTotal, new Color(52, 152, 219)));
        gridPanel.add(createBox("Pending Action", lblPending, new Color(241, 196, 15)));
        gridPanel.add(createBox("Under Investigation", lblInvestigating, new Color(230, 126, 34)));
        gridPanel.add(createBox("Resolved Cases", lblResolved, new Color(46, 204, 113)));
        gridPanel.add(createBox("Rejected Reports", lblRejected, new Color(231, 76, 60)));
        gridPanel.add(createBox("Closed Cases", lblClosed, new Color(149, 165, 166)));

        // Footer / Button Panel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        btnPanel.setBackground(new Color(236, 240, 241));

        JButton btnRefresh = new JButton("Refresh Statistics");
        JButton btnClose = new JButton("Close");

        btnPanel.add(btnRefresh);
        btnPanel.add(btnClose);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(gridPanel, BorderLayout.CENTER);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        add(mainPanel);

        btnRefresh.addActionListener(e -> loadStats());
        btnClose.addActionListener(e -> this.dispose());
    }

    private JPanel createBox(String title, JLabel lblValue, Color c) {
        JPanel box = new JPanel(new BorderLayout(5, 5));
        box.setBackground(new Color(245, 247, 250));
        box.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, c),
                new EmptyBorder(10, 10, 10, 10)
        ));

        JLabel lblT = new JLabel(title);
        lblT.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblT.setForeground(Color.DARK_GRAY);

        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblValue.setForeground(c);

        box.add(lblT, BorderLayout.NORTH);
        box.add(lblValue, BorderLayout.CENTER);
        return box;
    }

    private void loadStats() {
        try {
            Map<String, Integer> stats = incidentService.getStatistics();
            lblTotal.setText(String.valueOf(stats.getOrDefault("Total", 0)));
            lblPending.setText(String.valueOf(stats.getOrDefault("Pending", 0)));
            lblInvestigating.setText(String.valueOf(stats.getOrDefault("Under Investigation", 0)));
            lblResolved.setText(String.valueOf(stats.getOrDefault("Resolved", 0)));
            lblRejected.setText(String.valueOf(stats.getOrDefault("Rejected", 0)));
            lblClosed.setText(String.valueOf(stats.getOrDefault("Closed", 0)));
        } catch (SQLException e) {
            System.err.println("Database fetch notice: " + e.getMessage());
        }
    }
}
