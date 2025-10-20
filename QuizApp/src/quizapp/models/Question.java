package quizapp.models;

import java.util.List;

public class Question {
    private final int id;
    private final String questionText;
    private final List<Option> options;
    private final int categoryId;

    public Question(int id, String questionText, List<Option> options, int categoryId) {
        this.id = id;
        this.questionText = questionText;
        this.options = options;
        this.categoryId = categoryId;
    }

    public int getId() {
        return id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public List<Option> getOptions() {
        return options;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public Option getCorrectOption() {
        for (Option option : options) {
            if (option.isCorrect()) {
                return option;
            }
        }
        return null;
    }
}