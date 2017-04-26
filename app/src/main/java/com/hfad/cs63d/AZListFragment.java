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
    static final String DICTIONARY_TABLE = "dictionary";
    static final String CATEGORY_COL = "category";
    static final String ID_COL = "_id";

    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("AZListFragment", "onCreate(): " + savedInstanceState);
        super.onCreate(savedInstanceState);

        try {
            SQLiteOpenHelper dictionaryDatabaseHelper = new DictionaryDatabaseHelper(this.getContext());
            db = dictionaryDatabaseHelper.getReadableDatabase();
            cursor = db.query(true,
                    DICTIONARY_TABLE,
                    new String[]{ID_COL, CATEGORY_COL},
                    null,
                    null,
                    CATEGORY_COL,
                    null,
                    null,
                    null
            );
            Context context = getActivity();
            Log.d("AZListFragment", "onCreate(): context: " + context);
            Log.d("AZListFragment", "onCreate(): cursor: " + cursor.toString());
            CursorAdapter cursorAdapter = new SimpleCursorAdapter(context,
                    android.R.layout.simple_list_item_1,
                    cursor,
                    new String[]{ID_COL, CATEGORY_COL},
                    new int[]{android.R.id.text1},
                    0);
            Log.d("AZListFragment", "onCreate(): cursorAdapter: " + cursorAdapter.toString());
            setListAdapter(cursorAdapter);
        } catch (SQLiteException e) {
            Toast toast;
            toast = Toast.makeText(this.getContext(), "Database Unavailable", Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
