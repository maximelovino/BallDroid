package ch.hepia.lovino.balldroid.models;


public class HighScore {
    private final int score;
    private final DifficultyLevel difficulty;

    public HighScore(int score, DifficultyLevel difficulty) {
        this.score = score;
        this.difficulty = difficulty;
    }

    @Override
    public String toString() {
        return String.valueOf(score) + " - " + difficulty.toString();
    }

    public int getScore() {
        return score;
    }

    public DifficultyLevel getDifficulty() {
        return difficulty;
    }
}
