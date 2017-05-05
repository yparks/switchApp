package edu.mills.cs115;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 *  Implements the database for the history table.
 *
 *  @author Roberto Amparán (mr.amparan@gmail.com)
 */
class HistoryDatabaseHelper extends SQLiteOpenHelper {
    private final String TAG = getClass().toString();
    private Context context;
    private SQLiteDatabase sqlDatabase;

    private static final String DB_NAME = "cs_dictionary";
    private static final int DB_VERSION = 1;

    /**
     * Name of database table keeping track of the dictionary's history.
     */
    static final String DICTIONARY_TABLE = "history";

    /**
     * Name of column with unique row identifier in all tables.
     */
    static final String ID_COL = "_id";

    /**
     * Name of column in {@link #DICTIONARY_TABLE} with the term.
     */
    static final String TERM_COL = "term";

    /**
     * Name of column in {@link #DICTIONARY_TABLE} with the term's definition.
     */
    static final String DEFINITION_COL = "definition";

    /**
     * Default constructor.
     *
     * @param context the context
     */
    HistoryDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
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

    /**
     * Checks the current version of the database and also updates versions if necessary.
     *
     * @param db the SQLite database manager
     * @param oldVersion the previous version of the database
     * @param newVersion the new version of the database
     */
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