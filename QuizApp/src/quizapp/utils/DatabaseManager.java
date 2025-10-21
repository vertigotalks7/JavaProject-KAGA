package quizapp.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    // --- CONFIGURE YOUR DATABASE HERE ---
    private static final String DB_URL = "jdbc:mysql://localhost:3306/brainbuzz_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"; // Added params
    private static final String DB_USER = "root";       // <-- CHANGE THIS
    private static final String DB_PASSWORD = "WINDOWS10";   // <-- CHANGE THIS
    // ------------------------------------

    private static Connection connection;

    private DatabaseManager() {} // Private constructor for singleton pattern

    public static Connection getConnection() throws SQLException {
        // Ensure driver is loaded only once and handle potential errors
        if (connection == null || connection.isClosed()) {
            try {
                // Explicitly load the driver class
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            } catch (ClassNotFoundException e) {
                System.err.println("FATAL ERROR: MySQL JDBC Driver not found in classpath.");
                e.printStackTrace();
                // Consider throwing a runtime exception or showing a user dialog here
                throw new SQLException("MySQL Driver not found.", e);
            } catch (SQLException e) {
                System.err.println("FATAL ERROR: Could not connect to database.");
                System.err.println("DB_URL: " + DB_URL);
                System.err.println("DB_USER: " + DB_USER);
                // Don't print password in logs generally
                e.printStackTrace();
                throw e; // Re-throw SQLException
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                connection = null; // Set to null after closing
            }
        }
    }

    // Optional: Add a shutdown hook to ensure connection closes on exit
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(DatabaseManager::closeConnection));
    }
}