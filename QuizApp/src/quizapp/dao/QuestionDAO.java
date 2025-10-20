package quizapp.dao;

import quizapp.models.Option;
import quizapp.models.Question;
import quizapp.utils.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class QuestionDAO {

    public List<Question> getQuestionsForCategory(int categoryId, int limit) {
        Map<Integer, Question> questionMap = new LinkedHashMap<>();
        String sql = "SELECT q.id AS question_id, q.question_text, q.category_id, o.option_text, o.is_correct " +
                "FROM (SELECT * FROM questions WHERE category_id = ? ORDER BY RAND() LIMIT ?) AS q " +
                "JOIN options o ON q.id = o.question_id";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, categoryId);
            stmt.setInt(2, limit);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int qId = rs.getInt("question_id");

                    Question question = questionMap.get(qId);
                    if (question == null) {
                        question = new Question(
                                qId, // Pass the ID to the model
                                rs.getString("question_text"),
                                new ArrayList<>(),
                                rs.getInt("category_id")
                        );
                        questionMap.put(qId, question);
                    }

                    Option option = new Option(
                            rs.getString("option_text"),
                            rs.getBoolean("is_correct")
                    );

                    question.getOptions().add(option);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(questionMap.values());
    }

    public void deleteQuestion(int questionId) {
        String sql = "DELETE FROM questions WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, questionId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addQuestionWithOptions(int categoryId, String questionText, List<Option> options) {
        String questionSql = "INSERT INTO questions (category_id, question_text) VALUES (?, ?)";
        String optionSql = "INSERT INTO options (question_id, option_text, is_correct) VALUES (?, ?, ?)";
        Connection conn = null;

        try {
            conn = DatabaseManager.getConnection();
            conn.setAutoCommit(false); // Start transaction

            int questionId;
            try (PreparedStatement questionStmt = conn.prepareStatement(questionSql, Statement.RETURN_GENERATED_KEYS)) {
                questionStmt.setInt(1, categoryId);
                questionStmt.setString(2, questionText);
                questionStmt.executeUpdate();

                try (ResultSet generatedKeys = questionStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        questionId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Creating question failed, no ID obtained.");
                    }
                }
            }

            try (PreparedStatement optionStmt = conn.prepareStatement(optionSql)) {
                for (Option option : options) {
                    optionStmt.setInt(1, questionId);
                    optionStmt.setString(2, option.getOptionText());
                    optionStmt.setBoolean(3, option.isCorrect());
                    optionStmt.addBatch();
                }
                optionStmt.executeBatch();
            }

            conn.commit();

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}