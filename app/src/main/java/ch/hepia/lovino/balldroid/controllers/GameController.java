package ch.hepia.lovino.balldroid.controllers;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import ch.hepia.lovino.balldroid.models.DifficultyLevels;
import ch.hepia.lovino.balldroid.views.GameSurfaceView;

public class GameController {
    private Context context;
    private DifficultyLevels difficulty;
    private GameSurfaceView view;
    private float xAccel, yAccel = 0;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private final SensorEventListener sensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            xAccel = event.values[0];
            yAccel = event.values[1];
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
