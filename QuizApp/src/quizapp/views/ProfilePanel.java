package quizapp.views;

import quizapp.MainApp;
import quizapp.models.Category;
import quizapp.models.CategoryStats;
import quizapp.models.User;
import quizapp.utils.Theme;
import quizapp.views.components.StyledButton;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class ProfilePanel extends JPanel {
    private MainApp mainApp;
    private JLabel usernameLabel;
    private JLabel emailLabel;
    private JComboBox<Category> categoryComboBox;

    private JPanel chartContainerPanel;
    private JLabel totalLabel;
    private JLabel correctLabel;
    private JLabel wrongLabel;
    private JLabel avatarLabel;

    public ProfilePanel(MainApp mainApp) {
        this.mainApp = mainApp;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- Top Panel: Avatar, User Info, Category Selector ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        topPanel.setOpaque(false);

        // Avatar
        avatarLabel = new JLabel();
        avatarLabel.setPreferredSize(new Dimension(80, 80));
        avatarLabel.setHorizontalAlignment(SwingConstants.CENTER);
        avatarLabel.setVerticalAlignment(SwingConstants.CENTER);
        avatarLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        // User Info Panel
        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setOpaque(false);
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        usernameLabel = new JLabel("Username");
        usernameLabel.setFont(Theme.getFont(Theme.FONT_HEADER));
        emailLabel = new JLabel("email@example.com");
        emailLabel.setFont(Theme.getFont(Theme.FONT_BODY));
        userInfoPanel.add(usernameLabel);
        userInfoPanel.add(emailLabel);

        // Category Selector
        categoryComboBox = new JComboBox<>();
        categoryComboBox.setFont(Theme.getFont(Theme.FONT_BODY));
        categoryComboBox.addActionListener(e -> updateStats());

        topPanel.add(avatarLabel);
        topPanel.add(userInfoPanel);
        topPanel.add(Box.createHorizontalStrut(30));
        topPanel.add(new JLabel("View Stats for:"));
        topPanel.add(categoryComboBox);

        // --- Center Panel: Stats and Chart ---
        JPanel centerPanel = new JPanel(new BorderLayout(30, 10));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(20, 0, 20, 0));

        // Stats Labels Panel (Left side)
        JPanel statsLabelPanel = new JPanel();
        statsLabelPanel.setOpaque(false);
        statsLabelPanel.setLayout(new BoxLayout(statsLabelPanel, BoxLayout.Y_AXIS));
        statsLabelPanel.setBorder(new EmptyBorder(0, 20, 0, 0));

        totalLabel = new JLabel("Total Questions Answered: 0");
        correctLabel = new JLabel("Correct Answers: 0");
        wrongLabel = new JLabel("Wrong Answers: 0");

        Font statsFont = Theme.getFont(Theme.FONT_BODY_BOLD);
        totalLabel.setFont(statsFont);
        correctLabel.setFont(statsFont);
        wrongLabel.setFont(statsFont);

        statsLabelPanel.add(totalLabel);
        statsLabelPanel.add(Box.createVerticalStrut(10));
        statsLabelPanel.add(correctLabel);
        statsLabelPanel.add(Box.createVerticalStrut(10));
        statsLabelPanel.add(wrongLabel);
        statsLabelPanel.add(Box.createVerticalGlue());

        // Chart Container Panel (Right side)
        chartContainerPanel = new JPanel(new BorderLayout());
        chartContainerPanel.setOpaque(false);
        chartContainerPanel.setPreferredSize(new Dimension(350, 350));

        centerPanel.add(statsLabelPanel, BorderLayout.WEST);
        centerPanel.add(chartContainerPanel, BorderLayout.CENTER);

        // --- Bottom Panel: Back Button ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        JButton backButton = new StyledButton("Back to Dashboard");
        backButton.addActionListener(e -> mainApp.showPanel("dashboard"));
        bottomPanel.add(backButton);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void updateProfileInfo(User user) {
        if (user != null) {
            usernameLabel.setText(user.getUsername());
            emailLabel.setText(user.getEmail());

            // Update Avatar
            try {
                ImageIcon icon = new ImageIcon(getClass().getResource("/icons/default_avatar.png"));
                Image img = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                avatarLabel.setIcon(new ImageIcon(img));
                avatarLabel.setText("");
            } catch (Exception e) {
                avatarLabel.setIcon(null);
                avatarLabel.setText("N/A");
                System.err.println("Default avatar image not found in /icons/default_avatar.png");
            }

            // Refresh category dropdown
            categoryComboBox.removeAllItems();
            List<Category> categories = mainApp.getQuizService().getCategories();
            for (Category cat : categories) {
                categoryComboBox.addItem(cat);
            }
            if (!categories.isEmpty()) {
                categoryComboBox.setSelectedIndex(0);
            } else {
                updateStats(); // Update stats even if no categories
            }

        } else {
            // Clear fields if user is null
            usernameLabel.setText("N/A");
            emailLabel.setText("");
            categoryComboBox.removeAllItems();
            avatarLabel.setIcon(null);
            avatarLabel.setText("N/A");
            updateStats(); // Clear stats display
        }
    }

    private void updateStats() {
        Category selectedCategory = (Category) categoryComboBox.getSelectedItem();
        User currentUser = mainApp.getCurrentUser();

        if (selectedCategory == null || currentUser == null) {
            totalLabel.setText("Total Questions Answered: 0");
            correctLabel.setText("Correct Answers: 0");
            wrongLabel.setText("Wrong Answers: 0");
            updateChart(new CategoryStats(0, 0));
            return;
        }

        CategoryStats stats = mainApp.getQuizService().getStatsForCategory(currentUser, selectedCategory);

        totalLabel.setText("Total Questions Answered: " + stats.getTotalQuestions());
        correctLabel.setText("Correct Answers: " + stats.getCorrectAnswers());
        wrongLabel.setText("Wrong Answers: " + stats.getWrongAnswers());

        updateChart(stats);
    }

    private void updateChart(CategoryStats stats) {
        chartContainerPanel.removeAll();

        if (stats.getTotalQuestions() == 0) {
            JLabel noDataLabel = new JLabel("No quiz data available for this category.", SwingConstants.CENTER);
            noDataLabel.setFont(Theme.getFont(Theme.FONT_BODY));
            chartContainerPanel.add(noDataLabel, BorderLayout.CENTER);
            chartContainerPanel.revalidate();
            chartContainerPanel.repaint();
            return;
        }

        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Correct (" + stats.getCorrectAnswers() + ")", stats.getCorrectAnswers());
        dataset.setValue("Wrong (" + stats.getWrongAnswers() + ")", stats.getWrongAnswers());

        JFreeChart pieChart = ChartFactory.createPieChart(
                "Performance",
                dataset,
                true, true, false
        );

        pieChart.getTitle().setFont(Theme.getFont(Theme.FONT_BODY_BOLD));
        pieChart.setBackgroundPaint(Color.WHITE);

        PiePlot plot = (PiePlot) pieChart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlinePaint(null);
        plot.setLabelFont(Theme.getFont(Theme.FONT_SMALL));
        plot.setSectionPaint("Correct (" + stats.getCorrectAnswers() + ")", new Color(0, 180, 0)); // Green
        plot.setSectionPaint("Wrong (" + stats.getWrongAnswers() + ")", new Color(220, 0, 0));   // Red
        plot.setNoDataMessage("No data available");

        ChartPanel chartPanel = new ChartPanel(pieChart);
        chartPanel.setOpaque(false);
        chartContainerPanel.add(chartPanel, BorderLayout.CENTER);

        chartContainerPanel.revalidate();
        chartContainerPanel.repaint();
    }
}