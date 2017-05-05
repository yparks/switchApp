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
 * AZTermListFragment implements a ListFragment representation of all the terms categorized under a
 * selected category.
 *
 * @author barango
 */
public class AZTermListFragment extends ListFragment {
    private static final String TAG = "AZTermListFragment";
    private final static String GREENFOOT = "Greenfoot";
    private final static String OTHER = "other";
    private final static String STATEMENTS = "statements";
    private final static String KEYWORDS = "keywords";

    private SQLiteDatabase db;
    private Cursor cursor;
    private String selection;
    private String term;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "ENTERING CATERGORY -> TERM");
        Log.d(TAG, "CardView clicked: " + selection);
        super.onCreate(savedInstanceState);
        getCategoryTerms(selection);
    }

    /**
     * Gets all the terms filed under the selected category and displays them in a ListView.
     *
     * @param selection the category to be queried
     */
    private void getCategoryTerms(String selection) {
        try {
            SQLiteOpenHelper dictionaryDatabaseHelper = new DictionaryDatabaseHelper(this.getContext());
            db = dictionaryDatabaseHelper.getReadableDatabase();
            //query the database for the terms belonging to a category clicked
            cursor = db.query(
                    DictionaryDatabaseHelper.DICTIONARY_TABLE,
                    new String[]{DictionaryDatabaseHelper.ID_COL, DictionaryDatabaseHelper.TERM_COL},
                    DictionaryDatabaseHelper.CATEGORY_COL + " = ?",
                    new String[]{selection},
                    null,
                    null,
                    null);
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
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Log.d(TAG, "onListItemClick() long id = " + id);
        //query the database by the term clicked
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

    /**
     * Sets the string representation of a category by the position in which it was selected.
     *
     * @param position the position of the CardView that was clicked.
     */
    public void setCategory(int position) {
        switch (position) {
            case 0:
                selection = GREENFOOT;
                break;
            case 1:
                selection = KEYWORDS;
                break;
            case 2:
                selection = OTHER;
                break;
            case 3:
                selection = STATEMENTS;
                break;
        }
    }
}
