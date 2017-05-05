package edu.mills.cs115;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import android.os.AsyncTask;

public class ResultActivity extends Activity {
    public static final String EXTRA_TERMNO = "termNo";

    public static final String TERM_ON_CLICK = "";

    private static final String TAG = "ResultActivity";

    private WebView mWebView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);

        int termNo = (Integer)getIntent().getExtras().get(EXTRA_TERMNO);

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
                String term = cursor.getString(1);
                String definition = cursor.getString(2);
                boolean isFavorite = (cursor.getInt(3)==1);

//                //Term
//                TextView termView = (TextView) findViewById(R.id.term);
//                termView.setText(term);
//
//                //Definition
//                TextView definitionView = (TextView) findViewById(R.id.definition);
////                definitionView.setText(Html.fromHtml(definition));
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // for 24 api and more
//                    definitionView.setText(Html.fromHtml(definition, Html.FROM_HTML_MODE_LEGACY)); }
//                else {
//                    //for older api
//                    definitionView.setText(Html.fromHtml(definition));
////                    definitionView.setText(new HtmlSpanner().fromHtml(definition));
//
//                }
///
                //Term
                TextView termView = (TextView) findViewById(R.id.term);
                termView.setText(term);

                //Definition
                mWebView = (WebView) findViewById(R.id.definition);
                WebSettings webSettings = mWebView.getSettings();

                mWebView.loadData(definition, "text/html", null);

                //Add term and definition to database
                addTermToHistory(term, definition);
                Log.v(TAG, "added term");

                //Populate fav checkbox
                CheckBox favorite = (CheckBox)findViewById(R.id.favorite);
                favorite.setChecked(isFavorite);

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

    //Update database when checkbox is clicked
    public void onFavoriteClicked (View view) {
            int termNo = (Integer)getIntent().getExtras().get("termNo");
            new UpdateFavoriteTask().execute(termNo);
//        int termNo = (Integer) getIntent().getExtras().get("termNo");
//        CheckBox favorite = (CheckBox) findViewById(R.id.favorite);
//        ContentValues termValues = new ContentValues();
//        termValues.put(DictionaryDatabaseHelper.FAVORITES_COL, favorite.isChecked());
//        SQLiteOpenHelper dictionaryDatabaseHelper = new DictionaryDatabaseHelper(this);
//        SQLiteDatabase db = dictionaryDatabaseHelper.getWritableDatabase();
//        try {
//            db.update(DictionaryDatabaseHelper.DICTIONARY_TABLE, termValues, "=?", new String[]{
//                    Integer.toString(termNo)});
//            db.close();
//        } catch (SQLiteException e) {
//            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
//            toast.show();
//        }
    }

    private class UpdateFavoriteTask extends AsyncTask<Integer, Void, Boolean> {
        ContentValues termValues;
        protected void onPreExecute() {
            CheckBox favorite = (CheckBox)findViewById(R.id.favorite);
            termValues = new ContentValues(); termValues.put(DictionaryDatabaseHelper.FAVORITES_COL, favorite.isChecked());
        }
        protected Boolean doInBackground(Integer... terms) {
            int termNo = terms[0];
            SQLiteOpenHelper dictionaryDatabaseHelper = new DictionaryDatabaseHelper(ResultActivity.this);
            try {
                SQLiteDatabase db = dictionaryDatabaseHelper.getWritableDatabase();
                db.update(DictionaryDatabaseHelper.TERM_COL, termValues,
                        "_id = ?", new String[] {Integer.toString(termNo)});
                db.close();
                return true;
            } catch(SQLiteException e) {
                return false;
            }
        }
        protected void onPostExecute(Boolean success) {
            if (!success) {
                Toast toast = Toast.makeText(ResultActivity.this,
                        "Database unavailable", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    public long addTermToHistory(String term, String definition) {

        SQLiteOpenHelper historyDatabaseHelper = new HistoryDatabaseHelper(this);
        SQLiteDatabase db = historyDatabaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(HistoryDatabaseHelper.TERM_COL, term);
        contentValues.put(HistoryDatabaseHelper.DEFINITION_COL, definition);

        return db.insert(HistoryDatabaseHelper.DICTIONARY_TABLE, null, contentValues);
    }
}