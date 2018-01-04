package ch.hepia.lovino.balldroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import ch.hepia.lovino.balldroid.controllers.GameController;
import ch.hepia.lovino.balldroid.models.Ball;
import ch.hepia.lovino.balldroid.models.DifficultyLevels;
import ch.hepia.lovino.balldroid.views.GameSurfaceView;

public class GameActivity extends Activity {
    private SensorManager sensorManager = null;
    private Sensor accelerometer = null;
    private final static String LOG_TAG = "GAME ACTIVITY";
    private GameController controller;
    private Ball ball;
    private DifficultyLevels difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        this.difficulty = DifficultyLevels.values()[intent.getIntExtra("DIFFICULTY", DifficultyLevels.EASY.ordinal())];
        this.controller = new GameController(this, this.difficulty);
        setContentView(this.controller.getView());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public void showEndOfGame(int score) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Game is over");
        builder.setMessage("Your score is " + score + " points.");
        builder.setCancelable(false);
        builder.setPositiveButton("View high scores", (dialogInterface, i) -> {
            startActivity(new Intent(this, HighScoreActivity.class));
            finish();
        });
        builder.setNegativeButton("Go back home", ((dialogInterface, i) -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }));
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Called after {@link #onRestoreInstanceState}, {@link #onRestart}, or
     * {@link #onPause}, for your activity to start interacting with the user.
     * This is a good place to begin animations, open exclusive-access devices
     * (such as the camera), etc.
     * <p>
     * <p>Keep in mind that onResume is not the best indicator that your activity
     * is visible to the user; a system window such as the keyguard may be in
     * front.  Use {@link #onWindowFocusChanged} to know for certain that your
     * activity is visible to the user (for example, to resume a game).
     * <p>
     * <p><em>Derived classes must call through to the super class's
     * implementation of this method.  If they do not, an exception will be
     * thrown.</em></p>
     *
     * @see #onRestoreInstanceState
     * @see #onRestart
     * @see #onPostResume
     * @see #onPause
     */
    @Override
    protected void onResume() {
        super.onResume();
        controller.resumeGame();
    }

    /**
     * Called as part of the activity lifecycle when an activity is going into
     * the background, but has not (yet) been killed.  The counterpart to
     * {@link #onResume}.
     * <p>
     * <p>When activity B is launched in front of activity A, this callback will
     * be invoked on A.  B will not be created until A's {@link #onPause} returns,
     * so be sure to not do anything lengthy here.
     * <p>
     * <p>This callback is mostly used for saving any persistent state the
     * activity is editing, to present a "edit in place" model to the user and
     * making sure nothing is lost if there are not enough resources to start
     * the new activity without first killing this one.  This is also a good
     * place to do things like stop animations and other things that consume a
     * noticeable amount of CPU in order to make the switch to the next activity
     * as fast as possible, or to close resources that are exclusive access
     * such as the camera.
     * <p>
     * <p>In situations where the system needs more memory it may kill paused
     * processes to reclaim resources.  Because of this, you should be sure
     * that all of your state is saved by the time you return from
     * this function.  In general {@link #onSaveInstanceState} is used to save
     * per-instance state in the activity and this method is used to store
     * global persistent data (in content providers, files, etc.)
     * <p>
     * <p>After receiving this call you will usually receive a following call
     * to {@link #onStop} (after the next activity has been resumed and
     * displayed), however in some cases there will be a direct call back to
     * {@link #onResume} without going through the stopped state.
     * <p>
     * <p><em>Derived classes must call through to the super class's
     * implementation of this method.  If they do not, an exception will be
     * thrown.</em></p>
     *
     * @see #onResume
     * @see #onSaveInstanceState
     * @see #onStop
     */
    @Override
    protected void onPause() {
        super.onPause();
        controller.pauseGame();
    }

    @Override
    public void onBackPressed() {
        if (controller.isPaused()) {
            controller.resumeGame();
        } else {
            controller.pauseGame();
            Toast.makeText(this, R.string.pause_text, Toast.LENGTH_LONG).show();
        }
    }
}
