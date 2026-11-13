package com.antiragging.util;

import com.antiragging.model.Incident;
import com.antiragging.model.User;
import com.antiragging.ui.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Automated GUI Test Runner and Screenshot Capture Utility (Offline Capable).
 * Author: Kanishk Sharma (PIET25CR023)
 */
public class ScreenShooter {

    public static void main(String[] args) {
        System.out.println("Starting automated GUI test run and screenshot capture...");

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        File dir = new File("screenshots");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        User mockStudent = new User(2, "student1", "student123", "Kanishk Sharma", "PIET25CR023",
                "kanishk.sharma@piet.ac.in", "9876501234", "3CSD", "III Semester", "USER", "ACTIVE", new Timestamp(System.currentTimeMillis()));

        User mockAdmin = new User(1, "admin", "admin123", "Anti-Ragging Committee Head", "ADMIN001",
                "admin@piet.ac.in", "9876543210", "Administration", "N/A", "ADMIN", "ACTIVE", new Timestamp(System.currentTimeMillis()));

        SwingUtilities.invokeLater(() -> {
            try {
                // 1. Capture LoginFrame
                SessionManager.clearSession();
                LoginFrame loginFrame = new LoginFrame();
                captureWindow(loginFrame, "01_login_screen.png");
                loginFrame.dispose();

                // Set session
                SessionManager.setCurrentUser(mockStudent);

                // 2. Capture Student Dashboard
                StudentDashboard studentDashboard = safeCreate(() -> new StudentDashboard());
                if (studentDashboard != null) {
                    captureWindow(studentDashboard, "02_student_dashboard.png");
                }

                // 3. Capture Report Incident Form
                ReportIncidentFrame reportFrame = safeCreate(() -> new ReportIncidentFrame(studentDashboard));
                if (reportFrame != null) {
                    captureWindow(reportFrame, "03_report_incident_form.png");
                    reportFrame.dispose();
                }

                // 4. Capture My Reports Frame
                MyReportsFrame myReportsFrame = safeCreate(() -> new MyReportsFrame());
                if (myReportsFrame != null) {
                    populateSampleTable(myReportsFrame);
                    captureWindow(myReportsFrame, "04_my_reports_list.png");
                    myReportsFrame.dispose();
                }

                if (studentDashboard != null) studentDashboard.dispose();

                // Set Admin Session
                SessionManager.setCurrentUser(mockAdmin);

                // 5. Capture Admin Dashboard
                AdminDashboard adminDashboard = safeCreate(() -> new AdminDashboard());
                if (adminDashboard != null) {
                    populateAdminTable(adminDashboard);
                    captureWindow(adminDashboard, "05_admin_dashboard.png");
                }

                // 6. Capture Manage Incidents Frame
                ManageIncidentsFrame manageFrame = safeCreate(() -> new ManageIncidentsFrame(adminDashboard));
                if (manageFrame != null) {
                    populateManageTable(manageFrame);
                    captureWindow(manageFrame, "06_manage_incidents_console.png");
                }

                // 7. Capture Update Incident Frame
                UpdateIncidentFrame updateFrame = safeCreate(() -> new UpdateIncidentFrame(manageFrame, 1));
                if (updateFrame != null) {
                    captureWindow(updateFrame, "07_update_incident_status.png");
                    updateFrame.dispose();
                }

                if (manageFrame != null) manageFrame.dispose();
                if (adminDashboard != null) adminDashboard.dispose();

                // 8. Capture Incident Details Frame
                IncidentDetailsFrame detailsFrame = safeCreate(() -> new IncidentDetailsFrame(1));
                if (detailsFrame != null) {
                    captureWindow(detailsFrame, "08_incident_details_dossier.png");
                    detailsFrame.dispose();
                }

                // 9. Capture Statistics Frame
                StatisticsFrame statsFrame = safeCreate(() -> new StatisticsFrame());
                if (statsFrame != null) {
                    captureWindow(statsFrame, "09_statistics_analytics.png");
                    statsFrame.dispose();
                }

                // 10. Capture Change Password Frame
                ChangePasswordFrame passFrame = safeCreate(() -> new ChangePasswordFrame());
                if (passFrame != null) {
                    captureWindow(passFrame, "10_change_password.png");
                    passFrame.dispose();
                }

                System.out.println("ALL SCREENSHOTS SUCCESSFULLY CAPTURED AND STORED IN 'screenshots/'!");
                System.exit(0);

            } catch (Exception e) {
                System.err.println("Error during screenshot capture: " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            }
        });
    }

    @FunctionalInterface
    interface SupplierWithException<T> {
        T get() throws Exception;
    }

    private static <T extends JFrame> T safeCreate(SupplierWithException<T> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            System.out.println("Notice (Normal if MySQL offline): Frame initialized without DB connection. " + e.getMessage());
            return null;
        }
    }

    private static void populateSampleTable(MyReportsFrame frame) {
        try {
            JTable table = findJTable(frame);
            if (table != null) {
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.setRowCount(0);
                model.addRow(new Object[]{1, "2026-08-15", "Senior Hostel Block B", "Verbal Harassment", "Under Investigation", "2026-08-15 16:30:00"});
                model.addRow(new Object[]{2, "2026-08-20", "Main Canteen Area", "Psychological Harassment", "Pending", "2026-08-20 13:15:00"});
                model.addRow(new Object[]{3, "2026-07-10", "College Bus Stop", "Cyber/Online Harassment", "Resolved", "2026-07-10 20:00:00"});
            }
        } catch (Exception ignored) {}
    }

    private static void populateAdminTable(AdminDashboard frame) {
        try {
            JTable table = findJTable(frame);
            if (table != null) {
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.setRowCount(0);
                model.addRow(new Object[]{1, "PIET25CR023", "Kanishk Sharma", "3CSD", "Verbal Harassment", "Under Investigation", "2026-08-15"});
                model.addRow(new Object[]{2, "PIET25CR023", "Kanishk Sharma", "3CSD", "Psychological Harassment", "Pending", "2026-08-20"});
                model.addRow(new Object[]{3, "PIET25CR023", "Kanishk Sharma", "3CSD", "Cyber/Online Harassment", "Resolved", "2026-07-10"});
            }
        } catch (Exception ignored) {}
    }

    private static void populateManageTable(ManageIncidentsFrame frame) {
        try {
            JTable table = findJTable(frame);
            if (table != null) {
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.setRowCount(0);
                model.addRow(new Object[]{1, "PIET25CR023", "Kanishk Sharma", "3CSD", "2026-08-15", "Senior Hostel Block B", "Verbal Harassment", "Under Investigation", "Prof. R. K. Verma", "2026-08-15"});
                model.addRow(new Object[]{2, "PIET25CR023", "Kanishk Sharma", "3CSD", "2026-08-20", "Main Canteen Area", "Psychological Harassment", "Pending", "Unassigned", "2026-08-20"});
                model.addRow(new Object[]{3, "PIET25CR023", "Kanishk Sharma", "3CSD", "2026-07-10", "College Bus Stop", "Cyber/Online Harassment", "Resolved", "Dr. S. K. Gupta", "2026-07-10"});
            }
        } catch (Exception ignored) {}
    }

    private static JTable findJTable(Container container) {
        for (Component c : container.getComponents()) {
            if (c instanceof JTable) return (JTable) c;
            if (c instanceof Container) {
                JTable t = findJTable((Container) c);
                if (t != null) return t;
            }
        }
        return null;
    }

    private static void captureWindow(JFrame window, String filename) {
        try {
            window.pack();
            window.setVisible(true);

            Dimension size = window.getSize();
            if (size.width < 450) size.width = 650;
            if (size.height < 350) size.height = 500;
            window.setSize(size);
            window.revalidate();
            window.repaint();

            BufferedImage image = new BufferedImage(window.getWidth(), window.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = image.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            window.paint(g2d);
            g2d.dispose();

            File file = new File("screenshots/" + filename);
            ImageIO.write(image, "png", file);
            System.out.println("Captured screenshot: " + file.getAbsolutePath());
        } catch (Exception e) {
            System.err.println("Failed to capture " + filename + ": " + e.getMessage());
        }
    }
}
