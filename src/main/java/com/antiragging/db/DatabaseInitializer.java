package com.antiragging.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Automatic Database Setup & Schema Initializer.
 * Author: Kanishk Sharma (PIET25CR023)
 */
public class DatabaseInitializer {

    public static boolean initializeDatabase() {
        System.out.println("Starting automatic database verification and setup...");

        // Step 1: Connect to MySQL Server and create database if missing
        try (Connection serverConn = DBConnection.getRawServerConnection();
             Statement stmt = serverConn.createStatement()) {
            
            String createDbSql = "CREATE DATABASE IF NOT EXISTS " + DatabaseConfig.getDbName() +
                    " CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;";
            stmt.executeUpdate(createDbSql);
            System.out.println("Database '" + DatabaseConfig.getDbName() + "' checked/created successfully.");

        } catch (SQLException e) {
            System.err.println("Failed to connect to MySQL Server: " + e.getMessage());
            return false;
        }

        // Step 2: Connect to anti_ragging_db and create tables
        try (Connection dbConn = DBConnection.getConnection();
             Statement stmt = dbConn.createStatement()) {

            // Create users table
            String createUsersTable = """
                CREATE TABLE IF NOT EXISTS users (
                    user_id INT AUTO_INCREMENT PRIMARY KEY,
                    username VARCHAR(50) NOT NULL UNIQUE,
                    password VARCHAR(100) NOT NULL,
                    full_name VARCHAR(100) NOT NULL,
                    student_id VARCHAR(30) UNIQUE,
                    email VARCHAR(100) NOT NULL,
                    phone VARCHAR(20) NOT NULL,
                    department VARCHAR(50),
                    semester VARCHAR(20),
                    role VARCHAR(20) NOT NULL DEFAULT 'USER',
                    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
            """;
            stmt.executeUpdate(createUsersTable);

            // Create incidents table
            String createIncidentsTable = """
                CREATE TABLE IF NOT EXISTS incidents (
                    incident_id INT AUTO_INCREMENT PRIMARY KEY,
                    student_user_id INT NOT NULL,
                    student_name VARCHAR(100) NOT NULL,
                    student_id VARCHAR(30) NOT NULL,
                    department VARCHAR(50) NOT NULL,
                    semester VARCHAR(20) NOT NULL,
                    contact_number VARCHAR(20) NOT NULL,
                    email VARCHAR(100) NOT NULL,
                    incident_date DATE NOT NULL,
                    incident_time VARCHAR(20) NOT NULL,
                    incident_location VARCHAR(150) NOT NULL,
                    ragging_type VARCHAR(50) NOT NULL,
                    description TEXT NOT NULL,
                    persons_involved TEXT,
                    witness_details TEXT,
                    evidence_description TEXT,
                    immediate_help_required VARCHAR(5) NOT NULL DEFAULT 'No',
                    status VARCHAR(30) NOT NULL DEFAULT 'Pending',
                    assigned_officer VARCHAR(100) DEFAULT 'Unassigned',
                    investigation_notes TEXT,
                    action_taken TEXT,
                    resolution_details TEXT,
                    submission_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    FOREIGN KEY (student_user_id) REFERENCES users(user_id) ON DELETE CASCADE
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
            """;
            stmt.executeUpdate(createIncidentsTable);

            // Create incident_updates table
            String createUpdatesTable = """
                CREATE TABLE IF NOT EXISTS incident_updates (
                    update_id INT AUTO_INCREMENT PRIMARY KEY,
                    incident_id INT NOT NULL,
                    updated_by INT NOT NULL,
                    old_status VARCHAR(30),
                    new_status VARCHAR(30) NOT NULL,
                    update_notes TEXT,
                    action_taken TEXT,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (incident_id) REFERENCES incidents(incident_id) ON DELETE CASCADE,
                    FOREIGN KEY (updated_by) REFERENCES users(user_id) ON DELETE CASCADE
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
            """;
            stmt.executeUpdate(createUpdatesTable);

            System.out.println("Tables checked/created successfully.");

            // Seed default users if missing
            seedDefaultUsers(stmt);

            // Seed default sample incidents if missing
            seedSampleIncidents(stmt);

            return true;

        } catch (SQLException e) {
            System.err.println("Database table initialization error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private static void seedDefaultUsers(Statement stmt) throws SQLException {
        // Check if admin exists
        ResultSet rsAdmin = stmt.executeQuery("SELECT COUNT(*) FROM users WHERE username = 'admin'");
        if (rsAdmin.next() && rsAdmin.getInt(1) == 0) {
            String insertAdmin = """
                INSERT INTO users (username, password, full_name, student_id, email, phone, department, semester, role, status)
                VALUES ('admin', 'admin123', 'Anti-Ragging Committee Head', 'ADMIN001', 'admin@piet.ac.in', '9876543210', 'Administration', 'N/A', 'ADMIN', 'ACTIVE');
            """;
            stmt.executeUpdate(insertAdmin);
            System.out.println("Default ADMIN account created: admin / admin123");
        }

        // Check if student1 exists
        ResultSet rsStudent = stmt.executeQuery("SELECT COUNT(*) FROM users WHERE username = 'student1'");
        if (rsStudent.next() && rsStudent.getInt(1) == 0) {
            String insertStudent = """
                INSERT INTO users (username, password, full_name, student_id, email, phone, department, semester, role, status)
                VALUES ('student1', 'student123', 'Kanishk Sharma', 'PIET25CR023', 'kanishk.sharma@piet.ac.in', '9876501234', '3CSD', 'III Semester', 'USER', 'ACTIVE');
            """;
            stmt.executeUpdate(insertStudent);
            System.out.println("Default STUDENT account created: student1 / student123");
        }
    }

    private static void seedSampleIncidents(Statement stmt) throws SQLException {
        ResultSet rsIncidents = stmt.executeQuery("SELECT COUNT(*) FROM incidents");
        if (rsIncidents.next() && rsIncidents.getInt(1) == 0) {
            // Get student1 user_id
            ResultSet rsStudentId = stmt.executeQuery("SELECT user_id FROM users WHERE username = 'student1'");
            int studentUserId = 2; // fallback
            if (rsStudentId.next()) {
                studentUserId = rsStudentId.getInt("user_id");
            }

            String seedIncidentsSql = String.format("""
                INSERT INTO incidents (student_user_id, student_name, student_id, department, semester, contact_number, email, incident_date, incident_time, incident_location, ragging_type, description, persons_involved, witness_details, evidence_description, immediate_help_required, status, assigned_officer, investigation_notes, action_taken, resolution_details)
                VALUES 
                (%d, 'Kanishk Sharma', 'PIET25CR023', '3CSD', 'III Semester', '9876501234', 'kanishk.sharma@piet.ac.in', '2026-08-15', '04:30 PM', 'Senior Hostel Block B Ground Floor', 'Verbal Harassment', 'Seniors forced junior students to stand outside for hours and use abusive language.', '2 Senior Students (4th Year CSE)', 'Aarav Patel (Classmate)', 'Audio recording captured on mobile phone', 'No', 'Under Investigation', 'Prof. R. K. Verma', 'Initial statements recorded from witnesses.', 'Notice issued to accused students.'),
                (%d, 'Kanishk Sharma', 'PIET25CR023', '3CSD', 'III Semester', '9876501234', 'kanishk.sharma@piet.ac.in', '2026-08-20', '01:15 PM', 'College Main Canteen Area', 'Psychological Harassment', 'Systematic exclusion and psychological pressure applied during lunch breaks.', 'Group of 3 seniors', 'Canteen staff member Ramesh', 'CCTV footage request submitted', 'No', 'Pending', 'Unassigned', NULL, NULL, NULL),
                (%d, 'Kanishk Sharma', 'PIET25CR023', '3CSD', 'III Semester', '9876501234', 'kanishk.sharma@piet.ac.in', '2026-07-10', '08:00 PM', 'College Bus Stop', 'Cyber/Online Harassment', 'Unwanted aggressive messaging on WhatsApp group demanding assignment copies.', 'Anonymous WhatsApp account', 'Screenshots of chat group', 'Exported PDF of WhatsApp chat history', 'No', 'Resolved', 'Dr. S. K. Gupta', 'Identified phone number owner via college registry.', 'Warning issued and undertaking signed in front of Dean Academic.');
            """, studentUserId, studentUserId, studentUserId);

            stmt.executeUpdate(seedIncidentsSql);
            System.out.println("Sample incident records inserted successfully.");
        }
    }
}
