package edu.mills.cs115;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Handles the intent that performs a term search. Displays a view containing a
 * term and a definition in a WebView format. Adds a term to the history database
 * when the user has viewed the term.
 *
 * @author barango
 * @author Roberto Amparán (mr.amparan@gmail.com)
 * @author Ashley Vo
 */
public class ResultActivity extends Activity {
    public static final String TERM_ON_CLICK = "";
    private static final String TAG = "ResultActivity";

    private WebView webDisplay;

    private int id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Entered onCreate()");
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Called super");
        setContentView(R.layout.search_result);
        Log.d(TAG, "called setContentView()");

        Intent intent = getIntent();

        Log.d(TAG, "onCreate(): getIntent() = " + intent.getExtras());
        Log.d(TAG, "Intent.ACTION_SEARCH.equals(intent.getAction()) = " + Intent.ACTION_SEARCH.equals(intent.getAction()));
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doTermSearch(query);
        } else {
            String term = intent.getStringExtra(TERM_ON_CLICK);
            Log.d(TAG, "onCreate(): intent.getStringExtra(TERM_ON_CLICK) = " + term);
            doTermSearch(term);
        }
    }

    /**
     * Queries the database for a particular word and displays the result in a WebView.
     * Calls the addTermToHistory method to add the term and definition into the database.
     *
     * @param query the term to query
     *
     * @author barango
     * @author Roberto Amparán (mr.amparan@gmail.com)
     * @author Ashley Vo
     */
    public void doTermSearch(String query) {
        Log.v(TAG, "Reached doTermSearch() with " + query);
        try{
            SQLiteOpenHelper dictionaryDatabaseHelper = new DictionaryDatabaseHelper(this);
            SQLiteDatabase db = dictionaryDatabaseHelper.getWritableDatabase();
            Cursor cursor = db.query(
                    DictionaryDatabaseHelper.DICTIONARY_TABLE,
                    new String[] {DictionaryDatabaseHelper.ID_COL, DictionaryDatabaseHelper.TERM_COL, DictionaryDatabaseHelper.DEFINITION_COL, DictionaryDatabaseHelper.FAVORITES_COL},
                    DictionaryDatabaseHelper.TERM_COL + " = ?",
                    new String[] {query},
                    null,
                    null,
                    null);
            Log.d(TAG, "doTermSearch() row count: " + cursor.getCount());
            if (cursor.moveToFirst()){
                Log.v(TAG, "cursor.moveToFirst()? " + cursor.moveToFirst());

                // Set id obtained from the cursor to a class variable so that it could be accessed
                //by onFavoriteClicked later.
                id = cursor.getInt(0);
                Log.v(TAG, "id: " + id);
                String term = cursor.getString(1);
                Log.v(TAG, "cursor move to second?");
                Log.v(TAG, "term: " + term);
                String definition = cursor.getString(2);
                Log.v(TAG, "cursor move to third?");
                Log.v(TAG, "definition: " + definition);
                boolean isFavorite = (cursor.getInt(3)==1);
                Log.v(TAG, "cursor move to third?");
                Log.v(TAG, "isFavorite: " + isFavorite);

                TextView termView = (TextView) findViewById(R.id.term);
                termView.setText(term);

                webDisplay = (WebView) findViewById(R.id.definition);
                webDisplay.getSettings().setJavaScriptEnabled(true);
                webDisplay.loadData(definition, "text/html", null);

                addTermToHistory(term, definition);
                Log.v(TAG, "added term");

                //Populate favorite checkbox
                CheckBox favorite = (CheckBox)findViewById(R.id.favorite);
                favorite.setChecked(isFavorite);
                Log.v(TAG, "declared checkBox");
            }else {
                Toast toast = Toast.makeText(this, "Term not found", Toast.LENGTH_SHORT);
                toast.show();
            }
            cursor.close();
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * Makes a call to the UpdateFavorite Async class, passing along the ID of the term.
     *
     * @author Ashley Vo
     */
    public void onFavoriteClicked (View view) {
        Log.v(TAG, "Entered onFavoriteClicked()");
        new UpdateFavoriteTask().execute(id);

        Log.v(TAG, "executed AsyncTask");
        Log.v(TAG, "Exited onFavoriteClicked");
    }

    /**
     * Updates the database when the checkbox is clicked.
     *
     * @author Ashley Vo
     */
    private class UpdateFavoriteTask extends AsyncTask<Integer, Void, Boolean> {
        ContentValues termValues;

        /**
         * Loads and store the values of the favorite checkbox.
         */
        protected void onPreExecute() {
            Log.v(TAG, "Entered onPreExecute()");
            CheckBox favorite = (CheckBox)findViewById(R.id.favorite);
            Log.v(TAG, "Declared favorite variable");
            termValues = new ContentValues();
            termValues.put(DictionaryDatabaseHelper.FAVORITES_COL, favorite.isChecked());
            Log.v(TAG, "Set termValues as ContentValues: " + termValues.toString());
        }
        /**
         * Update Dictionary table.
         *
         * @param terms array of term ID's
         */
        protected Boolean doInBackground(Integer... terms) {
            Log.v(TAG, "Entered doInBackground");
            int termNo = terms[0];
            Log.v(TAG, "Set termNo");
            SQLiteOpenHelper dictionaryDatabaseHelper = new DictionaryDatabaseHelper(ResultActivity.this);
            Log.v(TAG, "Created databaseHelper object");
            try {
                SQLiteDatabase db = dictionaryDatabaseHelper.getWritableDatabase();
                db.update(DictionaryDatabaseHelper.DICTIONARY_TABLE, termValues,
                        "_id = ?", new String[] {Integer.toString(termNo)});
                Log.v(TAG, "Created Database");
                return true;
            } catch(SQLiteException e) {
                return false;
            }
        }
        /**
         * Display message if database is unavailable.
         *
         * @param success success, if database is available
         */
        protected void onPostExecute(Boolean success) {
            Log.v(TAG, "Entered onPostExecute()");
            if (!success) {
                Toast toast = Toast.makeText(ResultActivity.this,
                        "Database unavailable", Toast.LENGTH_SHORT);
                toast.show();
            }
            Log.v(TAG, "Exited onPostExecute()");
        }
    }

    /**
     * Adds the term to the database.
     *
     * @param term the term from the query
     * @param definition the definition from the query
     * @return the row ID of the newly inserted row, or -1 if an error occurred
     *
     * @author Roberto Amparán (mr.amparan@gmail.com)
     */
    public long addTermToHistory(String term, String definition) {

        SQLiteOpenHelper historyDatabaseHelper = new HistoryDatabaseHelper(this);
        SQLiteDatabase db = historyDatabaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(HistoryDatabaseHelper.TERM_COL, term);
        contentValues.put(HistoryDatabaseHelper.DEFINITION_COL, definition);

        return db.insert(HistoryDatabaseHelper.DICTIONARY_TABLE, null, contentValues);
    }
}
