package com.antiragging.ui;

import com.antiragging.model.User;
import com.antiragging.service.LoginService;
import com.antiragging.util.ValidationUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Modern Swing Login Frame with PreparedStatement Auth.
 * Author: Kanishk Sharma (PIET25CR023)
 */
public class LoginFrame extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private final LoginService loginService = new LoginService();

    public LoginFrame() {
        initUI();
    }

    private void initUI() {
        setTitle("Anti-Ragging Incident Reporting System - Login");
        setSize(480, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Header Panel
        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        headerPanel.setBackground(new Color(26, 37, 48));
        headerPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblTitle = new JLabel("ANTI-RAGGING SYSTEM", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Incident Reporting & Case Management Portal", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(189, 195, 199));

        headerPanel.add(lblTitle);
        headerPanel.add(lblSub);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(25, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        JLabel lblUser = new JLabel("Username:");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 13));
        formPanel.add(lblUser, gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        txtUsername = new JTextField(15);
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(txtUsername, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        JLabel lblPass = new JLabel("Password:");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 13));
        formPanel.add(lblPass, gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        txtPassword = new JPasswordField(15);
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(txtPassword, gbc);

        // Demo Tip Box
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        JPanel tipPanel = new JPanel(new GridLayout(2, 1));
        tipPanel.setBackground(new Color(245, 247, 250));
        tipPanel.setBorder(BorderFactory.createTitledBorder("Demo Credentials"));
        JLabel lblAdminDemo = new JLabel(" Admin: username = admin | password = admin123");
        lblAdminDemo.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        JLabel lblStudentDemo = new JLabel(" Student: username = student1 | password = student123");
        lblStudentDemo.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        tipPanel.add(lblAdminDemo);
        tipPanel.add(lblStudentDemo);
        formPanel.add(tipPanel, gbc);

        // Buttons Panel
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setBackground(Color.WHITE);

        JButton btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLogin.setBackground(new Color(0,0,0));
        btnLogin.setForeground(Color.BLACK);

        JButton btnClear = new JButton("Clear");
        btnClear.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JButton btnExit = new JButton("Exit");
        btnExit.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        btnPanel.add(btnLogin);
        btnPanel.add(btnClear);
        btnPanel.add(btnExit);
        formPanel.add(btnPanel, gbc);

        // Footer Panel
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(new Color(44, 62, 80));
        JLabel lblFooter = new JLabel("Minor Project NSP | Kanishk Sharma | PIET25CR023");
        lblFooter.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblFooter.setForeground(Color.WHITE);
        footerPanel.add(lblFooter);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Action Listeners
        btnLogin.addActionListener(this::handleLogin);
        btnClear.addActionListener(e -> {
            txtUsername.setText("");
            txtPassword.setText("");
        });
        btnExit.addActionListener(e -> System.exit(0));
    }

    private void handleLogin(ActionEvent e) {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (ValidationUtil.isEmpty(username) || ValidationUtil.isEmpty(password)) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Input Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            User user = loginService.login(username, password);
            if (user != null) {
                JOptionPane.showMessageDialog(this, "Login Successful! Welcome, " + user.getFullName() + ".", "Success", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
                if (user.isAdmin()) {
                    new AdminDashboard().setVisible(true);
                } else {
                    new StudentDashboard().setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.", "Authentication Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            if ("admin".equals(username) && "admin123".equals(password)) {
                User demoAdmin = new User(1, "admin", "admin123", "Anti-Ragging Committee Head", "ADMIN001", "admin@piet.ac.in", "9876543210", "Administration", "N/A", "ADMIN", "ACTIVE", null);
                com.antiragging.util.SessionManager.setCurrentUser(demoAdmin);
                JOptionPane.showMessageDialog(this, "Login Successful! Welcome, " + demoAdmin.getFullName() + " (Demo Mode).", "Success", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
                new AdminDashboard().setVisible(true);
            } else if ("student1".equals(username) && "student123".equals(password)) {
                User demoStudent = new User(2, "student1", "student123", "Kanishk Sharma", "PIET25CR023", "kanishk.sharma@piet.ac.in", "9876501234", "3CSD", "III Semester", "USER", "ACTIVE", null);
                com.antiragging.util.SessionManager.setCurrentUser(demoStudent);
                JOptionPane.showMessageDialog(this, "Login Successful! Welcome, " + demoStudent.getFullName() + " (Demo Mode).", "Success", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
                new StudentDashboard().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Database connection is offline and credentials did not match demo accounts.\nUse admin/admin123 or student1/student123.", "Authentication Notice", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}
