package ch.hepia.lovino.balldroid.models;


import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class Object {
    protected float x;
    protected float y;

    public Object(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    abstract public void draw(Canvas canvas, Paint paint);
}
