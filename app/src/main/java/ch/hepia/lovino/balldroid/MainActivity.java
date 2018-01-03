package ch.hepia.lovino.balldroid;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import ch.hepia.lovino.balldroid.models.DifficultyLevels;

public class MainActivity extends Activity {
    private Button gameButton;
    private Button highScoreButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        highScoreButton = findViewById(R.id.high_score_button);
        gameButton = findViewById(R.id.start_button);

        highScoreButton.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), HighScoreActivity.class);
            startActivity(intent);
        });

        gameButton.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), GameActivity.class).putExtra("DIFFICULTY", getSharedPreferences("GAME", MODE_PRIVATE).getInt("DIFFICULTY", DifficultyLevels.EASY.ordinal()));
            startActivity(intent);
        });

    }

    /**
     * Initialize the contents of the Activity's standard options menu.  You
     * should place your menu items in to <var>menu</var>.
     * <p>
     * <p>This is only called once, the first time the options menu is
     * displayed.  To update the menu every time it is displayed, see
     * {@link #onPrepareOptionsMenu}.
     * <p>
     * <p>The default implementation populates the menu with standard system
     * menu items.  These are placed in the {@link Menu#CATEGORY_SYSTEM} group so that
     * they will be correctly ordered with application-defined menu items.
     * Deriving classes should always call through to the base implementation.
     * <p>
     * <p>You can safely hold on to <var>menu</var> (and any items created
     * from it), making modifications to it as desired, until the next
     * time onCreateOptionsMenu() is called.
     * <p>
     * <p>When you add items to the menu, you can implement the Activity's
     * {@link #onOptionsItemSelected} method to handle them there.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed;
     * if you return false it will not be shown.
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("GAME", MODE_PRIVATE);
        int diffValue = prefs.getInt("DIFFICULTY", -1);
        if (diffValue != -1 && diffValue < 3) {
            Log.v("DIFFICULTY MENU", String.valueOf(diffValue));
            DifficultyLevels diff = DifficultyLevels.values()[diffValue];
            switch (diff) {
                case EASY:
                    menu.findItem(R.id.easy_difficulty_button).setChecked(true);
                    break;
                case MEDIUM:
                    menu.findItem(R.id.medium_difficulty_button).setChecked(true);
                    break;
                case HARD:
                    menu.findItem(R.id.hard_difficulty_button).setChecked(true);
                    break;
            }
        } else {
            menu.findItem(R.id.easy_difficulty_button).setChecked(true);
            prefs.edit().putInt("DIFFICULTY", DifficultyLevels.EASY.ordinal()).apply();
        }
        return true;
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     * <p>
     * <p>Derived classes should call through to the base class for it to
     * perform the default menu handling.</p>
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String toastText = null;

        int levelToPut = -1;
        switch (item.getItemId()) {
            case R.id.easy_difficulty_button:
                levelToPut = DifficultyLevels.EASY.ordinal();
                toastText = "Easy mode selected";
                break;
            case R.id.medium_difficulty_button:
                levelToPut = DifficultyLevels.MEDIUM.ordinal();
                toastText = "Medium mode selected";
                break;
            case R.id.hard_difficulty_button:
                levelToPut = DifficultyLevels.HARD.ordinal();
                toastText = "Hard mode selected";
                break;
            case R.id.about_button:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        if (levelToPut != -1) {
            item.setChecked(true);
            this.getSharedPreferences("GAME", MODE_PRIVATE).edit().putInt("DIFFICULTY", levelToPut).apply();
            Toast.makeText(this, toastText, Toast.LENGTH_LONG).show();
        }


        return true;
    }
}
