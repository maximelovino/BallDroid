package ch.hepia.lovino.balldroid.models;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class Ball extends Object {
    private static final int BALL_COLOR = Color.BLACK;
    private float speedX, speedY;
    private float maxSpeed;
    private int gravity;
    private final float radius = 6.67f;
    private static final float DRAG_FORCE = 10.0f;
    private static final float DRAG_FORCE_REBOUND = 2f;
    private static final float INIT_X = 100;
    private static final float INIT_Y = 100;
    private static final float MAX_SPEED_SLOW = 15.0f;
    private static final float MAX_SPEED_MEDIUM = 25.0f;
    private static final float MAX_SPEED_FAST = 45.0f;
    private static final int GRAVITY_EASY = 6;
    private static final int GRAVITY_MEDIUM = 9;
    private static final int GRAVITY_HARD = 15;


    public Ball(DifficultyLevels difficulty) {
        super(INIT_X, INIT_Y);
        switch (difficulty) {
            case EASY:
                this.maxSpeed = MAX_SPEED_SLOW;
                this.gravity = GRAVITY_EASY;
                break;
            case MEDIUM:
                this.maxSpeed = MAX_SPEED_MEDIUM;
                this.gravity = GRAVITY_MEDIUM;
                break;
            case HARD:
                this.maxSpeed = MAX_SPEED_FAST;
                this.gravity = GRAVITY_HARD;
                break;
        }
        this.speedY = 0;
        this.speedX = 0;

    }

    public float getRadius() {
        return radius;
    }

    public void setX(float newX) {
        this.x = newX;
    }

    public void setY(float newY) {
        this.y = newY;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        paint.setColor(BALL_COLOR);
        canvas.drawCircle(x, y, radius, paint);
    }

    public void reboundX() {
        this.speedX *= -1;
        if (this.speedX < 0) {
            this.speedX += DRAG_FORCE_REBOUND;
        } else {
            this.speedX -= DRAG_FORCE_REBOUND;
        }
    }

    public void reboundY() {
        this.speedY *= -1;
        if (this.speedY < 0) {
            this.speedY += DRAG_FORCE_REBOUND;
        } else {
            this.speedX -= DRAG_FORCE_REBOUND;
        }
    }

    public void putToStart() {
        this.x = INIT_X;
        this.y = INIT_Y;
        this.speedX = 0;
        this.speedY = 0;
    }

    public void incrementSpeedX(float toAdd) {
        this.speedX = Math.min(this.maxSpeed, this.speedX + toAdd / DRAG_FORCE);
    }

    public void incrementSpeedY() {
        this.speedY = Math.min(this.maxSpeed, this.speedY + this.gravity / DRAG_FORCE);
    }

    public void updatePosition() {
        this.x += speedX;
        this.y += speedY;
    }

    public RectF getBoundingRect() {
        return new RectF(x - radius, y - radius, x + radius, y + radius);
    }
}
