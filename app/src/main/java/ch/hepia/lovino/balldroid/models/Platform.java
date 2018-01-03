package ch.hepia.lovino.balldroid.models;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Platform extends StaticObject {
    private final static int PLATFORM_COLOR = Color.RED;

    public Platform(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        paint.setColor(PLATFORM_COLOR);
        canvas.drawRect(this.boundingRect, paint);
    }
}
