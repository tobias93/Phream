package com.example.phream.phream;

import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;

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

        // handle click events in the navigation
        mNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem){
                switch (menuItem.getItemId())
                {
                    case R.id.menu_main_drawer_add_stream:
                        addStream();
                        return false;
                    default:
                        // do nothing
                        return false;
                }
            }
        });

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

    public void addStream() {
        // ask for the stream name

        // - Input field for the name of the stream
        final EditText streamNameEditText = new EditText(this);
        streamNameEditText.setHint(R.string.main_addstream_name);
        streamNameEditText.setSingleLine(true);

        // - Dialog that shows the input text.
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        dialogBuilder.setTitle(R.string.main_addstream_title);
        dialogBuilder.setPositiveButton(R.string.main_addstream_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mDrawerLayout.closeDrawer(mNavigation);
                Log.i("#PHREAM", "added stream " + streamNameEditText.getText());
            }
        });
        dialogBuilder.setView(streamNameEditText);
        dialogBuilder.show();
    }
}
