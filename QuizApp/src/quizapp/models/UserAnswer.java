package quizapp.models;

public class UserAnswer {
    private final int userId;
    private final int questionId;
    private final int categoryId;
    private final boolean isCorrect;

    public UserAnswer(int userId, int questionId, int categoryId, boolean isCorrect) {
        this.userId = userId;
        this.questionId = questionId;
        this.categoryId = categoryId;
        this.isCorrect = isCorrect;
    }

    public int getUserId() { return userId; }
    public int getQuestionId() { return questionId; }
    public int getCategoryId() { return categoryId; }
    public boolean isCorrect() { return isCorrect; }
}