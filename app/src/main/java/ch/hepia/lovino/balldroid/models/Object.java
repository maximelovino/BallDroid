package ch.hepia.lovino.balldroid.models;


import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class Object {
    private int x;
    private int y;

    public Object(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    abstract public void draw(Canvas canvas, Paint paint);
}
