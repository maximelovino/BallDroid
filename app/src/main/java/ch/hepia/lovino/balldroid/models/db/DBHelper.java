package ch.hepia.lovino.balldroid.models.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static ch.hepia.lovino.balldroid.models.db.DBContract.*;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "BallDroid.db";
    private static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String createHighScoreTable = "CREATE TABLE " + ScoreEntry.TABLE_NAME + " ("
                + ScoreEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ScoreEntry.COLUMN_DIFFICULTY + " INTEGER NOT NULL,"
                + ScoreEntry.COLUMN_SCORE + " INTEGER NOT NULL" +
                ");";
        sqLiteDatabase.execSQL(createHighScoreTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void refresh(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + ScoreEntry.TABLE_NAME);
        onCreate(db);
    }
}
