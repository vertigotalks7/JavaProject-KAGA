package quizapp.models;

public class Option {
    private final String optionText;
    private final boolean isCorrect;

    public Option(String optionText, boolean isCorrect) {
        this.optionText = optionText;
        this.isCorrect = isCorrect;
    }

    public String getOptionText() { return optionText; }
    public boolean isCorrect() { return isCorrect; }
}