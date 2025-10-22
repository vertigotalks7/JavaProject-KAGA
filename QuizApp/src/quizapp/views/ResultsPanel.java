package quizapp.views;

import quizapp.MainApp;
import quizapp.models.QuizResult;
import quizapp.utils.Theme;
import quizapp.views.components.StyledButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;


public class ResultsPanel extends JPanel {

    private final MainApp mainApp;
    private JLabel scoreLabel;
    private JLabel categoryLabel;

    public ResultsPanel(MainApp mainApp) {
        this.mainApp = mainApp;
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(40, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);

        JLabel titleLabel = new JLabel("Quiz Completed!");
        titleLabel.setFont(Theme.getFont(Theme.FONT_TITLE));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        categoryLabel = new JLabel();
        categoryLabel.setFont(Theme.getFont(Theme.FONT_HEADER));
        categoryLabel.setHorizontalAlignment(SwingConstants.CENTER);
        categoryLabel.setForeground(Color.GRAY);

        scoreLabel = new JLabel();
        scoreLabel.setFont(Theme.getFont(Theme.FONT_HEADER));
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);

        StyledButton backToDashboardButton = new StyledButton("Back to Dashboard");
        backToDashboardButton.addActionListener(e -> mainApp.showPanel("dashboard"));

        gbc.weighty = 0;
        add(titleLabel, gbc);
        add(categoryLabel, gbc);

        gbc.insets = new Insets(20, 0, 20, 0); // More space around score
        add(scoreLabel, gbc);

        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.NONE; // Don't stretch the button
        gbc.anchor = GridBagConstraints.CENTER; // Center the button
        add(backToDashboardButton, gbc);
    }


    public void displayResult(QuizResult result) {
        if (result != null) {
            scoreLabel.setText(String.format("You scored %d / %d", result.getScore(), result.getTotalQuestions()));
            categoryLabel.setText("Category: " + result.getCategoryName());
        } else {

            scoreLabel.setText("Score: N/A");
            categoryLabel.setText("Category: N/A");
        }
    }
}