package com.antiragging.ui;

import com.antiragging.service.LoginService;
import com.antiragging.util.SessionManager;
import com.antiragging.util.ValidationUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Change Password Frame for Users.
 * Author: Kanishk Sharma (PIET25CR023)
 */
public class ChangePasswordFrame extends JFrame {

    private JPasswordField txtCurrentPass;
    private JPasswordField txtNewPass;
    private JPasswordField txtConfirmPass;

    private final LoginService loginService = new LoginService();

    public ChangePasswordFrame() {
        initUI();
    }

    private void initUI() {
        setTitle("Anti-Ragging System - Change Password");
        setSize(420, 320);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Header Panel
        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        headerPanel.setBackground(new Color(26, 37, 48));
        headerPanel.setBorder(new EmptyBorder(12, 12, 12, 12));

        JLabel lblTitle = new JLabel("CHANGE PASSWORD", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("User: " + SessionManager.getCurrentUsername(), SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(189, 195, 199));

        headerPanel.add(lblTitle);
        headerPanel.add(lblSub);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Current Password
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.4;
        formPanel.add(new JLabel("Current Password:*"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.6;
        txtCurrentPass = new JPasswordField(15);
        formPanel.add(txtCurrentPass, gbc);

        // New Password
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.4;
        formPanel.add(new JLabel("New Password:*"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.6;
        txtNewPass = new JPasswordField(15);
        formPanel.add(txtNewPass, gbc);

        // Confirm Password
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.4;
        formPanel.add(new JLabel("Confirm Password:*"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.6;
        txtConfirmPass = new JPasswordField(15);
        formPanel.add(txtConfirmPass, gbc);

        // Buttons Panel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnPanel.setBackground(new Color(236, 240, 241));

        JButton btnUpdate = new JButton("Update Password");
        btnUpdate.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnUpdate.setBackground(new Color(41, 128, 185));
        btnUpdate.setForeground(Color.WHITE);

        JButton btnCancel = new JButton("Cancel");

        btnPanel.add(btnUpdate);
        btnPanel.add(btnCancel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        add(mainPanel);

        btnUpdate.addActionListener(e -> handlePasswordChange());
        btnCancel.addActionListener(e -> this.dispose());
    }

    private void handlePasswordChange() {
        String currentPass = new String(txtCurrentPass.getPassword()).trim();
        String newPass = new String(txtNewPass.getPassword()).trim();
        String confirmPass = new String(txtConfirmPass.getPassword()).trim();

        if (ValidationUtil.isEmpty(currentPass) || ValidationUtil.isEmpty(newPass) || ValidationUtil.isEmpty(confirmPass)) {
            JOptionPane.showMessageDialog(this, "Please fill in all password fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (newPass.length() < 4) {
            JOptionPane.showMessageDialog(this, "New password must be at least 4 characters long.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!newPass.equals(confirmPass)) {
            JOptionPane.showMessageDialog(this, "New password and Confirm password do not match.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            boolean success = loginService.changePassword(currentPass, newPass);
            if (success) {
                JOptionPane.showMessageDialog(this, "Password updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Current password is incorrect.", "Authentication Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating password in database: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
