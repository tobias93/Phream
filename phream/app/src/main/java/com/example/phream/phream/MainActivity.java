package com.example.phream.phream;


import android.content.Intent;
import android.graphics.Bitmap;

import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;

import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import android.util.Log;
import android.widget.EditText;

import com.example.phream.phream.model.IStreamsCallback;
import com.example.phream.phream.model.Stream;
import com.example.phream.phream.model.StreamManager;


public class MainActivity extends AppCompatActivity implements IStreamsCallback {

    // Constants
    static final int PICK_PHOTO_REQUEST = 1;

    // UI
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigation;

    // Model
    StreamManager streamManager;

    /**
     * Setup the Activity
     * @param savedInstanceState
     */
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

        // Init streamManager
        streamManager = new StreamManager(this);
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

    /**
     * Handle back button presses
     */
    @Override
    public void onBackPressed() {
        // if the navigation drawer is opened: close it! if not: exit!
        if (mDrawerLayout.isDrawerOpen(mNavigation)){
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
            }
            else{
                mDrawerLayout.closeDrawer(mNavigation);
            }
            return true;
        }
        return super.onKeyDown(keyCode, e);
    }

    /**
     * Handle button presses for the menu button in the action bar.
     * @param item
     * @return
     */
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

    /**
     * Start the camera intent
     * @param v
     */
    public void openCamera(View v){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, PICK_PHOTO_REQUEST);
    }

    /**
     * Get back the camera intent's result.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_PHOTO_REQUEST) {
            if (resultCode == RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");

                if (photo != null) {
                    Toast toast = Toast.makeText(this, "Klappt", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
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
                mDrawerLayout.closeDrawer(mNavigation);
                
                Log.i("#PHREAM", "added stream " + streamNameEditText.getText());
            }
        });
        dialogBuilder.setView(streamNameEditText);
        dialogBuilder.show();
    }

    @Override
    public void onStreamCreated(Stream stream) {

    }

    @Override
    public void onStreamUpdated(Stream stream) {

    }

    @Override
    public void onStreamDeleted(Stream stream) {

    }

    @Override
    public void onStreamListAviable(Stream[] streams) {
        Menu menu = mNavigation.getMenu().findItem(R.id.main_drawer_streams).getSubMenu();
        menu.clear();
        for (Stream stream : streams) {
            menu.add(stream.getName());
        }

        //reinflate the menu to make the changes visible.
        //mNavigation.getMenu().clear();
        //mNavigation.inflateMenu(R.menu.menu_main_drawer);
    }

    @Override
    public void onStreamCreationError(Stream stream) {

    }

    @Override
    public void onStreamUpdateError(Stream stream) {

    }

    @Override
    public void onStreamDeletionError(Stream stream) {

    }
}
