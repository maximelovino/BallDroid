package ch.hepia.lovino.balldroid.models;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Score implements Drawable {
    private int score;

    public Score(int score) {
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
        //TODO hardcoded position is not good
        paint.setTextSize(50);
        paint.setColor(Color.BLACK);
        canvas.drawText("Score " + String.valueOf(score), 1000, 100, paint);
    }
}
