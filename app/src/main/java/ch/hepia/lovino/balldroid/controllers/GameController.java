package ch.hepia.lovino.balldroid.controllers;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.ArrayList;

import ch.hepia.lovino.balldroid.GameActivity;
import ch.hepia.lovino.balldroid.models.Ball;
import ch.hepia.lovino.balldroid.models.BallDirection;
import ch.hepia.lovino.balldroid.models.Bonus;
import ch.hepia.lovino.balldroid.models.DifficultyLevel;
import ch.hepia.lovino.balldroid.models.Game;
import ch.hepia.lovino.balldroid.models.Platform;
import ch.hepia.lovino.balldroid.models.PointArea;
import ch.hepia.lovino.balldroid.models.Score;
import ch.hepia.lovino.balldroid.models.Time;
import ch.hepia.lovino.balldroid.models.db.DBHelper;
import ch.hepia.lovino.balldroid.views.GameSurfaceView;

import static ch.hepia.lovino.balldroid.models.db.DBContract.ScoreEntry;

public class GameController {
    private static final int TIMER_SECONDS = 60;
    private GameActivity context;
    private DifficultyLevel difficulty;
    private GameSurfaceView view;
    private float xAccel = 0;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Game game;
    private Ball ball;
    private Score score;
    private Time time;
    private boolean paused = true;
    private TimerThread timer;
    private ArrayList<Bonus> bonusesToRemove;
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

    public GameController(GameActivity context, DifficultyLevel difficulty) {
        this.context = context;
        this.difficulty = difficulty;
        this.view = new GameSurfaceView(context, this);
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            this.accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        }
        this.bonusesToRemove = new ArrayList<>();
        this.timer = new TimerThread(10 * 1000, this);
    }

    public void updateBall() {
        if (paused) return;
        this.time.setTimeRemaining((int) timer.getRemainingTime() / 1000);
        this.ball.incrementSpeedX(xAccel);
        this.ball.incrementSpeedY();
        this.ball.updatePosition();
        if (this.ball.getX() > (this.view.getSurfaceWidth() - this.ball.getRadius())) {
            this.ball.setX(this.view.getSurfaceWidth() - this.ball.getRadius());
            this.ball.reboundX();
        }
        if (this.ball.getX() < this.ball.getRadius()) {
            this.ball.setX(this.ball.getRadius());
            this.ball.reboundX();
        }
        BallDirection direction = ball.getDirection();
        for (Platform p : this.game.getPlatforms()) {
            if (ball.getBoundingRect().intersect(p.getBoundingRect())) {
                switch (direction) {
                    case N:
                        reboundBottom(ball, p);
                        break;
                    case NE:
                        reboundBottom(ball, p);
                        reboundLeft(ball, p);
                        break;
                    case E:
                        reboundLeft(ball, p);
                        break;
                    case SE:
                        reboundTop(ball, p);
                        reboundLeft(ball, p);
                        break;
                    case S:
                        reboundTop(ball, p);
                        break;
                    case SW:
                        reboundTop(ball, p);
                        reboundRight(ball, p);
                        break;
                    case W:
                        reboundRight(ball, p);
                        break;
                    case NW:
                        reboundBottom(ball, p);
                        reboundRight(ball, p);
                        break;
                    case STILL:
                        break;
                }
            }
        }

        for (PointArea pointArea : this.game.getPointsAreas()) {
            if (ball.getBoundingRect().intersect(pointArea.getBoundingRect())) {
                score.increment(pointArea.getPoints());
                this.ball.putToStart();
            }
        }

        this.bonusesToRemove.forEach(game::removeBonus);
        this.bonusesToRemove.clear();

        for (Bonus bonus : this.game.getBonuses()) {
            if (ball.getBoundingRect().intersect(bonus.getBoundingRect())) {
                Log.v("BONUS", "Hit a bonus of " + bonus.getSeconds());
                bonusesToRemove.add(bonus);
                timer.addToTime(bonus.getSeconds());
            }
        }
    }

    private void reboundTop(Ball ball, Platform p) {
        if (Math.abs(this.ball.getY() - p.getBoundingRect().top) < ball.getRadius()) {
            this.ball.reboundY();
            this.ball.setY(p.getBoundingRect().top - ball.getRadius());
        }
    }

    private void reboundBottom(Ball ball, Platform p) {
        if (Math.abs(this.ball.getY() - p.getBoundingRect().bottom) < ball.getRadius()) {
            this.ball.reboundY();
            this.ball.setY(p.getBoundingRect().bottom + ball.getRadius());
        }
    }

    private void reboundLeft(Ball ball, Platform p) {
        if (Math.abs(this.ball.getX() - p.getBoundingRect().left) < ball.getRadius()) {
            this.ball.reboundX();
            this.ball.setX(p.getBoundingRect().left - ball.getRadius());
        }
    }

    private void reboundRight(Ball ball, Platform p) {
        if (Math.abs(this.ball.getX() - p.getBoundingRect().right) < ball.getRadius()) {
            this.ball.reboundX();
            this.ball.setX(p.getBoundingRect().right + ball.getRadius());
        }
    }

    public void start() {
        this.game = new Game(this.difficulty, 0, TIMER_SECONDS, this.view.getSurfaceWidth(), this.view.getSurfaceHeight());
        this.ball = game.getBall();
        this.score = game.getScore();
        this.time = game.getTime();
        this.timer = new TimerThread(this.time.getTimeRemaining() * 1000, this);
        this.timer.start();
    }

    public Game getGame() {
        return game;
    }

    public void resumeGame() {
        this.sensorManager.registerListener(this.sensorListener, this.accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        paused = false;
        if (this.time != null) {
            this.timer = new TimerThread(this.time.getTimeRemaining() * 1000, this);
            this.timer.start();
        }
    }

    public void pauseGame() {
        this.sensorManager.unregisterListener(this.sensorListener, this.accelerometer);
        paused = true;
        this.timer.stopTimer();
        try {
            this.timer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public GameSurfaceView getView() {
        return view;
    }

    public boolean isPaused() {
        return paused;
    }

    public void endGame() {
        Log.w("GAME", "Game is over");
        pauseGame();
        //if we join here, since this function is called from timer thread, we would be joining the thread running this
        try {
            this.timer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (score.getScore() > 0)
            saveScore();
        context.showEndOfGame(score.getScore());
    }

    private void saveScore() {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ScoreEntry.COLUMN_DIFFICULTY, difficulty.ordinal());
        values.put(ScoreEntry.COLUMN_SCORE, score.getScore());
        db.insert(ScoreEntry.TABLE_NAME, null, values);
    }
}
