package com.hfad.cs63d;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView textHome;
        private TextView textHistory;
    private TextView textFavorites;
    private TextView textAZ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textHome = (TextView) findViewById(R.id.text_home);
        textHistory = (TextView) findViewById(R.id.text_history);
        textFavorites = (TextView) findViewById(R.id.text_favorites);
        textAZ = (TextView) findViewById(R.id.text_az);


        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_home:
                                textHome.setVisibility(View.VISIBLE);
                                textHistory.setVisibility(View.GONE);
                                textFavorites.setVisibility(View.GONE);
                                textAZ.setVisibility(View.GONE);
                                break;
                            case R.id.action_history:
                                textHome.setVisibility(View.GONE);
                                textHistory.setVisibility(View.VISIBLE);
                                textFavorites.setVisibility(View.GONE);
                                textAZ.setVisibility(View.GONE);
                                break;
                            case R.id.action_favorites:
                                textHome.setVisibility(View.GONE);
                                textHistory.setVisibility(View.GONE);
                                textFavorites.setVisibility(View.VISIBLE);
                                textAZ.setVisibility(View.GONE);
                                break;
                            case R.id.action_az:
                                textHome.setVisibility(View.GONE);
                                textHistory.setVisibility(View.GONE);
                                textFavorites.setVisibility(View.GONE);
                                textAZ.setVisibility(View.VISIBLE);
                                break;
                        }
                        return false;
                    }
                });
    }
    //silly comment
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        // MenuInflater inflater = getMenuInflater();
        // inflater.inflate(R.menu.options_menu, menu);
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

}//Test