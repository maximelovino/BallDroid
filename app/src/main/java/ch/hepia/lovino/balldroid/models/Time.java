package ch.hepia.lovino.balldroid.models;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Time implements Drawable {
    private int timeRemaining;
    private static final int VERTICAL_POSITION = 175;
    private final int xPosition;

    public Time(int xPosition, int timeRemaining) {
        this.xPosition = xPosition;
        this.timeRemaining = timeRemaining;
    }

    public void setTimeRemaining(int timeRemaining) {
        this.timeRemaining = timeRemaining;
    }

    public int getTimeRemaining() {
        return timeRemaining;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        paint.setTextSize(50);
        paint.setColor(Color.BLACK);
        canvas.drawText("Time " + String.valueOf(timeRemaining), xPosition, VERTICAL_POSITION, paint);
    }
}
