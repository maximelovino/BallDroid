package ch.hepia.lovino.balldroid.models;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Score implements Drawable {
    private static final int VERTICAL_POSITION = 100;
    private int score;
    private final int xPosition;

    public Score(int xPosition, int score) {
        this.xPosition = xPosition;
        this.score = score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void increment(int value) {
        this.score += value;
    }

    public int getScore() {
        return score;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        paint.setTextSize(50);
        paint.setColor(Color.BLACK);
        canvas.drawText("Score " + String.valueOf(score), xPosition, VERTICAL_POSITION, paint);
    }
}
