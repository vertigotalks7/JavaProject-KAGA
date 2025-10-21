package quizapp.views;

import quizapp.MainApp;
import quizapp.models.*; // Import UserAnswer
import quizapp.utils.Theme;
import quizapp.views.components.StyledButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizPanel extends JPanel {

    private static final int QUIZ_TIME_SECONDS = 300; // 5 minutes
    private static final int QUESTIONS_PER_QUIZ = 15;

    private final MainApp mainApp;
    private Category currentCategory;
    private List<Question> questionList;
    private int currentQuestionIndex;
    private final Map<Question, Option> userAnswers = new HashMap<>();

    private JLabel timerLabel;
    private JLabel questionNumberLabel;
    private JTextArea questionTextArea;
    private JPanel optionsPanel;
    private ButtonGroup optionsGroup;
    private Timer quizTimer;

    private StyledButton previousButton;
    private StyledButton nextButton;
    private StyledButton exitButton;

    public QuizPanel(MainApp mainApp) {
        this.mainApp = mainApp;
        initComponents();
    }

    public void startQuiz(Category category) {
        this.currentCategory = category;
        this.questionList = mainApp.getQuizService().getQuestionsForCategory(category, QUESTIONS_PER_QUIZ);
        this.currentQuestionIndex = 0;
        this.userAnswers.clear();

        if (questionList.isEmpty() || questionList.size() < QUESTIONS_PER_QUIZ) {
            JOptionPane.showMessageDialog(mainApp,
                    "Not enough questions in the database for a full quiz in this category.\n" +
                            "Please add at least " + QUESTIONS_PER_QUIZ + " questions.",
                    "Quiz Error", JOptionPane.ERROR_MESSAGE);
            mainApp.showPanel("dashboard");
            return;
        }

        displayQuestion();
        startTimer();
    }

    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 40, 40, 40));

        // --- Top Panel ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        questionNumberLabel = new JLabel();
        questionNumberLabel.setFont(Theme.getFont(Theme.FONT_HEADER));
        timerLabel = new JLabel();
        timerLabel.setFont(Theme.getFont(Theme.FONT_HEADER));
        topPanel.add(questionNumberLabel, BorderLayout.WEST);
        topPanel.add(timerLabel, BorderLayout.EAST);

        // --- Center Panel ---
        JPanel centerPanel = new JPanel(new BorderLayout(20, 20));
        centerPanel.setOpaque(false);
        questionTextArea = new JTextArea();
        questionTextArea.setFont(Theme.getFont(Theme.FONT_BODY_BOLD));
        questionTextArea.setWrapStyleWord(true);
        questionTextArea.setLineWrap(true);
        questionTextArea.setEditable(false);
        questionTextArea.setFocusable(false);
        questionTextArea.setOpaque(false);
        optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.setOpaque(false);
        centerPanel.add(questionTextArea, BorderLayout.NORTH);
        centerPanel.add(optionsPanel, BorderLayout.CENTER);

        // --- Bottom Navigation Panel ---
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        previousButton = new StyledButton("Previous");
        nextButton = new StyledButton("Next");
        exitButton = new StyledButton("Exit Quiz");

        previousButton.addActionListener(e -> navigateToPreviousQuestion());
        nextButton.addActionListener(e -> navigateToNextQuestion());
        exitButton.addActionListener(e -> exitQuiz());

        JPanel leftButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftButtonPanel.setOpaque(false);
        leftButtonPanel.add(previousButton);
        leftButtonPanel.add(exitButton);

        bottomPanel.add(leftButtonPanel, BorderLayout.WEST);
        bottomPanel.add(nextButton, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void displayQuestion() {
        Question q = questionList.get(currentQuestionIndex);
        questionNumberLabel.setText("Question " + (currentQuestionIndex + 1) + " / " + questionList.size());
        questionTextArea.setText(q.getQuestionText());

        optionsPanel.removeAll();
        optionsGroup = new ButtonGroup();

        List<Option> shuffledOptions = new ArrayList<>(q.getOptions());
        Collections.shuffle(shuffledOptions);

        Option previousAnswer = userAnswers.get(q);

        for (Option option : shuffledOptions) {
            JRadioButton radioButton = new JRadioButton(option.getOptionText());
            radioButton.setFont(Theme.getFont(Theme.FONT_BODY));
            radioButton.setOpaque(false);
            radioButton.setActionCommand(option.getOptionText());
            optionsGroup.add(radioButton);

            if (previousAnswer != null && option.getOptionText().equals(previousAnswer.getOptionText())) {
                radioButton.setSelected(true);
            }

            optionsPanel.add(radioButton);
            optionsPanel.add(Box.createVerticalStrut(10));
        }

        updateNavigationButtons();
        revalidate();
        repaint();
    }

    private void updateNavigationButtons() {
        previousButton.setEnabled(currentQuestionIndex > 0);
        exitButton.setEnabled(true);
        if (currentQuestionIndex == questionList.size() - 1) {
            nextButton.setText("Submit");
        } else {
            nextButton.setText("Next");
        }
    }

    private void saveCurrentAnswer() {
        if (optionsGroup != null && optionsGroup.getSelection() != null) {
            String selectedOptionText = optionsGroup.getSelection().getActionCommand();
            Question currentQuestion = questionList.get(currentQuestionIndex);
            for (Option opt : currentQuestion.getOptions()) {
                if (opt.getOptionText().equals(selectedOptionText)) {
                    userAnswers.put(currentQuestion, opt);
                    break;
                }
            }
        }
    }

    private void navigateToPreviousQuestion() {
        saveCurrentAnswer();
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            displayQuestion();
        }
    }

    private void navigateToNextQuestion() {
        saveCurrentAnswer();
        if (currentQuestionIndex < questionList.size() - 1) {
            currentQuestionIndex++;
            displayQuestion();
        } else {
            finishQuiz();
        }
    }

    private void exitQuiz() {
        if (quizTimer != null) {
            quizTimer.stop();
        }
        int choice = JOptionPane.showConfirmDialog(
                this, "Are you sure you want to exit the quiz?\nYour progress will not be saved.",
                "Confirm Exit", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            mainApp.showPanel("dashboard");
        } else {
            if (quizTimer != null && !quizTimer.isRunning()) {
                quizTimer.start();
            }
        }
    }

    private void startTimer() {
        if (quizTimer != null) {
            quizTimer.stop();
        }
        final int[] timeLeft = {QUIZ_TIME_SECONDS};
        timerLabel.setText(String.format("Time: %02d:%02d", timeLeft[0] / 60, timeLeft[0] % 60));

        quizTimer = new Timer(1000, e -> {
            timeLeft[0]--;
            timerLabel.setText(String.format("Time: %02d:%02d", timeLeft[0] / 60, timeLeft[0] % 60));
            if (timeLeft[0] <= 0) {
                JOptionPane.showMessageDialog(this, "Time's up! Submitting your answers.", "Time Over", JOptionPane.WARNING_MESSAGE);
                finishQuiz();
            }
        });
        quizTimer.start();
    }

    private void finishQuiz() {
        if (quizTimer != null) {
            quizTimer.stop();
        }
        saveCurrentAnswer(); // Save the final answer

        int score = 0;
        List<UserAnswer> answerLog = new ArrayList<>(); // List to hold individual answers

        for (Question q : questionList) {
            Option userAnswer = userAnswers.get(q);
            // Default to incorrect if no answer was selected or stored
            boolean wasCorrect = (userAnswer != null && userAnswer.isCorrect());

            if (wasCorrect) {
                score++;
            }

            // Log every answer attempt (even if unanswered, logged as incorrect)
            answerLog.add(new UserAnswer(
                    mainApp.getCurrentUser().getId(),
                    q.getId(),
                    q.getCategoryId(), // Use the category ID from the question object
                    wasCorrect
            ));
        }

        // Save the detailed answers to the new user_answers table
        mainApp.getQuizService().saveUserAnswers(answerLog);

        // Save the overall quiz result (as before)
        QuizResult result = new QuizResult(
                mainApp.getCurrentUser().getId(),
                currentCategory.getId(),
                currentCategory.getName(),
                score,
                questionList.size(),
                new Timestamp(System.currentTimeMillis())
        );
        mainApp.getQuizService().saveResult(result);

        mainApp.showResults(result);
    }
}