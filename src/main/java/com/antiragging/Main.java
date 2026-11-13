package com.antiragging;

import com.antiragging.db.DatabaseConfig;
import com.antiragging.db.DatabaseInitializer;
import com.antiragging.ui.LoginFrame;

import javax.swing.*;
import java.awt.*;

/**
 * Main Entry Point for Anti-Ragging Incident Reporting System.
 * Submitted by: Kanishk Sharma (PIET25CR023, 3CSD)
 */
public class Main {

    public static void main(String[] args) {
        // Set System Look and Feel for native UI rendering
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Launch in Swing Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            System.out.println("=======================================================================");
            System.out.println("   ANTI-RAGGING INCIDENT REPORTING SYSTEM - NSP MINOR PROJECT");
            System.out.println("   Developer: Kanishk Sharma | Student ID: PIET25CR023 | Branch: 3CSD");
            System.out.println("=======================================================================");

            // Step 1: Automatic Database Initialization
            boolean dbSuccess = DatabaseInitializer.initializeDatabase();

            if (!dbSuccess) {
                String msg = "Notice: MySQL Server is not currently reachable on port " + DatabaseConfig.getDbPort() + ".\n\n" +
                        "1. If MySQL is installed, make sure MySQL Server service is started.\n" +
                        "2. If your local MySQL has a root password, set it in 'src/main/resources/database.properties'.\n\n" +
                        "Would you like to launch the application in Preview / Demo GUI Mode anyway?";
                int opt = JOptionPane.showConfirmDialog(
                        null,
                        msg,
                        "MySQL Server Offline - Launch Options",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE
                );
                if (opt != JOptionPane.YES_OPTION) {
                    System.exit(0);
                    return;
                }
            }

            // Step 2: Launch Login Window Frame
            System.out.println("Database successfully initialized. Launching GUI Login Screen...");
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
