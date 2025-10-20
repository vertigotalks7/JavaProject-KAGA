package quizapp.models;

public class LeaderboardEntry {
    private final String username;
    private final String categoryName;
    private final int highScore;

    public LeaderboardEntry(String username, String categoryName, int highScore) {
        this.username = username;
        this.categoryName = categoryName;
        this.highScore = highScore;
    }

    public String getUsername() {
        return username;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getHighScore() {
        return highScore;
    }
}