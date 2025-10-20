package quizapp.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    // --- CONFIGURE YOUR DATABASE HERE ---
    private static final String DB_URL = "jdbc:mysql://localhost:3306/brainbuzz_db";
    private static final String DB_USER = "root";       // <-- Change this to your MySQL username
    private static final String DB_PASSWORD = "WINDOWS10";   // <-- Change this to your MySQL password
    // ------------------------------------

    private static Connection connection;

    private DatabaseManager() {}

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            } catch (ClassNotFoundException e) {
                System.err.println("MySQL JDBC Driver not found. Please add it to your project's libraries.");
                e.printStackTrace();
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}