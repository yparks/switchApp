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
 * FavoritesListFragment implements a list view which contains all of the users' favorite terms.
 *
 * @author Ashley Vo
 */
public class FavoritesListFragment extends ListFragment {
    private static final String TAG = "FavoritesListFragment";

    private SQLiteDatabase db;
    private Cursor cursor;
    private String term;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "ENTERING FAVORITES DISPLAY");

        super.onCreate(savedInstanceState);
        try {
            SQLiteOpenHelper dictionaryDatabaseHelper = new DictionaryDatabaseHelper(this.getContext());
            db = dictionaryDatabaseHelper.getReadableDatabase();
            cursor = db.query(
                    DictionaryDatabaseHelper.DICTIONARY_TABLE, //table to query
                    new String[]{DictionaryDatabaseHelper.ID_COL, DictionaryDatabaseHelper.TERM_COL},//columns to return
                    DictionaryDatabaseHelper.FAVORITES_COL + " = 1", // where favorites = 1
                    null,
                    null,
                    null,
                    null);
            Log.d(TAG, "onCreate(): cursor: " + cursor.getCount());
            CursorAdapter cursorAdapter =
                    new SimpleCursorAdapter(getContext(),
                            R.layout.fragment_favorites_list,
                            cursor,
                            new String[]{DictionaryDatabaseHelper.TERM_COL},
                            new int[]{R.id.favorite_text},
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
                DictionaryDatabaseHelper.DICTIONARY_TABLE,
                new String[]{DictionaryDatabaseHelper.TERM_COL},
                DictionaryDatabaseHelper.ID_COL + " = ?",
                new String[]{Long.toString(id)},
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
