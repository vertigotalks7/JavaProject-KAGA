package quizapp.views;

import quizapp.MainApp;
import quizapp.models.Category;
import quizapp.models.Question;
import quizapp.utils.Theme;
import quizapp.views.components.StyledButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

public class DeleteContentDialog extends JDialog {

    private final MainApp mainApp;
    private JList<Category> categoryList;
    private DefaultListModel<Category> categoryListModel;
    private JList<String> questionList;
    private DefaultListModel<String> questionListModel;
    private List<Question> currentQuestions;

    public DeleteContentDialog(Frame parent) {
        super(parent, "Delete Content", true);
        this.mainApp = (MainApp) parent;
        initComponents();
        setSize(600, 500);
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        contentPanel.setBackground(Color.WHITE);

        // Category Panel
        JPanel categoryPanel = new JPanel(new BorderLayout(5, 5));
        categoryPanel.setOpaque(false);
        categoryPanel.setBorder(createTitledBorder("Categories"));

        categoryListModel = new DefaultListModel<>();
        categoryList = new JList<>(categoryListModel);
        categoryList.setFont(Theme.getFont(Theme.FONT_BODY));
        categoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        categoryList.addListSelectionListener(e -> loadQuestionsForCategory());

        JPanel categoryButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        categoryButtons.setOpaque(false);
        JButton delCategoryBtn = new JButton("Delete Selected Category");
        delCategoryBtn.addActionListener(e -> deleteCategory());
        categoryButtons.add(delCategoryBtn);

        categoryPanel.add(new JScrollPane(categoryList), BorderLayout.CENTER);
        categoryPanel.add(categoryButtons, BorderLayout.SOUTH);

        // Question Panel
        JPanel questionPanel = new JPanel(new BorderLayout(5, 5));
        questionPanel.setOpaque(false);
        questionPanel.setBorder(createTitledBorder("Questions in Selected Category"));

        questionListModel = new DefaultListModel<>();
        questionList = new JList<>(questionListModel);
        questionList.setFont(Theme.getFont(Theme.FONT_BODY));

        JPanel questionButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        questionButtons.setOpaque(false);
        JButton delQuestionBtn = new JButton("Delete Selected Question");
        delQuestionBtn.addActionListener(e -> deleteQuestion());
        questionButtons.add(delQuestionBtn);

        questionPanel.add(new JScrollPane(questionList), BorderLayout.CENTER);
        questionPanel.add(questionButtons, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, categoryPanel, questionPanel);
        splitPane.setDividerLocation(200);
        contentPanel.add(splitPane, BorderLayout.CENTER);

        // Bottom panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);
        JButton closeBtn = new StyledButton("Close");
        closeBtn.addActionListener(e -> dispose());
        bottomPanel.add(closeBtn);
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);

        refreshData();
        add(contentPanel);
    }

    private TitledBorder createTitledBorder(String title) {
        return BorderFactory.createTitledBorder(
                null, title, TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION, Theme.getFont(Theme.FONT_BODY_BOLD)
        );
    }

    private void refreshData() {
        categoryListModel.clear();
        List<Category> categories = mainApp.getQuizService().getCategories();
        for (Category category : categories) {
            categoryListModel.addElement(category);
        }
        questionListModel.clear();
    }

    private void loadQuestionsForCategory() {
        questionListModel.clear();
        Category selectedCategory = categoryList.getSelectedValue();
        if (selectedCategory != null) {
            currentQuestions = mainApp.getQuizService().getQuestionsForCategory(selectedCategory, Integer.MAX_VALUE);
            for (Question q : currentQuestions) {
                questionListModel.addElement(q.getQuestionText());
            }
        }
    }

    private void deleteCategory() {
        Category selected = categoryList.getSelectedValue();
        if (selected == null) return;

        int confirm = JOptionPane.showConfirmDialog(this, "Delete category '" + selected.getName() + "' and all its questions?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            mainApp.getQuizService().deleteCategory(selected.getId());
            refreshData();
        }
    }

    private void deleteQuestion() {
        int selectedIndex = questionList.getSelectedIndex();
        if (selectedIndex == -1) return;

        int confirm = JOptionPane.showConfirmDialog(this, "Delete this question?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Question selectedQuestion = currentQuestions.get(selectedIndex);
            mainApp.getQuizService().deleteQuestion(selectedQuestion.getId());
            loadQuestionsForCategory();
        }
    }
}
