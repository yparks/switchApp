package edu.mills.cs115;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Implements a ListFragment, which displays the contents from the history table
 * as a list. The ListFragment comprises terms the user has seen. It is listed in
 * descending order, it displays only distinct values, and it sets a limit of fifty.
 *
 * @author Roberto Amparán (mr.amparan@gmail.com)
 */
public class HistoryListFragment extends ListFragment{
    private static final String TAG = "HistoryListFragment";

    private SQLiteDatabase db;
    private Cursor cursor;
    private String term;
    private static final String LIST_LIMIT = "50";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "ENTERING TERM DISPLAY");

        super.onCreate(savedInstanceState);
        try {
            SQLiteOpenHelper historyDatabaseHelper = new HistoryDatabaseHelper(this.getContext());
            db = historyDatabaseHelper.getReadableDatabase();
            cursor = db.query(
                    true, //select distinct values
                    HistoryDatabaseHelper.DICTIONARY_TABLE, //table to query
                    new String[]{HistoryDatabaseHelper.ID_COL, HistoryDatabaseHelper.TERM_COL}, //columns to return
                    null,
                    null,
                    HistoryDatabaseHelper.TERM_COL, //group by term
                    null,
                    HistoryDatabaseHelper.ID_COL + " DESC", //specify descending sort order
                    LIST_LIMIT); //set a limit of 50 terms

            Log.d(TAG, "onCreate(): cursor: " + cursor.getCount());

            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    int row = cursor.getInt(0);
                    String result = cursor.getString(1);
                    Log.d(TAG, "Term " + result);
                    Log.d(TAG, "Term row: " + row);
                    cursor.moveToNext();
                }
            }
            Log.d(TAG, "onCreate(): context: " + getContext());
            CursorAdapter cursorAdapter = new SimpleCursorAdapter(getContext(),
                    R.layout.history,
                    cursor,
                    new String[]{HistoryDatabaseHelper.TERM_COL},
                    new int[]{R.id.history_text},
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
        super.onListItemClick(l, v, position, id);
        Log.d(TAG, "onListItemClick() long id = " + id);

        Toast toast = Toast.makeText(this.getContext(), "testing", Toast.LENGTH_LONG);
        toast.show();

        cursor = db.query(
                HistoryDatabaseHelper.DICTIONARY_TABLE,
                new String[]{HistoryDatabaseHelper.TERM_COL},
                HistoryDatabaseHelper.ID_COL + " = ?",
                new String[] {Long.toString(id)},
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            term = cursor.getString(0);
            Log.v(TAG, "id to term: " + term);
        }

        Intent intent = new Intent(getActivity(), ResultActivity.class);
        intent.putExtra(ResultActivity.TERM_ON_CLICK, term);
        getActivity().startActivity(intent);
    }
}