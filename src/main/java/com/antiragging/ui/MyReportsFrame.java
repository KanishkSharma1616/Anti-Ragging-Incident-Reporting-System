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

/**
 * View My Reports Frame for Logged-in Students.
 * Author: Kanishk Sharma (PIET25CR023)
 */
public class MyReportsFrame extends JFrame {

    private final IncidentService incidentService = new IncidentService();

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JComboBox<String> comboFilterStatus;

    public MyReportsFrame() {
        initUI();
        try {
            loadMyReports();
        } catch (Exception ignored) {}
    }

    private void initUI() {
        setTitle("Anti-Ragging System - My Incident Reports");
        setSize(800, 520);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(26, 37, 48));
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel lblTitle = new JLabel("MY SUBMITTED INCIDENT REPORTS");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Track investigation progress for reports logged under ID: " + SessionManager.getCurrentStudentId());
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(189, 195, 199));

        headerPanel.add(lblTitle, BorderLayout.NORTH);
        headerPanel.add(lblSub, BorderLayout.SOUTH);

        // Filter / Search Toolbar Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        filterPanel.setBackground(new Color(236, 240, 241));

        filterPanel.add(new JLabel("Search:"));
        txtSearch = new JTextField(12);
        filterPanel.add(txtSearch);

        filterPanel.add(new JLabel("Status:"));
        comboFilterStatus = new JComboBox<>(new String[]{"All", "Pending", "Under Investigation", "Resolved", "Rejected", "Closed"});
        filterPanel.add(comboFilterStatus);

        JButton btnSearch = new JButton("Search");
        JButton btnRefresh = new JButton("Refresh");

        filterPanel.add(btnSearch);
        filterPanel.add(btnRefresh);

        // Table Panel
        String[] columns = {"Incident ID", "Date", "Location", "Ragging Type", "Status", "Submission Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(24);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(236, 240, 241));

        JScrollPane scrollPane = new JScrollPane(table);

        // Bottom Action Panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        bottomPanel.setBackground(new Color(245, 247, 250));

        JButton btnViewDetails = new JButton("View Full Report Details");
        btnViewDetails.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnViewDetails.setBackground(new Color(41, 128, 185));
        btnViewDetails.setForeground(Color.WHITE);

        JButton btnClose = new JButton("Close");

        bottomPanel.add(btnViewDetails);
        bottomPanel.add(btnClose);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.add(filterPanel, BorderLayout.NORTH);
        centerContainer.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(centerContainer, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Event Handlers
        btnSearch.addActionListener(e -> applyFilter());
        comboFilterStatus.addActionListener(e -> applyFilter());
        btnRefresh.addActionListener(e -> {
            txtSearch.setText("");
            comboFilterStatus.setSelectedIndex(0);
            loadMyReports();
        });

        btnViewDetails.addActionListener(e -> openDetails());
        btnClose.addActionListener(e -> this.dispose());
    }

    private void loadMyReports() {
        try {
            tableModel.setRowCount(0);
            int studentUserId = SessionManager.getCurrentUserId();
            List<Incident> list = incidentService.getStudentIncidents(studentUserId);
            for (Incident inc : list) {
                tableModel.addRow(new Object[]{
                        inc.getIncidentId(),
                        inc.getIncidentDate(),
                        inc.getIncidentLocation(),
                        inc.getRaggingType(),
                        inc.getStatus(),
                        inc.getSubmissionDate()
                });
            }
        } catch (SQLException e) {
            System.err.println("Database fetch notice: " + e.getMessage());
        }
    }

    private void applyFilter() {
        try {
            String query = txtSearch.getText().trim();
            String status = (String) comboFilterStatus.getSelectedItem();

            tableModel.setRowCount(0);
            int studentUserId = SessionManager.getCurrentUserId();
            List<Incident> list = incidentService.getStudentIncidents(studentUserId);

            for (Incident inc : list) {
                boolean matchesQuery = query.isEmpty() ||
                        String.valueOf(inc.getIncidentId()).contains(query) ||
                        inc.getIncidentLocation().toLowerCase().contains(query.toLowerCase()) ||
                        inc.getRaggingType().toLowerCase().contains(query.toLowerCase());

                boolean matchesStatus = "All".equalsIgnoreCase(status) || inc.getStatus().equalsIgnoreCase(status);

                if (matchesQuery && matchesStatus) {
                    tableModel.addRow(new Object[]{
                            inc.getIncidentId(),
                            inc.getIncidentDate(),
                            inc.getIncidentLocation(),
                            inc.getRaggingType(),
                            inc.getStatus(),
                            inc.getSubmissionDate()
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openDetails() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an incident report row from the table.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int incidentId = (int) tableModel.getValueAt(selectedRow, 0);
        new IncidentDetailsFrame(incidentId).setVisible(true);
    }
}
