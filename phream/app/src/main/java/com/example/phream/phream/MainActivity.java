package com.example.phream.phream;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;

import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import com.example.phream.phream.model.Stream;

public class MainActivity extends AppCompatActivity implements StreamView.OnFragmentInteractionListener, NoStreamView.OnFragmentInteractionListener, StreamSelectionView.OnFragmentInteractionListener {

    // UI
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private View mNavigation;

    /**
     * Initialize everything
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find views
        mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_main_drawer_layout);
        mNavigation = findViewById(R.id.activity_main_navigation_container);

        // Menu icon in the status bar interacts with the navigation drawer in a fancy way.
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.action_settings, R.string.action_settings);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerToggle.syncState();

        // Insert the default fragment into the main view
        BlankView defaultView = new BlankView();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.activity_main_main_view_container, defaultView)
                .commit();
    }

    /**
     * Handle back button presses
     */
    @Override
    public void onBackPressed() {
        // if the navigation drawer is opened: close it! if not: exit!
        if (mDrawerLayout.isDrawerOpen(mNavigation)) {
            mDrawerLayout.closeDrawer(mNavigation);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Handle menu button presses
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent e) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (!mDrawerLayout.isDrawerOpen(mNavigation)) {
                mDrawerLayout.openDrawer(mNavigation);
            } else {
                mDrawerLayout.closeDrawer(mNavigation);
            }
            return true;
        }
        return super.onKeyDown(keyCode, e);
    }

    /**
     * Handle button presses for the menu button in the action bar.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Toggle navigation drawer
                if (mDrawerLayout.isDrawerOpen(mNavigation)) {
                    mDrawerLayout.closeDrawer(mNavigation);
                } else {
                    mDrawerLayout.openDrawer(mNavigation);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Always show the selected stream.
     */
    @Override
    public void onStreamSelected(Stream stream) {
        if (stream == null)
        {
            NoStreamView noStreamView = new NoStreamView();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_main_main_view_container, noStreamView)
                    .commit();
        } else {
            StreamView streamView = new StreamView();
            streamView.initPicturesManager(stream);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_main_main_view_container, streamView)
                    .commit();
        }
        mDrawerLayout.closeDrawer(mNavigation);
    }

    //---- stream transactions ---------------------------------------------------------------------

    @Override
    public void renameStream(Stream stream) {
        StreamSelectionView s = (StreamSelectionView) getSupportFragmentManager().findFragmentById(R.id.activity_main_navigation);
        s.renameStream(stream);
    }

    @Override
    public void deleteStream(Stream stream) {

    }
}
