package quizapp.services;

import quizapp.dao.CategoryDAO;
import quizapp.dao.QuestionDAO;
import quizapp.dao.QuizResultDAO;
import quizapp.models.*;

import java.util.List;

public class QuizService {

    private final CategoryDAO categoryDAO;
    private final QuestionDAO questionDAO;
    private final QuizResultDAO quizResultDAO;

    public QuizService() {
        this.categoryDAO = new CategoryDAO();
        this.questionDAO = new QuestionDAO();
        this.quizResultDAO = new QuizResultDAO();
    }

    public List<Category> getCategories() {
        return categoryDAO.getAllCategories();
    }

    public List<Question> getQuestionsForCategory(Category category, int limit) {
        if (category == null) return List.of();
        return questionDAO.getQuestionsForCategory(category.getId(), limit);
    }

    public void saveResult(QuizResult result) {
        quizResultDAO.saveResult(result);
    }

    public List<QuizResult> getHistoryForUser(User user) {
        if (user == null) return List.of();
        return quizResultDAO.getResultsForUser(user.getId());
    }

    public List<LeaderboardEntry> getLeaderboard() {
        return quizResultDAO.getTopScores(15);
    }

    public void saveUserAnswers(List<UserAnswer> answers) {
        quizResultDAO.saveUserAnswers(answers);
    }

    public CategoryStats getStatsForCategory(User user, Category category) {
        if (user == null || category == null) return new CategoryStats(0, 0);
        return quizResultDAO.getStatsForCategory(user.getId(), category.getId());
    }

    public void addCategory(String name) {
        categoryDAO.addCategory(name);
    }

    public void deleteCategory(int categoryId) {
        categoryDAO.deleteCategory(categoryId);
    }

    public void deleteQuestion(int questionId) {
        questionDAO.deleteQuestion(questionId);
    }

    public void addQuestionWithOptions(int categoryId, String questionText, List<Option> options) {
        questionDAO.addQuestionWithOptions(categoryId, questionText, options);
    }

    public void deleteResultsForUser(int userId) {
        quizResultDAO.deleteResultsForUser(userId);
    }
}