package com.antiragging.ui;

import com.antiragging.model.Incident;
import com.antiragging.service.IncidentService;
import com.antiragging.util.SessionManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;

/**
 * Admin Update Incident Investigation & Status Frame.
 * Author: Kanishk Sharma (PIET25CR023)
 */
public class UpdateIncidentFrame extends JFrame {

    private final ManageIncidentsFrame parentConsole;
    private final int incidentId;
    private final IncidentService incidentService = new IncidentService();

    private Incident currentIncident;

    private JComboBox<String> comboStatus;
    private JTextField txtAssignedOfficer;
    private JTextArea txtNotes;
    private JTextArea txtActionTaken;
    private JTextArea txtResolutionDetails;

    public UpdateIncidentFrame(ManageIncidentsFrame parent, int incidentId) {
        this.parentConsole = parent;
        this.incidentId = incidentId;
        initUI();
        try {
            loadIncidentData();
        } catch (Exception ignored) {}
    }

    private void initUI() {
        setTitle("Update Incident Status & Investigation - #" + incidentId);
        setSize(650, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Header Panel
        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        headerPanel.setBackground(new Color(26, 37, 48));
        headerPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblTitle = new JLabel("UPDATE INCIDENT #" + incidentId, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Modify investigation details, log official updates, and record actions taken", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(189, 195, 199));

        headerPanel.add(lblTitle);
        headerPanel.add(lblSub);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(20, 25, 20, 25));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // Status
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        formPanel.add(new JLabel("Incident Status:*"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        comboStatus = new JComboBox<>(new String[]{
                "Pending",
                "Under Investigation",
                "Resolved",
                "Rejected",
                "Closed"
        });
        formPanel.add(comboStatus, gbc);
        row++;

        // Assigned Officer
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Assigned Officer / Faculty:"), gbc);
        gbc.gridx = 1;
        txtAssignedOfficer = new JTextField();
        formPanel.add(txtAssignedOfficer, gbc);
        row++;

        // Investigation Notes
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Investigation Notes:"), gbc);
        gbc.gridx = 1;
        txtNotes = new JTextArea(3, 20);
        txtNotes.setLineWrap(true);
        txtNotes.setWrapStyleWord(true);
        formPanel.add(new JScrollPane(txtNotes), gbc);
        row++;

        // Action Taken
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Action Taken:"), gbc);
        gbc.gridx = 1;
        txtActionTaken = new JTextArea(3, 20);
        txtActionTaken.setLineWrap(true);
        txtActionTaken.setWrapStyleWord(true);
        formPanel.add(new JScrollPane(txtActionTaken), gbc);
        row++;

        // Resolution Details
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Resolution Details:"), gbc);
        gbc.gridx = 1;
        txtResolutionDetails = new JTextArea(3, 20);
        txtResolutionDetails.setLineWrap(true);
        txtResolutionDetails.setWrapStyleWord(true);
        formPanel.add(new JScrollPane(txtResolutionDetails), gbc);
        row++;

        JScrollPane scrollPane = new JScrollPane(formPanel);

        // Buttons Panel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        btnPanel.setBackground(new Color(236, 240, 241));

        JButton btnSave = new JButton("Save Updates");
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSave.setBackground(new Color(39, 174, 96));
        btnSave.setForeground(Color.WHITE);

        JButton btnCancel = new JButton("Cancel");

        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        add(mainPanel);

        btnSave.addActionListener(e -> handleSave());
        btnCancel.addActionListener(e -> this.dispose());
    }

    private void loadIncidentData() {
        try {
            currentIncident = incidentService.getIncidentById(incidentId);
            if (currentIncident != null) {
                comboStatus.setSelectedItem(currentIncident.getStatus());
                txtAssignedOfficer.setText(currentIncident.getAssignedOfficer() != null ? currentIncident.getAssignedOfficer() : "");
                txtNotes.setText(currentIncident.getInvestigationNotes() != null ? currentIncident.getInvestigationNotes() : "");
                txtActionTaken.setText(currentIncident.getActionTaken() != null ? currentIncident.getActionTaken() : "");
                txtResolutionDetails.setText(currentIncident.getResolutionDetails() != null ? currentIncident.getResolutionDetails() : "");
            } else {
                JOptionPane.showMessageDialog(this, "Incident not found in database.", "Error", JOptionPane.ERROR_MESSAGE);
                this.dispose();
            }
        } catch (SQLException e) {
            System.err.println("Database fetch notice: " + e.getMessage());
        }
    }

    private void handleSave() {
        if (currentIncident == null) return;

        String newStatus = (String) comboStatus.getSelectedItem();
        String oldStatus = currentIncident.getStatus();
        String officer = txtAssignedOfficer.getText().trim();
        String notes = txtNotes.getText().trim();
        String action = txtActionTaken.getText().trim();
        String resolution = txtResolutionDetails.getText().trim();

        try {
            int updatedByUserId = SessionManager.getCurrentUserId();
            boolean success = incidentService.updateIncidentStatus(
                    incidentId,
                    updatedByUserId,
                    oldStatus,
                    newStatus,
                    officer,
                    notes,
                    action,
                    resolution
            );

            if (success) {
                JOptionPane.showMessageDialog(this, "Incident #" + incidentId + " updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                if (parentConsole != null) {
                    parentConsole.loadAllIncidents();
                }
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update incident.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving update to MySQL: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
