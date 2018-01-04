package ch.hepia.lovino.balldroid.models;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Time implements Drawable {
    private int timeRemaining;

    public Time(int timeRemaining) {
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
        //TODO hardcoded position is not good
        paint.setTextSize(50);
        paint.setColor(Color.BLACK);
        canvas.drawText("Time " + String.valueOf(timeRemaining), 1000, 175, paint);
    }
}
