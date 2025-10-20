package quizapp.models;

import java.sql.Timestamp;

public class QuizResult {
    private final int userId;
    private final int categoryId;
    private final int score;
    private final int totalQuestions;
    private final Timestamp dateTaken;
    private final String categoryName;

    public QuizResult(int userId, int categoryId, String categoryName, int score, int totalQuestions, Timestamp dateTaken) {
        this.userId = userId;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.dateTaken = dateTaken;
    }

    public int getUserId() { return userId; }
    public int getCategoryId() { return categoryId; }
    public int getScore() { return score; }
    public int getTotalQuestions() { return totalQuestions; }
    public Timestamp getDateTaken() { return dateTaken; }
    public String getCategoryName() { return categoryName; }
}