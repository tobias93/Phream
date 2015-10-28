package com.example.phream.phream;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find views
        mNavigation = (NavigationView) findViewById(R.id.activity_main_navigation);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_main_drawer);

        // Menu icon in the status bar interacts with the navigation drawer in a fancy way.
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.action_settings, R.string.action_settings);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        // if the navigation drawer is opened: close it! if not: exit!
        if (mDrawerLayout.isDrawerOpen(mNavigation)){
            mDrawerLayout.closeDrawer(mNavigation);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                // Toggle navigation drawer
                if (mDrawerLayout.isDrawerOpen(mNavigation)){
                    mDrawerLayout.closeDrawer(mNavigation);
                } else {
                    mDrawerLayout.openDrawer(mNavigation);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
