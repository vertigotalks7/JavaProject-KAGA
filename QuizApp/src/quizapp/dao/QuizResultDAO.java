package quizapp.dao;

import quizapp.models.CategoryStats;
import quizapp.models.LeaderboardEntry;
import quizapp.models.QuizResult;
import quizapp.models.UserAnswer;
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
            System.err.println("Error saving quiz result: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<QuizResult> getResultsForUser(int userId) {
        List<QuizResult> results = new ArrayList<>();
        String sql = "SELECT * FROM quiz_results WHERE user_id = ? ORDER BY date_taken DESC";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
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
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user results: " + e.getMessage());
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
                "ORDER BY high_score DESC, MAX(r.date_taken) DESC " + // Secondary sort by date
                "LIMIT ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    topScores.add(new LeaderboardEntry(
                            rs.getString("username"),
                            rs.getString("category_name"),
                            rs.getInt("high_score")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching leaderboard scores: " + e.getMessage());
            e.printStackTrace();
        }
        return topScores;
    }

    public void deleteResultsForUser(int userId) {
        String sql = "DELETE FROM quiz_results WHERE user_id = ?";
        String sqlAnswers = "DELETE FROM user_answers WHERE user_id = ?";

        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
            conn.setAutoCommit(false);

            try(PreparedStatement stmt = conn.prepareStatement(sql)){
                stmt.setInt(1, userId);
                stmt.executeUpdate();
            }
            try(PreparedStatement stmtAns = conn.prepareStatement(sqlAnswers)){
                stmtAns.setInt(1, userId);
                stmtAns.executeUpdate();
            }
            conn.commit();

        } catch (SQLException e) {
            System.err.println("Error deleting user results: " + e.getMessage());
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); } catch (SQLException ex) { ex.printStackTrace(); }
        }
    }

    public void saveUserAnswers(List<UserAnswer> answers) {
        String sql = "INSERT INTO user_answers (user_id, question_id, category_id, is_correct) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (UserAnswer answer : answers) {
                stmt.setInt(1, answer.getUserId());
                stmt.setInt(2, answer.getQuestionId());
                stmt.setInt(3, answer.getCategoryId());
                stmt.setBoolean(4, answer.isCorrect());
                stmt.addBatch();
            }
            stmt.executeBatch();

        } catch (SQLException e) {
            System.err.println("Error saving user answers: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public CategoryStats getStatsForCategory(int userId, int categoryId) {
        String sql = "SELECT COUNT(*) AS total, SUM(CASE WHEN is_correct = 1 THEN 1 ELSE 0 END) AS correct " +
                "FROM user_answers WHERE user_id = ? AND category_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, categoryId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int total = rs.getInt("total");
                    int correct = rs.getInt("correct");
                    return new CategoryStats(total, correct);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching category stats: " + e.getMessage());
            e.printStackTrace();
        }
        return new CategoryStats(0, 0);
    }
}