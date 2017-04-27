package com.hfad.cs63d;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class ResultActivity extends Activity {


    private static final String TAG = "ResultActivity";

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
        Log.v(TAG, "Reached doTermSearch() with " + query);
        try{
            SQLiteOpenHelper dictionaryDatabaseHelper = new DictionaryDatabaseHelper(this);
            SQLiteDatabase db = dictionaryDatabaseHelper.getReadableDatabase();
            Cursor cursor = db.query(
                    DictionaryDatabaseHelper.DICTIONARY_TABLE,
                    new String[] {DictionaryDatabaseHelper.TERM_COL, DictionaryDatabaseHelper.DEFINITION_COL},
                    DictionaryDatabaseHelper.TERM_COL + " = ?",
                    new String[] {query},
                    null,
                    null,
                    null);
            Log.d(TAG, "doTermSearch() row count: " + cursor.getCount());
            if (cursor.moveToFirst()){
                Log.v(TAG, "cursor.moveToFirst()? " + cursor.moveToFirst());
                String term = cursor.getString(0);
                String definition = cursor.getString(1);
                //Term
                TextView termView = (TextView) findViewById(R.id.term);
                termView.setText(term);
                //Definition
                TextView definitionView = (TextView) findViewById(R.id.definition);
                definitionView.setText(definition);
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
}
