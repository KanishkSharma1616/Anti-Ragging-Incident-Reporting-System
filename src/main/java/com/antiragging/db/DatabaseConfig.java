package com.antiragging.db;

import java.io.InputStream;
import java.util.Properties;

/**
 * Database Configuration holder reading from database.properties file.
 * Author: Kanishk Sharma (PIET25CR023)
 */
public class DatabaseConfig {

    private static String dbHost = "localhost";
    private static String dbPort = "3306";
    private static String dbName = "anti_ragging_db";
    private static String dbUsername = "root";
    private static String dbPassword = "";

    static {
        loadConfig();
    }

    private static void loadConfig() {
        try (InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream("database.properties")) {
            if (input != null) {
                Properties prop = new Properties();
                prop.load(input);
                dbHost = prop.getProperty("db.host", dbHost).trim();
                dbPort = prop.getProperty("db.port", dbPort).trim();
                dbName = prop.getProperty("db.name", dbName).trim();
                dbUsername = prop.getProperty("db.username", dbUsername).trim();
                dbPassword = prop.getProperty("db.password", "").trim();
            }
        } catch (Exception e) {
            System.err.println("Warning: Unable to load database.properties, using default configurations. " + e.getMessage());
        }
    }

    public static String getDbHost() {
        return dbHost;
    }

    public static String getDbPort() {
        return dbPort;
    }

    public static String getDbName() {
        return dbName;
    }

    public static String getDbUsername() {
        return dbUsername;
    }

    public static String getDbPassword() {
        return dbPassword;
    }

    public static String getBaseUrl() {
        return "jdbc:mysql://" + dbHost + ":" + dbPort + "/?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
    }

    public static String getDatabaseUrl() {
        return "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName + "?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
    }
}
