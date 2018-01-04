package ch.hepia.lovino.balldroid.models;


public abstract class Object implements Drawable {
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
}
