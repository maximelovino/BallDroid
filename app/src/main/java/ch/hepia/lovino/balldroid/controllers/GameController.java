package ch.hepia.lovino.balldroid.controllers;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import ch.hepia.lovino.balldroid.models.Ball;
import ch.hepia.lovino.balldroid.models.DifficultyLevels;
import ch.hepia.lovino.balldroid.models.Game;
import ch.hepia.lovino.balldroid.models.Platform;
import ch.hepia.lovino.balldroid.views.GameSurfaceView;

public class GameController {
    private Context context;
    private DifficultyLevels difficulty;
    private GameSurfaceView view;
    private float xAccel = 0;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Game game;
    private Ball ball;
    private final SensorEventListener sensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            xAccel = -event.values[0];
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            //Do nothing for now
        }
    };

    public GameController(Context context, DifficultyLevels difficulty) {
        this.context = context;
        this.difficulty = difficulty;
        this.view = new GameSurfaceView(context, this);
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            this.accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        }
        this.ball = new Ball(this.difficulty);
    }

    public void updateBall() {
        this.ball.incrementSpeedX(xAccel);
        this.ball.incrementSpeedY();
        this.ball.updatePosition();
        //TODO handle collisions
        if (this.ball.getX() > (this.view.getSurfaceWidth() - this.ball.getRadius())) {
            this.ball.setX(this.view.getSurfaceWidth() - this.ball.getRadius());
            this.ball.reboundX();
        }
        if (this.ball.getX() < this.ball.getRadius()) {
            this.ball.setX(this.ball.getRadius());
            this.ball.reboundX();
        }
        if (this.ball.getY() > this.view.getSurfaceHeight()) {
            this.ball.putToStart();
        }

        for (Platform p : this.game.getPlatforms()) {
            if (ball.getBoundingRect().intersect(p.getBoundingRect())) {
                this.ball.reboundY();
                if (Math.abs(this.ball.getY() - p.getBoundingRect().bottom) < ball.getRadius()) {
                    this.ball.setY(p.getBoundingRect().bottom + ball.getRadius());
                } else if (Math.abs(this.ball.getY() - p.getBoundingRect().top) < ball.getRadius()) {
                    this.ball.setY(p.getBoundingRect().top - ball.getRadius());
                } else if (Math.abs(this.ball.getX() - p.getBoundingRect().left) < ball.getRadius()) {
                    this.ball.setX(p.getBoundingRect().left - ball.getRadius());
                } else if (Math.abs(this.ball.getX() - p.getBoundingRect().right) < ball.getRadius()) {
                    this.ball.setX(p.getBoundingRect().right + ball.getRadius());
                }
            }
        }
    }

    public void start() {
        this.game = new Game(this.ball, this.view.getSurfaceWidth(), this.view.getSurfaceHeight());
    }

    public Game getGame() {
        return game;
    }

    public void resumeGame() {
        this.sensorManager.registerListener(this.sensorListener, this.accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void pauseGame() {
        this.sensorManager.unregisterListener(this.sensorListener, this.accelerometer);
    }

    public GameSurfaceView getView() {
        return view;
    }
}