package quizapp;

import quizapp.models.Category;
import quizapp.models.QuizResult;
import quizapp.models.User;
import quizapp.services.QuizService;
import quizapp.services.UserService;
import quizapp.views.*;

import javax.swing.*;
import java.awt.*;

public class MainApp extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private UserService userService;
    private QuizService quizService;
    private User currentUser;

    private LoginPanel loginPanel;
    private RegistrationPanel registrationPanel;
    private DashboardPanel dashboardPanel;
    private ProfilePanel profilePanel;
    private QuizPanel quizPanel;
    private ResultsPanel resultsPanel;
    private HistoryPanel historyPanel;
    private LeaderboardPanel leaderboardPanel;
    private AdminPanel adminPanel;

    private UserManagementDialog userManagementDialog;

    public MainApp() {
        setTitle("BrainBuzz Quiz App");
        setSize(1070, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        userService = new UserService();
        quizService = new QuizService();

        loginPanel = new LoginPanel(this);
        registrationPanel = new RegistrationPanel(this);
        dashboardPanel = new DashboardPanel(this);
        profilePanel = new ProfilePanel(this);
        quizPanel = new QuizPanel(this);
        resultsPanel = new ResultsPanel(this);
        historyPanel = new HistoryPanel(this);
        leaderboardPanel = new LeaderboardPanel(this);
        adminPanel = new AdminPanel(this);

        mainPanel.add(loginPanel, "login");
        mainPanel.add(registrationPanel, "register");
        mainPanel.add(dashboardPanel, "dashboard");
        mainPanel.add(profilePanel, "profile");
        mainPanel.add(quizPanel, "quiz");
        mainPanel.add(resultsPanel, "results");
        mainPanel.add(historyPanel, "history");
        mainPanel.add(leaderboardPanel, "leaderboard");
        mainPanel.add(adminPanel, "admin");

        add(mainPanel);
        cardLayout.show(mainPanel, "login");
    }

    public void showPanel(String panelName) {
        switch (panelName) {
            case "profile":
                if (currentUser != null) profilePanel.updateProfileInfo(currentUser);
                break;
            case "history":
                historyPanel.populateHistory();
                break;
            case "leaderboard":
                leaderboardPanel.populateLeaderboard();
                break;
            case "admin":
                // The admin panel is now static, so no refresh needed here
                break;
        }
        cardLayout.show(mainPanel, panelName);
    }

    public void openUserManagementDialog() {
        if (userManagementDialog == null) {
            userManagementDialog = new UserManagementDialog(this);
        }
        userManagementDialog.setVisible(true);
    }

    public void startQuiz(Category category) {
        quizPanel.startQuiz(category);
        showPanel("quiz");
    }

    public void showResults(QuizResult result) {
        resultsPanel.displayResult(result);
        showPanel("results");
    }

    public void loginUser(User user) {
        this.currentUser = user;
        dashboardPanel.updateForNewUser(user);
        showPanel("dashboard");
    }

    public void logoutUser() {
        this.currentUser = null;
        loginPanel.clearFields();
        showPanel("login");
    }

    public User getCurrentUser() { return currentUser; }
    public UserService getUserService() { return userService; }
    public QuizService getQuizService() { return quizService; }
    public LoginPanel getLoginPanel() { return loginPanel; }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new MainApp().setVisible(true));
    }
}

