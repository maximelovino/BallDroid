package ch.hepia.lovino.balldroid.models;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class PointArea extends StaticGameObject {
    private final int points;
    private static final int TEXT_COLOR = Color.BLACK;
    private static final int BG_COLOR = Color.YELLOW;


    public PointArea(float x, float y, float width, float height, int points) {
        super(x, y, width, height);
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        paint.setTextSize(width / 2);
        paint.setColor(BG_COLOR);
        canvas.drawRect(this.getBoundingRect(), paint);
        paint.setColor(TEXT_COLOR);
        canvas.drawText(String.valueOf(points), x + width / 4, y + 3 * height / 4, paint);
    }
}
