package ch.hepia.lovino.balldroid.models;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class Ball {
    private float x, y;
    private float speedX, speedY;
    private float maxSpeed;
    private final float radius = 6.67f;
    private static final float COMPENSATOR = 10.0f;
    private static final float COMPENSATOR_REBOUND = 2f;
    private static final float INIT_X = 100;
    private static final float INIT_Y = 100;
    private static final float MAX_SPEED_SLOW = 15.0f;
    private static final float MAX_SPEED_MEDIUM = 25.0f;
    private static final float MAX_SPEED_FAST = 45.0f;

    public Ball(DifficultyLevels difficulty) {
        this.x = INIT_X;
        this.y = INIT_Y;
        switch (difficulty) {
            case EASY:
                this.maxSpeed = MAX_SPEED_SLOW;
                break;
            case MEDIUM:
                this.maxSpeed = MAX_SPEED_MEDIUM;
                break;
            case HARD:
                this.maxSpeed = MAX_SPEED_FAST;
                break;
        }
        this.speedY = 0;
        this.speedX = 0;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getSpeedX() {
        return speedX;
    }

    public void setSpeedX(float speedX) {
        this.speedX = speedX;
    }

    public float getSpeedY() {
        return speedY;
    }

    public void setSpeedY(float speedY) {
        this.speedY = speedY;
    }

    public RectF getBoundingRect() {
        return new RectF(x - radius, y - radius, x + radius, y + radius);
    }
}
