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
        return questionDAO.getQuestionsForCategory(category.getId(), limit);
    }

    public void saveResult(QuizResult result) {
        quizResultDAO.saveResult(result);
    }

    public List<QuizResult> getHistoryForUser(User user) {
        return quizResultDAO.getResultsForUser(user.getId());
    }

    public List<LeaderboardEntry> getLeaderboard() {
        return quizResultDAO.getTopScores(15);
    }

    // --- Admin Methods ---
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