package edu.mills.cs115;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

public class MainActivity extends AppCompatActivity {
    private ListFragment listFragment;
    private Fragment HomeFragment;
    private Fragment materialFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private final String TAG = "MainActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_home:
                                Log.d("MainActivity", "onNavigationItemSelected() " + item);
                                transaction = fragmentManager.beginTransaction();
                                HomeFragment = new HomeFragment();
                                transaction.replace(R.id.content_frame, HomeFragment);
                                transaction.addToBackStack(null);
                                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                transaction.commit();
                                //add your fragment here
                                break;
                            case R.id.action_history:
                                //Set list fragment as instance of history class
                                Log.d("MainActivity", "onNavigationItemSelected() " + item);
                                transaction = fragmentManager.beginTransaction();
                                listFragment = new HistoryListFragment();
                                transaction.replace(R.id.content_frame, listFragment);
                                transaction.addToBackStack(null);
                                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                transaction.commit();
                                break;
                            case R.id.action_favorites:
                                //add your fragment here
                                break;
                            case R.id.action_az:
                                Log.d("MainActivity", "onNavigationItemSelected() " + item);
                                transaction = fragmentManager.beginTransaction();
                                materialFragment = new AZCategoryFragment();
                                transaction.replace(R.id.content_frame, materialFragment);
                                transaction.addToBackStack(null);
                                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                transaction.commit();
                                break;
                        }
                        return true;
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        getMenuInflater().inflate(R.menu.search_menu, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        ComponentName cn = new ComponentName(this, ResultActivity.class);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(cn));

        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
//            doSearch(query);
        }
    }
}