package com.hfad.cs63d;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class ResultActivity extends Activity {
    private SQLiteDatabase db;
    private Cursor cursor;

    static final String DICTIONARY_TABLE = "dictionary";
    static final String ID_COL = "_id";
    static final String TERM_COL = "term";
    static final String DEFINITION_COL = "definition";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);



        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doTermSearch(query);
        }
    }

    public void doTermSearch(String query) {
        Log.v("ResultActivity.java", "Reached doTermSearch with " + query);
        try{
            SQLiteOpenHelper dictionaryDatabaseHelper = new DictionaryDatabaseHelper(this);
            db = dictionaryDatabaseHelper.getReadableDatabase();
            Cursor cursor = db.query(DICTIONARY_TABLE,
                    new String[] {TERM_COL, DEFINITION_COL},
                    "term = ?",
                    new String[] {query},
                    null,
                    null,
                    null);
            Log.v("ResultActivity.java", "in doTermSearch() " + cursor);
            if (cursor.moveToFirst()){
                Log.v("ResultActivity.java", "in doTermSearch() " + cursor.moveToFirst());
                String term = cursor.getString(0);
                String definition = cursor.getString(1);

                TextView termView = (TextView) findViewById(R.id.term);
                termView.setText(term);

                TextView definitionView = (TextView) findViewById(R.id.definition);
                definitionView.setText(definition);
            }else {
                Toast toast = Toast.makeText(this, "Term not found", Toast.LENGTH_SHORT);
                toast.show();
            }

        }catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        // MenuInflater inflater = getMenuInflater();
        // inflater.inflate(R.menu.options_menu, menu);
        getMenuInflater().inflate(R.menu.search_menu, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        ComponentName cn = new ComponentName(this, ResultActivity.class);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(cn));

        return true;
    }
}
