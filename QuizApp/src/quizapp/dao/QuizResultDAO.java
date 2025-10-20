package quizapp.dao;

import quizapp.models.LeaderboardEntry;
import quizapp.models.QuizResult;
import quizapp.utils.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuizResultDAO {

    public void saveResult(QuizResult result) {
        String sql = "INSERT INTO quiz_results (user_id, category_id, category_name, score, total_questions, date_taken) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, result.getUserId());
            stmt.setInt(2, result.getCategoryId());
            stmt.setString(3, result.getCategoryName());
            stmt.setInt(4, result.getScore());
            stmt.setInt(5, result.getTotalQuestions());
            stmt.setTimestamp(6, result.getDateTaken());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<QuizResult> getResultsForUser(int userId) {
        List<QuizResult> results = new ArrayList<>();
        String sql = "SELECT * FROM quiz_results WHERE user_id = ? ORDER BY date_taken DESC";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                results.add(new QuizResult(
                        rs.getInt("user_id"),
                        rs.getInt("category_id"),
                        rs.getString("category_name"),
                        rs.getInt("score"),
                        rs.getInt("total_questions"),
                        rs.getTimestamp("date_taken")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public List<LeaderboardEntry> getTopScores(int limit) {
        List<LeaderboardEntry> topScores = new ArrayList<>();
        String sql = "SELECT u.username, r.category_name, MAX(r.score) AS high_score " +
                "FROM quiz_results r " +
                "JOIN users u ON r.user_id = u.id " +
                "GROUP BY u.username, r.category_name " +
                "ORDER BY high_score DESC " +
                "LIMIT ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                topScores.add(new LeaderboardEntry(
                        rs.getString("username"),
                        rs.getString("category_name"),
                        rs.getInt("high_score")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return topScores;
    }

    public void deleteResultsForUser(int userId) {
        String sql = "DELETE FROM quiz_results WHERE user_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}