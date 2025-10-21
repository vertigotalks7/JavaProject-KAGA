package quizapp.models;

public class CategoryStats {
    private final int totalQuestions;
    private final int correctAnswers;
    private final int wrongAnswers;

    public CategoryStats(int totalQuestions, int correctAnswers) {
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
        this.wrongAnswers = totalQuestions - correctAnswers;
    }

    public int getTotalQuestions() { return totalQuestions; }
    public int getCorrectAnswers() { return correctAnswers; }
    public int getWrongAnswers() { return wrongAnswers; }
}