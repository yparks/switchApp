package com.hfad.cs63d;

import android.annotation.TargetApi;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
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

public class AZCategoryMaterialList extends Fragment {
    private static final String TAG = "AZCategoryMaterialList";
    private AZTermList azTermList;
    private SQLiteDatabase db;
    private Cursor cursor;
    private String[] categories;


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.az_category_recycler,
                container, false);
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
            //rows expected? 4 - 'Greenfoot', 'other', 'statements', 'keywords'
            Log.d(TAG, "onCreate(): Number of rows returned by cursor: " + cursor.getCount());
            categories = new String[cursor.getCount()];
            Log.d(TAG, "onCreate(): cursor.moveToFirst()?: " + cursor.moveToFirst());
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


        CategoryAdapter categoryAdapter = new CategoryAdapter(categories);
        recyclerView.setAdapter(categoryAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        categoryAdapter.setListener(new CategoryAdapter.Listener() {
            @Override
            public void onClick(int position) {
                azTermList = new AZTermList();
                azTermList.setCategory(position);
                Log.d(TAG, "CATEGORY position: " + position);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.addToBackStack(null);
                transaction.replace(R.id.content_frame, azTermList).commit();
            }
        });
        return recyclerView;
    }
}
