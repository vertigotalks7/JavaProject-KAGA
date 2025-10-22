package quizapp.dao;

import quizapp.models.User;
import quizapp.utils.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAO {

    public Optional<User> findUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("email"),
                            rs.getBoolean("isAdmin")
                    );
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding user by email: " + e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY username";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"), // Be cautious about loading passwords unnecessarily
                        rs.getString("email"),
                        rs.getBoolean("isAdmin")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all users: " + e.getMessage());
            e.printStackTrace();
        }
        return users;
    }

    public void saveUser(String username, String email, String password, boolean isAdmin) {
        String sql = "INSERT INTO users (username, email, password, isAdmin) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.setBoolean(4, isAdmin);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error saving user: " + e.getMessage());
            e.printStackTrace();
        }
    }
}