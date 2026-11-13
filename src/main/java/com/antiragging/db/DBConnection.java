package com.antiragging.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * JDBC Connection Manager for Anti-Ragging System.
 * Author: Kanishk Sharma (PIET25CR023)
 */
public class DBConnection {

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found! Ensure mysql-connector-j is included.");
        }
    }

    /**
     * Connects to MySQL server without selecting a specific database.
     * Useful for database creation and verification.
     */
    public static Connection getRawServerConnection() throws SQLException {
        return DriverManager.getConnection(
                DatabaseConfig.getBaseUrl(),
                DatabaseConfig.getDbUsername(),
                DatabaseConfig.getDbPassword()
        );
    }

    /**
     * Connects directly to the anti_ragging_db database.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                DatabaseConfig.getDatabaseUrl(),
                DatabaseConfig.getDbUsername(),
                DatabaseConfig.getDbPassword()
        );
    }
}
