package com.example.phream.phream;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import android.util.Log;
import android.widget.EditText;

import com.example.phream.phream.model.IStreamsCallback;
import com.example.phream.phream.model.Stream;
import com.example.phream.phream.model.StreamManager;
import com.example.phream.phream.model.database.DBManager;

public class MainActivity extends AppCompatActivity implements IStreamsCallback, StreamView.OnFragmentInteractionListener, NoStreamView.OnFragmentInteractionListener {

    // UI
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigation;

    // Controller
    StreamManager streamManager;

    // The current status
    Stream activeStream;

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

        // Init database
        DBManager.init(this);

        // Init streamManager
        streamManager = new StreamManager();
        streamManager.setCallback(this);

        // Request all streams from the database.
        streamManager.findAllStreams();

        // handle click events in the navigation
        mNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
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

    public void startStreamFragment(Stream stream) {
        this.activeStream = stream;
        Toast.makeText(MainActivity.this, "selected stream " + stream.getName(), Toast.LENGTH_LONG).show();
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
     * Creates a new stream
     */
    public void addStream() {
        // Input field for the name of the stream
        final EditText streamNameEditText = new EditText(this);
        streamNameEditText.setHint(R.string.main_addstream_name);
        streamNameEditText.setSingleLine(true);

        // Dialog that shows the input text.
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        dialogBuilder.setTitle(R.string.main_addstream_title);
        dialogBuilder.setPositiveButton(R.string.main_addstream_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Close the navigation drawer
                mDrawerLayout.closeDrawer(mNavigation);

                // Add the stream
                String streamName = streamNameEditText.getText().toString();
                Stream stream = new Stream(streamName);
                streamManager.insertStream(stream);
            }
        });
        dialogBuilder.setView(streamNameEditText);
        dialogBuilder.show();
    }

    @Override
    public void onStreamCreated(Stream stream) {
        // refresh the list of streams.
        streamManager.findAllStreams();
    }

    @Override
    public void onStreamUpdated(Stream stream) {

    }

    @Override
    public void onStreamDeleted(Stream stream) {

    }

    @Override
    public void onStreamListAvailable(Stream[] streams) {

        // Rebuild the menu
        Menu navigationMenu = mNavigation.getMenu();
        Menu streamsMenu = navigationMenu.findItem(R.id.main_drawer_streams).getSubMenu();
        streamsMenu.clear();
        for (final Stream stream : streams) {
            MenuItem item = streamsMenu.add(stream.getName());
            item.setIcon(R.drawable.ic_folder_open_black_24dp);
            item.setCheckable(true);
            if (stream == activeStream) {
                item.setChecked(true);
            }
            item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    startStreamFragment(stream);
                    return true;
                }
            });
        }

        // Temporary workaround for a bug in the android support library
        // grrrrr...
        // look here for further information:
        // http://stackoverflow.com/a/30706233/5440981
        MenuItem mi = navigationMenu.getItem(navigationMenu.size() - 1);
        mi.setTitle(mi.getTitle());
    }

    @Override
    public void onStreamCreationError(Stream stream) {
        Log.i("#PHREAM", "stream creation error.");
    }

    @Override
    public void onStreamUpdateError(Stream stream) {

    }

    @Override
    public void onStreamDeletionError(Stream stream) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
