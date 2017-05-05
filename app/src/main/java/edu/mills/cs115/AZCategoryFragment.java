package edu.mills.cs115;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * AZCategoryFragment implements the view for categories within the dictionary database. Categories are
 * displayed in a recycler view containing card views.
 *
 * @author barango
 */
public class AZCategoryFragment extends Fragment {
    private static final String TAG = "AZCategoryFragment";
    private AZTermListFragment azTermListFragment;
    private SQLiteDatabase db;
    private Cursor cursor;
    private String[] categories;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        recyclerView = (RecyclerView) inflater.inflate(edu.mills.cs115.R.layout.az_category_recycler,
                container, false);
        retrieveCategories();
        createCategoryAdapter();
        return recyclerView;
    }

    /**
     * Queries the database for distinct category values and saves them to a list.
     */
    private void retrieveCategories() {
        try {
            SQLiteOpenHelper dictionaryDatabaseHelper = new DictionaryDatabaseHelper(this.getContext());
            db = dictionaryDatabaseHelper.getReadableDatabase();
            cursor = db.query(true, //select district categories
                    DictionaryDatabaseHelper.DICTIONARY_TABLE, //table to query
                    new String[]{DictionaryDatabaseHelper.ID_COL,
                            DictionaryDatabaseHelper.CATEGORY_COL},//columns to return
                    null,
                    null,
                    DictionaryDatabaseHelper.CATEGORY_COL, //group by category
                    null,
                    null,
                    null
            );
            categories = new String[cursor.getCount()];
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    for (int i = 0; i < categories.length; i++) {
                        categories[i] = cursor.getString(1);
                        cursor.moveToNext();
                    }
                }
                Log.d(TAG, "onCreate(): categories[i].length = " + categories.length);
            }
        } catch (SQLiteException e) {
            Toast toast;
            toast = Toast.makeText(this.getContext(), "Database Unavailable", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    /**
     * Creates the recycler view adapter for displaying category card views.
     */
    public void createCategoryAdapter(){
        AZCategoryAdapter azCategoryAdapter = new AZCategoryAdapter(categories);
        recyclerView.setAdapter(azCategoryAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        azCategoryAdapter.setListener(new AZCategoryAdapter.Listener() {
            @Override
            public void onClick(int position) {
                azTermListFragment = new AZTermListFragment();
                azTermListFragment.setCategory(position);
                Log.d(TAG, "CATEGORY position: " + position);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.addToBackStack(null);
                transaction.replace(R.id.content_frame, azTermListFragment).commit();
            }
        });
    }
}
