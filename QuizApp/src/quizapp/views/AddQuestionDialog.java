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
        JTextArea newQuestionText = new JTextArea(4, 30);
        newQuestionText.setFont(Theme.getFont(Theme.FONT_BODY));
        newQuestionText.setWrapStyleWord(true);
        newQuestionText.setLineWrap(true);
        panel.add(new JScrollPane(newQuestionText));
        panel.add(Box.createVerticalStrut(15));

        // Options
        panel.add(createLabel("Options (Select the correct one):"));
        JTextField[] optionFields = new JTextField[4];
        JRadioButton[] radioButtons = new JRadioButton[4];
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
        StyledButton addQuestionBtn = new StyledButton("Add Question to Database");
        addQuestionBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        addQuestionBtn.addActionListener(e -> {
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
            for (int i = 0; i < 4; i++) {
                String optionText = optionFields[i].getText().trim();
                if (optionText.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "All four option fields must be filled.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                newOptions.add(new Option(optionText, radioButtons[i].isSelected()));
            }

            mainApp.getQuizService().addQuestionWithOptions(selectedCategory.getId(), questionText, newOptions);
            JOptionPane.showMessageDialog(this, "Question added successfully!");

            dispose(); // Close dialog on success
        });

        panel.add(addQuestionBtn);
        add(panel);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(Theme.getFont(Theme.FONT_BODY_BOLD));
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
