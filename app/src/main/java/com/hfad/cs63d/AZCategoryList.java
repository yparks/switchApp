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


public class AZCategoryList extends ListFragment {
    private AZTermList listFragment;
    private FragmentManager fragmentManager;
    private static final String TAG = "AZCategoryList";

    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        fragmentManager = getFragmentManager();
        Log.d(TAG, "onCreate(): " + savedInstanceState);
        super.onCreate(savedInstanceState);
        try {
            SQLiteOpenHelper dictionaryDatabaseHelper = new DictionaryDatabaseHelper(this.getContext());
            db = dictionaryDatabaseHelper.getReadableDatabase();
            cursor = db.query(true,
                    DictionaryDatabaseHelper.DICTIONARY_TABLE,
                    new String[]{DictionaryDatabaseHelper.ID_COL, DictionaryDatabaseHelper.CATEGORY_COL},
                    null,
                    null,
                    DictionaryDatabaseHelper.CATEGORY_COL,
                    null,
                    null,
                    null
            );
            Log.d(TAG, "onCreate(): cursor: " + cursor.getCount());
            if(cursor.moveToFirst()){
                while (!cursor.isAfterLast()) {
                    String category = cursor.getString(1);
                    Log.d(TAG, "CATEGORY " + category);
                    cursor.moveToNext();
                }
            }
            Log.d(TAG, "onCreate(): context: " + getContext());
            CursorAdapter cursorAdapter = new SimpleCursorAdapter(getContext(),
                    R.layout.az_category,
                    cursor,
                    new String[]{DictionaryDatabaseHelper.CATEGORY_COL},
                    new int[]{R.id.cat_text},
                    0);
            Log.d(TAG, "onCreate(): cursorAdapter: " + cursorAdapter);
            setListAdapter(cursorAdapter);
        } catch (SQLiteException e) {
            Toast toast;
            toast = Toast.makeText(this.getContext(), "Database Unavailable", Toast.LENGTH_LONG);
            toast.show();
        }

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l,v,position,id);
        Log.d(TAG, "onListItemClick() long id = " + id);
        listFragment = new AZTermList();
        listFragment.setCategory(id);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.addToBackStack(null);
        transaction.replace(R.id.content_frame, listFragment).commit();
    }
}
