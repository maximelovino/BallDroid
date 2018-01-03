package ch.hepia.lovino.balldroid.controllers;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.ArrayList;

import ch.hepia.lovino.balldroid.models.Ball;
import ch.hepia.lovino.balldroid.models.DifficultyLevels;
import ch.hepia.lovino.balldroid.models.Object;
import ch.hepia.lovino.balldroid.views.GameSurfaceView;

public class GameController {
    private Context context;
    private DifficultyLevels difficulty;
    private GameSurfaceView view;
    private float xAccel = 0;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private ArrayList<Object> objects;
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
        this.objects = new ArrayList<>();
        this.objects.add(ball);
    }

    public void updateBall() {
        this.ball.incrementSpeedX(xAccel);
        this.ball.incrementSpeedY();
        this.ball.updatePosition();
        //TODO handle collisions
    }

    public ArrayList<Object> getObjects() {
        return objects;
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
