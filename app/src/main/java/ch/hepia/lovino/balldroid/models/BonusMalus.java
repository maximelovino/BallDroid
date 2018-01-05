package ch.hepia.lovino.balldroid.models;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class BonusMalus extends StaticObject {
    private int seconds;
    private static final int COLOR_BONUS = Color.GREEN;
    private static final int COLOR_MALUS = Color.RED;

    public BonusMalus(float x, float y, float width, float height, int seconds) {
        super(x, y, width, height);
        this.seconds = seconds;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        paint.setColor(seconds < 0 ? COLOR_MALUS : COLOR_BONUS);
        canvas.drawRect(this.getBoundingRect(), paint);
    }

    public int getSeconds() {
        return seconds;
    }
}
