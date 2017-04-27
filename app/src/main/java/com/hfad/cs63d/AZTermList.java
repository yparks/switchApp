package com.hfad.cs63d;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class AZTermList extends ListFragment{
    private static final String TAG = "AZTermList";

    private SQLiteDatabase db;
    private Cursor cursor;
    private String category;
    private String term;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "ENTERING TERM DISPLAY");
        Log.d(TAG, "Keyword clicked: " + category);
        super.onCreate(savedInstanceState);
        try {
            SQLiteOpenHelper dictionaryDatabaseHelper = new DictionaryDatabaseHelper(this.getContext());
            db = dictionaryDatabaseHelper.getReadableDatabase();
            cursor = db.query(
                    DictionaryDatabaseHelper.DICTIONARY_TABLE,
                    new String[]{DictionaryDatabaseHelper.ID_COL, DictionaryDatabaseHelper.TERM_COL},
                    DictionaryDatabaseHelper.CATEGORY_COL + " = ?",
                    new String[] {category},
                    null,
                    null,
                    null);
            Log.d(TAG, "onCreate(): cursor: " + cursor.getCount());
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    int row = cursor.getInt(0);
                    String result = cursor.getString(1);
                    Log.d(TAG, "Term " + result);
                    Log.d(TAG, "Term row" + row);
                    cursor.moveToNext();
                }
            }
            Log.d(TAG, "onCreate(): context: " + getContext());
            CursorAdapter cursorAdapter = new SimpleCursorAdapter(getContext(),
                    R.layout.az_term,
                    cursor,
                    new String[]{DictionaryDatabaseHelper.TERM_COL},
                    new int[]{R.id.term_text},
                    0);
            Log.d(TAG, "onCreate(): cursorAdapter: " + cursorAdapter);
            setListAdapter(cursorAdapter);
        } catch (SQLiteException e) {
            Toast toast;
            toast = Toast.makeText(this.getContext(), "Database Unavailable", Toast.LENGTH_LONG);
            toast.show();
        }

    }

    private ResultActivity resultActivity;

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l,v,position,id);
        Log.d(TAG, "onListItemClick() long id = " + id);

        Toast toast = Toast.makeText(this.getContext(), "testing", Toast.LENGTH_LONG);
        toast.show();
        resultActivity = new ResultActivity();
        cursor = db.query(
                DictionaryDatabaseHelper.DICTIONARY_TABLE,
                new String[]{DictionaryDatabaseHelper.TERM_COL},
                DictionaryDatabaseHelper.ID_COL + " = ?",
                new String[] {Long.toString(id)},
                null,
                null,
                null);
        if (cursor.moveToFirst()) {
            term = cursor.getString(0);
            Log.v(TAG, "id to term: " + term);
        }
        resultActivity.doTermSearch(term);
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.replace(R.id.content_frame, resultActivity).commit();
    }


    public void setCategory(long id) {
        int position = (int) id;
        switch (position){
            case 97: category = "keywords";
                break;
            case 55: category = "Greenfoot";
                break;
            case 96: category = "null";
                break;
            case 98: category = "statements";
                break;
        }
    }
}
