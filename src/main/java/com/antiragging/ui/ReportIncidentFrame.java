package com.antiragging.ui;

import com.antiragging.model.Incident;
import com.antiragging.model.User;
import com.antiragging.service.IncidentService;
import com.antiragging.util.SessionManager;
import com.antiragging.util.ValidationUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;

/**
 * Incident Reporting Form Frame for Students.
 * Author: Kanishk Sharma (PIET25CR023)
 */
public class ReportIncidentFrame extends JFrame {

    private final StudentDashboard parentDashboard;
    private final IncidentService incidentService = new IncidentService();

    private JTextField txtStudentName;
    private JTextField txtStudentId;
    private JTextField txtDepartment;
    private JTextField txtSemester;
    private JTextField txtContactNumber;
    private JTextField txtEmail;
    private JTextField txtIncidentDate;
    private JTextField txtIncidentTime;
    private JTextField txtIncidentLocation;

    private JComboBox<String> comboRaggingType;
    private JTextArea txtDescription;
    private JTextArea txtPersonsInvolved;
    private JTextArea txtWitnessDetails;
    private JTextArea txtEvidenceDescription;

    private JRadioButton rdoHelpYes;
    private JRadioButton rdoHelpNo;

    public ReportIncidentFrame(StudentDashboard parent) {
        this.parentDashboard = parent;
        initUI();
        prefillStudentData();
    }

    private void initUI() {
        setTitle("Report New Ragging Incident");
        setSize(750, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Header Panel
        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        headerPanel.setBackground(new Color(26, 37, 48));
        headerPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblTitle = new JLabel("ANTI-RAGGING INCIDENT REPORT FORM", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Strict Confidentiality Maintained | Fill all required details accurately", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(189, 195, 199));

        headerPanel.add(lblTitle);
        headerPanel.add(lblSub);

        // Scrollable Form Content
        JPanel formContainer = new JPanel(new GridBagLayout());
        formContainer.setBackground(Color.WHITE);
        formContainer.setBorder(new EmptyBorder(20, 25, 20, 25));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // Student Info Section
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        JLabel lblSec1 = new JLabel("1. Student Details");
        lblSec1.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSec1.setForeground(new Color(41, 128, 185));
        formContainer.add(lblSec1, gbc);

        gbc.gridwidth = 1;

        // Name & ID
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.2;
        formContainer.add(new JLabel("Student Name:*"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.8;
        txtStudentName = new JTextField();
        formContainer.add(txtStudentName, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        formContainer.add(new JLabel("Student ID / Roll No:*"), gbc);
        gbc.gridx = 1;
        txtStudentId = new JTextField();
        formContainer.add(txtStudentId, gbc);
        row++;

        // Department & Semester
        gbc.gridx = 0; gbc.gridy = row;
        formContainer.add(new JLabel("Department:*"), gbc);
        gbc.gridx = 1;
        txtDepartment = new JTextField();
        formContainer.add(txtDepartment, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        formContainer.add(new JLabel("Semester:*"), gbc);
        gbc.gridx = 1;
        txtSemester = new JTextField();
        formContainer.add(txtSemester, gbc);
        row++;

        // Contact & Email
        gbc.gridx = 0; gbc.gridy = row;
        formContainer.add(new JLabel("Contact Number (10 digit):*"), gbc);
        gbc.gridx = 1;
        txtContactNumber = new JTextField();
        formContainer.add(txtContactNumber, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        formContainer.add(new JLabel("Email Address:*"), gbc);
        gbc.gridx = 1;
        txtEmail = new JTextField();
        formContainer.add(txtEmail, gbc);
        row++;

        // Incident Info Section
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        JLabel lblSec2 = new JLabel("2. Incident Information");
        lblSec2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSec2.setForeground(new Color(41, 128, 185));
        formContainer.add(lblSec2, gbc);

        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = row;
        formContainer.add(new JLabel("Incident Date (YYYY-MM-DD):*"), gbc);
        gbc.gridx = 1;
        txtIncidentDate = new JTextField(LocalDate.now().toString());
        formContainer.add(txtIncidentDate, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        formContainer.add(new JLabel("Incident Time (e.g. 03:30 PM):*"), gbc);
        gbc.gridx = 1;
        txtIncidentTime = new JTextField("02:00 PM");
        formContainer.add(txtIncidentTime, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        formContainer.add(new JLabel("Incident Location:*"), gbc);
        gbc.gridx = 1;
        txtIncidentLocation = new JTextField();
        formContainer.add(txtIncidentLocation, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        formContainer.add(new JLabel("Category of Ragging:*"), gbc);
        gbc.gridx = 1;
        String[] types = {
                "Verbal Harassment",
                "Physical Harassment",
                "Psychological Harassment",
                "Cyber/Online Harassment",
                "Threat/Intimidation",
                "Other"
        };
        comboRaggingType = new JComboBox<>(types);
        formContainer.add(comboRaggingType, gbc);
        row++;

        // Description JTextArea
        gbc.gridx = 0; gbc.gridy = row;
        formContainer.add(new JLabel("Detailed Description:*"), gbc);
        gbc.gridx = 1;
        txtDescription = new JTextArea(4, 20);
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        formContainer.add(new JScrollPane(txtDescription), gbc);
        row++;

        // Persons Involved
        gbc.gridx = 0; gbc.gridy = row;
        formContainer.add(new JLabel("Persons Involved / Accused:"), gbc);
        gbc.gridx = 1;
        txtPersonsInvolved = new JTextArea(2, 20);
        txtPersonsInvolved.setLineWrap(true);
        txtPersonsInvolved.setWrapStyleWord(true);
        formContainer.add(new JScrollPane(txtPersonsInvolved), gbc);
        row++;

        // Witness Details
        gbc.gridx = 0; gbc.gridy = row;
        formContainer.add(new JLabel("Witness Details:"), gbc);
        gbc.gridx = 1;
        txtWitnessDetails = new JTextArea(2, 20);
        txtWitnessDetails.setLineWrap(true);
        txtWitnessDetails.setWrapStyleWord(true);
        formContainer.add(new JScrollPane(txtWitnessDetails), gbc);
        row++;

        // Evidence Description
        gbc.gridx = 0; gbc.gridy = row;
        formContainer.add(new JLabel("Evidence Description:"), gbc);
        gbc.gridx = 1;
        txtEvidenceDescription = new JTextArea(2, 20);
        txtEvidenceDescription.setLineWrap(true);
        txtEvidenceDescription.setWrapStyleWord(true);
        formContainer.add(new JScrollPane(txtEvidenceDescription), gbc);
        row++;

        // Immediate Help Required (Radio Buttons)
        gbc.gridx = 0; gbc.gridy = row;
        formContainer.add(new JLabel("Immediate Help Required?"), gbc);
        gbc.gridx = 1;
        rdoHelpYes = new JRadioButton("Yes");
        rdoHelpNo = new JRadioButton("No", true);
        ButtonGroup bgHelp = new ButtonGroup();
        bgHelp.add(rdoHelpYes);
        bgHelp.add(rdoHelpNo);
        JPanel rdoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rdoPanel.setBackground(Color.WHITE);
        rdoPanel.add(rdoHelpYes);
        rdoPanel.add(rdoHelpNo);
        formContainer.add(rdoPanel, gbc);
        row++;

        JScrollPane scrollPane = new JScrollPane(formContainer);
        scrollPane.getVerticalScrollBar().setUnitIncrement(12);

        // Buttons Panel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        btnPanel.setBackground(new Color(236, 240, 241));

        JButton btnSubmit = new JButton("Submit Report");
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSubmit.setBackground(new Color(39, 174, 96));
        btnSubmit.setForeground(Color.WHITE);

        JButton btnClear = new JButton("Clear Form");
        JButton btnBack = new JButton("Back to Dashboard");

        btnPanel.add(btnSubmit);
        btnPanel.add(btnClear);
        btnPanel.add(btnBack);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Event listeners
        btnSubmit.addActionListener(e -> handleSubmit());
        btnClear.addActionListener(e -> clearForm());
        btnBack.addActionListener(e -> this.dispose());
    }

    private void prefillStudentData() {
        User u = SessionManager.getCurrentUser();
        if (u != null) {
            txtStudentName.setText(u.getFullName());
            txtStudentId.setText(u.getStudentId());
            txtDepartment.setText(u.getDepartment());
            txtSemester.setText(u.getSemester());
            txtContactNumber.setText(u.getPhone());
            txtEmail.setText(u.getEmail());
        }
    }

    private void handleSubmit() {
        // Validation
        if (ValidationUtil.isEmpty(txtStudentName.getText())) {
            JOptionPane.showMessageDialog(this, "Please enter Student Name.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!ValidationUtil.isValidStudentId(txtStudentId.getText())) {
            JOptionPane.showMessageDialog(this, "Please enter a valid Student ID.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!ValidationUtil.isValidPhone(txtContactNumber.getText())) {
            JOptionPane.showMessageDialog(this, "Please enter a valid 10-digit contact number.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!ValidationUtil.isValidEmail(txtEmail.getText())) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!ValidationUtil.isValidDate(txtIncidentDate.getText())) {
            JOptionPane.showMessageDialog(this, "Please enter a valid date in YYYY-MM-DD format.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (ValidationUtil.isEmpty(txtIncidentLocation.getText())) {
            JOptionPane.showMessageDialog(this, "Please enter the incident location.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (ValidationUtil.isEmpty(txtDescription.getText())) {
            JOptionPane.showMessageDialog(this, "Please enter the detailed incident description.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Incident incident = new Incident();
            incident.setStudentUserId(SessionManager.getCurrentUserId());
            incident.setStudentName(txtStudentName.getText().trim());
            incident.setStudentId(txtStudentId.getText().trim());
            incident.setDepartment(txtDepartment.getText().trim());
            incident.setSemester(txtSemester.getText().trim());
            incident.setContactNumber(txtContactNumber.getText().trim());
            incident.setEmail(txtEmail.getText().trim());
            incident.setIncidentDate(Date.valueOf(txtIncidentDate.getText().trim()));
            incident.setIncidentTime(txtIncidentTime.getText().trim());
            incident.setIncidentLocation(txtIncidentLocation.getText().trim());
            incident.setRaggingType((String) comboRaggingType.getSelectedItem());
            incident.setDescription(txtDescription.getText().trim());
            incident.setPersonsInvolved(txtPersonsInvolved.getText().trim());
            incident.setWitnessDetails(txtWitnessDetails.getText().trim());
            incident.setEvidenceDescription(txtEvidenceDescription.getText().trim());
            incident.setImmediateHelpRequired(rdoHelpYes.isSelected() ? "Yes" : "No");

            boolean success = incidentService.createIncident(incident);

            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Incident Report Submitted Successfully!\nIncident Reference ID: #" + incident.getIncidentId() +
                        "\nOur Anti-Ragging Committee will review this report urgently.",
                        "Report Submitted", JOptionPane.INFORMATION_MESSAGE);

                if (parentDashboard != null) {
                    parentDashboard.loadStudentStatistics();
                }
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to submit incident report. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error inserting record into database: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void clearForm() {
        txtIncidentLocation.setText("");
        txtDescription.setText("");
        txtPersonsInvolved.setText("");
        txtWitnessDetails.setText("");
        txtEvidenceDescription.setText("");
        comboRaggingType.setSelectedIndex(0);
        rdoHelpNo.setSelected(true);
    }
}
