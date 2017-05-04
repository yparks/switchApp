package edu.mills.cs115;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


class HistoryDatabaseHelper extends SQLiteOpenHelper {
    private final String TAG = getClass().toString();
    private Context mHelperContext;
    public SQLiteDatabase sqlDatabase;
    int count = 0;

    private static final String DB_NAME = "dictionaryHistory";
    private static final int DB_VERSION = 1;

    //package private table and column names
    static final String DICTIONARY_TABLE = "history";
    static final String ID_COL = "_id";
    static final String TERM_COL = "term";
    static final String DEFINITION_COL = "definition";

    HistoryDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mHelperContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        sqlDatabase = db;
        updateMyDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);
    }

    //Add newVersion?
    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            db.execSQL("CREATE TABLE " + DICTIONARY_TABLE + " (" +
                    ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TERM_COL + " TEXT, " +
                    DEFINITION_COL + " TEXT)");
            Log.v(TAG, "in updateMyDatabase()");
        }
    }
}