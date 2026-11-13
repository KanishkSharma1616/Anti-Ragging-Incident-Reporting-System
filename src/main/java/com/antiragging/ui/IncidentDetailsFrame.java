package com.antiragging.ui;

import com.antiragging.model.Incident;
import com.antiragging.model.IncidentUpdate;
import com.antiragging.service.IncidentService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Comprehensive Incident Details View Frame with Audit Log History.
 * Author: Kanishk Sharma (PIET25CR023)
 */
public class IncidentDetailsFrame extends JFrame {

    private final int incidentId;
    private final IncidentService incidentService = new IncidentService();

    private JTextArea txtDetails;
    private DefaultTableModel historyTableModel;

    public IncidentDetailsFrame(int incidentId) {
        this.incidentId = incidentId;
        initUI();
        try {
            loadDetails();
        } catch (Exception ignored) {}
    }

    private void initUI() {
        setTitle("Anti-Ragging System - Detailed Case Report #" + incidentId);
        setSize(750, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Header Panel
        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        headerPanel.setBackground(new Color(26, 37, 48));
        headerPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblTitle = new JLabel("OFFICIAL INCIDENT DOSSIER #" + incidentId, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Anti-Ragging Committee Inspection & Case History Log", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(189, 195, 199));

        headerPanel.add(lblTitle);
        headerPanel.add(lblSub);

        // Tabbed Pane (Details + History)
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 12));

        // Tab 1: Detailed Text Dossier
        txtDetails = new JTextArea();
        txtDetails.setEditable(false);
        txtDetails.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtDetails.setMargin(new Insets(15, 15, 15, 15));

        JScrollPane scrollDetails = new JScrollPane(txtDetails);
        tabbedPane.addTab("Report Overview", scrollDetails);

        // Tab 2: Investigation Update History Table
        String[] cols = {"Update ID", "Updated By", "Old Status", "New Status", "Notes", "Action Taken", "Updated At"};
        historyTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable historyTable = new JTable(historyTableModel);
        historyTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        historyTable.setRowHeight(24);
        historyTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane scrollHistory = new JScrollPane(historyTable);
        tabbedPane.addTab("Investigation Audit Logs", scrollHistory);

        // Bottom Close Panel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        btnPanel.setBackground(new Color(236, 240, 241));

        JButton btnClose = new JButton("Close Window");
        btnPanel.add(btnClose);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        add(mainPanel);

        btnClose.addActionListener(e -> this.dispose());
    }

    private void loadDetails() {
        try {
            Incident inc = incidentService.getIncidentById(incidentId);
            if (inc == null) {
                txtDetails.setText("Incident record not found.");
                return;
            }

            StringBuilder sb = new StringBuilder();
            sb.append("=======================================================================\n");
            sb.append("                 ANTI-RAGGING INCIDENT DOSSIER                         \n");
            sb.append("=======================================================================\n\n");

            sb.append("[1. GENERAL INFORMATION]\n");
            sb.append("  Incident ID              : #").append(inc.getIncidentId()).append("\n");
            sb.append("  Status                   : ").append(inc.getStatus()).append("\n");
            sb.append("  Category / Ragging Type  : ").append(inc.getRaggingType()).append("\n");
            sb.append("  Submission Date          : ").append(inc.getSubmissionDate()).append("\n");
            sb.append("  Last Updated             : ").append(inc.getLastUpdated()).append("\n\n");

            sb.append("[2. COMPLAINANT / STUDENT DETAILS]\n");
            sb.append("  Student Name             : ").append(inc.getStudentName()).append("\n");
            sb.append("  Student ID / Roll No     : ").append(inc.getStudentId()).append("\n");
            sb.append("  Department               : ").append(inc.getDepartment()).append("\n");
            sb.append("  Semester                 : ").append(inc.getSemester()).append("\n");
            sb.append("  Contact Number           : ").append(inc.getContactNumber()).append("\n");
            sb.append("  Email Address            : ").append(inc.getEmail()).append("\n\n");

            sb.append("[3. INCIDENT EVENT DETAILS]\n");
            sb.append("  Date of Incident         : ").append(inc.getIncidentDate()).append("\n");
            sb.append("  Time of Incident         : ").append(inc.getIncidentTime()).append("\n");
            sb.append("  Location of Incident     : ").append(inc.getIncidentLocation()).append("\n");
            sb.append("  Immediate Help Required  : ").append(inc.getImmediateHelpRequired()).append("\n\n");

            sb.append("[4. DETAILED STATEMENT & EVIDENCE]\n");
            sb.append("  Description              : ").append(inc.getDescription()).append("\n\n");
            sb.append("  Persons Involved/Accused : ").append(inc.getPersonsInvolved() != null ? inc.getPersonsInvolved() : "None stated").append("\n\n");
            sb.append("  Witness Details          : ").append(inc.getWitnessDetails() != null ? inc.getWitnessDetails() : "None stated").append("\n\n");
            sb.append("  Evidence Description     : ").append(inc.getEvidenceDescription() != null ? inc.getEvidenceDescription() : "None provided").append("\n\n");

            sb.append("[5. INVESTIGATION & RESOLUTION STATUS]\n");
            sb.append("  Assigned Officer         : ").append(inc.getAssignedOfficer() != null ? inc.getAssignedOfficer() : "Unassigned").append("\n");
            sb.append("  Investigation Notes      : ").append(inc.getInvestigationNotes() != null ? inc.getInvestigationNotes() : "N/A").append("\n");
            sb.append("  Action Taken             : ").append(inc.getActionTaken() != null ? inc.getActionTaken() : "N/A").append("\n");
            sb.append("  Resolution Details       : ").append(inc.getResolutionDetails() != null ? inc.getResolutionDetails() : "N/A").append("\n");
            sb.append("=======================================================================\n");

            txtDetails.setText(sb.toString());
            txtDetails.setCaretPosition(0);

            // Load Audit logs
            historyTableModel.setRowCount(0);
            List<IncidentUpdate> updates = incidentService.getIncidentHistory(incidentId);
            for (IncidentUpdate up : updates) {
                historyTableModel.addRow(new Object[]{
                        up.getUpdateId(),
                        up.getUpdatedByName(),
                        up.getOldStatus(),
                        up.getNewStatus(),
                        up.getUpdateNotes(),
                        up.getActionTaken(),
                        up.getUpdatedAt()
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
            txtDetails.setText("Error loading incident dossier: " + e.getMessage());
        }
    }
}
