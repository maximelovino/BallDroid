package ch.hepia.lovino.balldroid.models;


import android.graphics.RectF;

public abstract class StaticObject extends Object {
    protected RectF boundingRect;
    private float width, height;

    public StaticObject(float x, float y, float width, float height) {
        super(x, y);
        this.width = width;
        this.height = height;

        this.boundingRect = new RectF(x, y, x + width, y + height);
    }

    public RectF getBoundingRect() {
        return new RectF(boundingRect);
    }
}
