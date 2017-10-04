package com.example.android.techeval;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.android.techeval.Fragments.ScenarioOneFragment;
import com.example.android.techeval.Fragments.ScenarioTwoFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final static String CURRENT_NAVI_ID = "CurrentNaviId";

    private int mCurrentNaviId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        int navigationId = -1;
        if (savedInstanceState != null) {
            navigationId = savedInstanceState.getInt(CURRENT_NAVI_ID);
        }

        if (navigationId == -1) {
            // show the default fragment when the App is launched
            navigationView.setCheckedItem(R.id.nav_scenario_one);
            showScenarioOne();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Put the current navigation id
        outState.putInt(CURRENT_NAVI_ID, mCurrentNaviId);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_scenario_one) {
            showScenarioOne();
        } else if (id == R.id.nav_scenario_two) {
            showScenarioTwo();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     * show the ScenarioOneFragment to the main screen
     */
    private void showScenarioOne() {
        mCurrentNaviId = R.id.nav_scenario_one;

        ScenarioOneFragment fragmentOne = new ScenarioOneFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragmentOne)
                .commit();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.nav_scenario_one));
        }
    }

    /**
     * show the ScenarioTwoFragment to the main screen
     */
    private void showScenarioTwo() {
        mCurrentNaviId = R.id.nav_scenario_two;
        ScenarioTwoFragment fragmentTwo = new ScenarioTwoFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragmentTwo)
                .commit();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.nav_scenario_two));
        }
    }
}
