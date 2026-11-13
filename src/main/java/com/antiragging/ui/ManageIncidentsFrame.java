package com.antiragging.ui;

import com.antiragging.model.Incident;
import com.antiragging.service.IncidentService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Admin Incident Management Console Frame with Full CRUD.
 * Author: Kanishk Sharma (PIET25CR023)
 */
public class ManageIncidentsFrame extends JFrame {

    private final AdminDashboard parentDashboard;
    private final IncidentService incidentService = new IncidentService();

    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField txtSearch;
    private JComboBox<String> comboFilterStatus;
    private JComboBox<String> comboFilterType;

    public ManageIncidentsFrame(AdminDashboard parent) {
        this.parentDashboard = parent;
        initUI();
        try {
            loadAllIncidents();
        } catch (Exception ignored) {}
    }

    private void initUI() {
        setTitle("Anti-Ragging System - Manage Incidents (Admin Console)");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(26, 37, 48));
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel lblTitle = new JLabel("INCIDENT MANAGEMENT CONSOLE");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Search, inspect, update status, assign officers, and manage complete incident lifecycle");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(189, 195, 199));

        headerPanel.add(lblTitle, BorderLayout.NORTH);
        headerPanel.add(lblSub, BorderLayout.SOUTH);

        // Filter / Search Toolbar Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filterPanel.setBackground(new Color(236, 240, 241));

        filterPanel.add(new JLabel("Search:"));
        txtSearch = new JTextField(12);
        filterPanel.add(txtSearch);

        filterPanel.add(new JLabel("Status:"));
        comboFilterStatus = new JComboBox<>(new String[]{"All", "Pending", "Under Investigation", "Resolved", "Rejected", "Closed"});
        filterPanel.add(comboFilterStatus);

        filterPanel.add(new JLabel("Type:"));
        comboFilterType = new JComboBox<>(new String[]{
                "All",
                "Verbal Harassment",
                "Physical Harassment",
                "Psychological Harassment",
                "Cyber/Online Harassment",
                "Threat/Intimidation",
                "Other"
        });
        filterPanel.add(comboFilterType);

        JButton btnSearch = new JButton("Search");
        btnSearch.setBackground(new Color(41, 128, 185));
        btnSearch.setForeground(Color.WHITE);

        JButton btnRefresh = new JButton("Refresh");

        filterPanel.add(btnSearch);
        filterPanel.add(btnRefresh);

        // Table Panel
        String[] columns = {
                "Incident ID", "Student ID", "Student Name", "Department",
                "Incident Date", "Location", "Ragging Type", "Status", "Assigned Officer", "Submission Date"
        };

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

        // Action Buttons Grid Panel at bottom
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        actionPanel.setBackground(new Color(245, 247, 250));

        JButton btnView = new JButton("View Details");
        btnView.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnView.setBackground(new Color(41, 128, 185));
        btnView.setForeground(Color.WHITE);

        JButton btnUpdate = new JButton("Update Investigation");
        btnUpdate.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnUpdate.setBackground(new Color(39, 174, 96));
        btnUpdate.setForeground(Color.WHITE);

        JButton btnDelete = new JButton("Delete Incident");
        btnDelete.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnDelete.setBackground(new Color(231, 76, 60));
        btnDelete.setForeground(Color.WHITE);

        JButton btnClose = new JButton("Close Console");

        actionPanel.add(btnView);
        actionPanel.add(btnUpdate);
        actionPanel.add(btnDelete);
        actionPanel.add(btnClose);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.add(filterPanel, BorderLayout.NORTH);
        centerContainer.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(centerContainer, BorderLayout.CENTER);
        mainPanel.add(actionPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Handlers
        btnSearch.addActionListener(e -> handleSearch());
        comboFilterStatus.addActionListener(e -> handleSearch());
        comboFilterType.addActionListener(e -> handleSearch());

        btnRefresh.addActionListener(e -> {
            txtSearch.setText("");
            comboFilterStatus.setSelectedIndex(0);
            comboFilterType.setSelectedIndex(0);
            loadAllIncidents();
        });

        btnView.addActionListener(e -> openViewDetails());
        btnUpdate.addActionListener(e -> openUpdateFrame());
        btnDelete.addActionListener(e -> handleDelete());
        btnClose.addActionListener(e -> this.dispose());
    }

    public void loadAllIncidents() {
        try {
            tableModel.setRowCount(0);
            List<Incident> list = incidentService.getAllIncidents();
            for (Incident inc : list) {
                tableModel.addRow(new Object[]{
                        inc.getIncidentId(),
                        inc.getStudentId(),
                        inc.getStudentName(),
                        inc.getDepartment(),
                        inc.getIncidentDate(),
                        inc.getIncidentLocation(),
                        inc.getRaggingType(),
                        inc.getStatus(),
                        inc.getAssignedOfficer() != null ? inc.getAssignedOfficer() : "Unassigned",
                        inc.getSubmissionDate()
                });
            }
            if (parentDashboard != null) {
                parentDashboard.loadDashboardData();
            }
        } catch (SQLException e) {
            System.err.println("Database fetch notice: " + e.getMessage());
        }
    }

    private void handleSearch() {
        try {
            String query = txtSearch.getText().trim();
            String status = (String) comboFilterStatus.getSelectedItem();
            String type = (String) comboFilterType.getSelectedItem();

            tableModel.setRowCount(0);
            List<Incident> list = incidentService.searchIncidents(query, status, type);
            for (Incident inc : list) {
                tableModel.addRow(new Object[]{
                        inc.getIncidentId(),
                        inc.getStudentId(),
                        inc.getStudentName(),
                        inc.getDepartment(),
                        inc.getIncidentDate(),
                        inc.getIncidentLocation(),
                        inc.getRaggingType(),
                        inc.getStatus(),
                        inc.getAssignedOfficer() != null ? inc.getAssignedOfficer() : "Unassigned",
                        inc.getSubmissionDate()
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error executing search: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openViewDetails() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an incident from the table to view details.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int incidentId = (int) tableModel.getValueAt(selectedRow, 0);
        new IncidentDetailsFrame(incidentId).setVisible(true);
    }

    private void openUpdateFrame() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an incident from the table to update.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int incidentId = (int) tableModel.getValueAt(selectedRow, 0);
        new UpdateIncidentFrame(this, incidentId).setVisible(true);
    }

    private void handleDelete() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an incident from the table to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int incidentId = (int) tableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete incident record #" + incidentId + "?\nThis action cannot be undone.",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean success = incidentService.deleteIncident(incidentId);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Incident record #" + incidentId + " deleted successfully.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
                    loadAllIncidents();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete incident record.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting record from MySQL: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
