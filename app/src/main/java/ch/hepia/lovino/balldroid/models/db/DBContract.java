package ch.hepia.lovino.balldroid.models.db;


import android.provider.BaseColumns;

public class DBContract {
    public static final class ScoreEntry implements BaseColumns {
        public static final String TABLE_NAME = "t_scores";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_DIFFICULTY = "difficulty";
        public static final String COLUMN_SCORE = "score";
    }

}
