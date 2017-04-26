package com.hfad.cs63d;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.widget.Toast;


public class AZListFragment extends ListFragment {
    private static final String TAG = "AZListFragment";

    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
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
            Context context = getActivity();
            Log.d(TAG, "onCreate(): context: " + context);
            Log.d(TAG, "onCreate(): cursor: " + cursor.getCount());
            CursorAdapter cursorAdapter = new SimpleCursorAdapter(context,
                    android.R.layout.simple_list_item_1,
                    cursor,
                    new String[]{DictionaryDatabaseHelper.CATEGORY_COL},
                    new int[]{android.R.id.text1},
                    0);
            Log.d(TAG, "onCreate(): cursorAdapter: " + cursorAdapter);
            this.setListAdapter(cursorAdapter);
        } catch (SQLiteException e) {
            Toast toast;
            toast = Toast.makeText(this.getContext(), "Database Unavailable", Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
