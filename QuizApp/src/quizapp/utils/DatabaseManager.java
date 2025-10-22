package quizapp.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/brainbuzz_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"; // Added params
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "WINDOWS10";

    private static Connection connection;

    private DatabaseManager() {}

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            } catch (ClassNotFoundException e) {
                System.err.println("FATAL ERROR: MySQL JDBC Driver not found in classpath.");
                e.printStackTrace();
                throw new SQLException("MySQL Driver not found.", e);
            } catch (SQLException e) {
                System.err.println("FATAL ERROR: Could not connect to database.");
                System.err.println("DB_URL: " + DB_URL);
                System.err.println("DB_USER: " + DB_USER);
                e.printStackTrace();
                throw e;
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
                connection = null;
            }
        }
    }

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(DatabaseManager::closeConnection));
    }
}