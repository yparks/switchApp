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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class AZListFragment extends ListFragment {
    private static final String TAG = "AZListFragment";

    private SQLiteDatabase db;
    private Cursor cursor;

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        Log.d(TAG, "onCreate(): " + savedInstanceState);
//        super.onCreate(savedInstanceState);
////        ListView listView = getListView();
//        try {
//            SQLiteOpenHelper dictionaryDatabaseHelper = new DictionaryDatabaseHelper(this.getContext());
//            db = dictionaryDatabaseHelper.getReadableDatabase();
//            cursor = db.query(true,
//                    DictionaryDatabaseHelper.DICTIONARY_TABLE,
//                    new String[]{DictionaryDatabaseHelper.ID_COL, DictionaryDatabaseHelper.CATEGORY_COL},
//                    null,
//                    null,
//                    DictionaryDatabaseHelper.CATEGORY_COL,
//                    null,
//                    null,
//                    null
//            );
//            Log.d(TAG, "onCreate(): cursor: " + cursor.getCount());
//            if(cursor.moveToFirst()){
//                while (!cursor.isAfterLast()) {
//                    String category = cursor.getString(1);
//                    Log.d(TAG, "CATEGORY " + category);
//                    cursor.moveToNext();
//                }
//            }
//            Context context = getActivity();
//            Log.d(TAG, "onCreate(): context: " + context);
//            CursorAdapter cursorAdapter = new SimpleCursorAdapter(context,
//                    android.R.layout.simple_list_item_1,
//                    cursor,
//                    new String[]{DictionaryDatabaseHelper.CATEGORY_COL},
//                    new int[]{android.R.id.text1},
//                    0);
//            Log.d(TAG, "onCreate(): cursorAdapter: " + cursorAdapter);
//            setListAdapter(cursorAdapter);
//        } catch (SQLiteException e) {
//            Toast toast;
//            toast = Toast.makeText(this.getContext(), "Database Unavailable", Toast.LENGTH_LONG);
//            toast.show();
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                inflater.getContext(),
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.categories));
        setListAdapter(adapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
