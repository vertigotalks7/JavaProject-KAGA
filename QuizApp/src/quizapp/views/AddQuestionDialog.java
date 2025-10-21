package quizapp.views;

import quizapp.MainApp;
import quizapp.models.Category;
import quizapp.models.Option;
import quizapp.utils.Theme;
import quizapp.views.components.StyledButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AddQuestionDialog extends JDialog {

    private final MainApp mainApp;
    private JComboBox<Category> categoryComboBox;
    private JTextArea newQuestionText;
    private JTextField[] optionFields;
    private JRadioButton[] radioButtons;

    public AddQuestionDialog(Frame parent) {
        super(parent, "Add New Question", true);
        this.mainApp = (MainApp) parent;
        initComponents();
        setSize(500, 600);
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Category Selection
        panel.add(createLabel("Select Category:"));
        categoryComboBox = new JComboBox<>();
        categoryComboBox.setFont(Theme.getFont(Theme.FONT_BODY));
        refreshCategories();
        panel.add(categoryComboBox);
        panel.add(Box.createVerticalStrut(15));

        // Question Text
        panel.add(createLabel("Question Text:"));
        newQuestionText = new JTextArea(4, 30);
        newQuestionText.setFont(Theme.getFont(Theme.FONT_BODY));
        newQuestionText.setWrapStyleWord(true);
        newQuestionText.setLineWrap(true);
        panel.add(new JScrollPane(newQuestionText));
        panel.add(Box.createVerticalStrut(15));

        // Options
        panel.add(createLabel("Options (Select the correct one):"));
        optionFields = new JTextField[4];
        radioButtons = new JRadioButton[4];
        ButtonGroup optionGroup = new ButtonGroup();

        for (int i = 0; i < 4; i++) {
            JPanel optionPanel = new JPanel(new BorderLayout(5, 0));
            optionPanel.setOpaque(false);
            radioButtons[i] = new JRadioButton();
            optionFields[i] = new JTextField(25);
            optionFields[i].setFont(Theme.getFont(Theme.FONT_BODY));
            optionGroup.add(radioButtons[i]);
            optionPanel.add(radioButtons[i], BorderLayout.WEST);
            optionPanel.add(optionFields[i], BorderLayout.CENTER);
            panel.add(optionPanel);
            panel.add(Box.createVerticalStrut(5));
        }
        radioButtons[0].setSelected(true); // Default selection

        panel.add(Box.createVerticalStrut(20));

        // Buttons at the bottom
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);

        StyledButton addQuestionBtn = new StyledButton("Add Question");
        addQuestionBtn.addActionListener(e -> addQuestionAction());

        StyledButton cancelBtn = new StyledButton("Cancel");
        cancelBtn.setBackground(Color.GRAY); // Different color for cancel
        cancelBtn.addActionListener(e -> dispose());

        buttonPanel.add(addQuestionBtn);
        buttonPanel.add(cancelBtn);

        panel.add(buttonPanel);
        add(panel);
    }

    private void addQuestionAction() {
        Category selectedCategory = (Category) categoryComboBox.getSelectedItem();
        if (selectedCategory == null) {
            JOptionPane.showMessageDialog(this, "Please select a category.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String questionText = newQuestionText.getText().trim();
        if (questionText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Question text cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Option> newOptions = new ArrayList<>();
        int correctIndex = -1;
        for (int i = 0; i < 4; i++) {
            String optionText = optionFields[i].getText().trim();
            if (optionText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All four option fields must be filled.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            boolean isCorrect = radioButtons[i].isSelected();
            if (isCorrect) correctIndex = i; // Track which is correct
            newOptions.add(new Option(optionText, isCorrect));
        }

        // Ensure exactly one correct answer is selected
        if (correctIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select one option as the correct answer.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        mainApp.getQuizService().addQuestionWithOptions(selectedCategory.getId(), questionText, newOptions);
        JOptionPane.showMessageDialog(this, "Question added successfully!");

        dispose(); // Close dialog on success
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(Theme.getFont(Theme.FONT_BODY_BOLD));
        label.setAlignmentX(Component.LEFT_ALIGNMENT); // Align labels left
        return label;
    }

    private void refreshCategories() {
        categoryComboBox.removeAllItems();
        List<Category> categories = mainApp.getQuizService().getCategories();
        for (Category category : categories) {
            categoryComboBox.addItem(category);
        }
    }
}