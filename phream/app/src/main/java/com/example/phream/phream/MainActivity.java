package com.example.phream.phream;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;

import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;

import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import android.util.Log;
import android.widget.EditText;

import com.example.phream.phream.model.IStreamsCallback;
import com.example.phream.phream.model.RecyclerViewAdapter;
import com.example.phream.phream.model.Stream;
import com.example.phream.phream.model.StreamManager;
import com.example.phream.phream.model.database.DBManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements IStreamsCallback {


    // Constants
    static final int PICK_CAMERA_REQUEST = 1;
    static final int GALLERY_INTENT_CALLED = 2;

    // UI
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigation;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    // Model
    StreamManager streamManager;

    // The current status
    Stream activeStream;

    /**
     * Setup the Activity
     */
    private File directory = new File(Environment.getExternalStorageDirectory() + File.separator + "Phream" + File.separator);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create App dir
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Log.d("Phream", "No SDCARD");
            //Creating an internal dir
            directory = this.getDir("Phream", Context.MODE_PRIVATE);
            if (!directory.isDirectory()) {
                directory.mkdirs();
            }
        } else {
            if (!directory.isDirectory()) {
                directory.mkdirs();
            }
        }

        File nomedia = new File(directory.getAbsolutePath() + File.separator + ".nomedia");
        if (!nomedia.exists()) {
            try {
                nomedia.createNewFile();
            } catch (IOException e) {
                Log.d("Phream", "Couldn't create .nomedia file!");
            }
        }

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

        // Recyler view
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        String[] strings = {"Hallo", "Hallo","Hallo","Hallo","Hallo","Hallo","Hallo","Hallo","Hallo","Hallo","Hallo","Hallo","Hallo","Hallo","Hallo","Hallo","Hallo","Hallo","Hallo","Hallo","Hallo","Hallo","Hallo","Hallo", };
        mAdapter = new RecyclerViewAdapter(strings);
        mRecyclerView.setAdapter(mAdapter);



    }


    public void startStreamFragment(Stream stream) {
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

    public void openGallery(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.main_select_image_gallery)), GALLERY_INTENT_CALLED);
    }

    /**
     * Start the camera intent
     */
    public void openCamera(View v){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, PICK_CAMERA_REQUEST);
    }

    /**
     * Get back the camera intent result.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;
        if (null == data) return;

        if (requestCode == PICK_CAMERA_REQUEST) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            if (photo != null) {
                Toast toast = Toast.makeText(this, "Klappt", Toast.LENGTH_LONG);
                toast.show();
            }
        }
        if (requestCode == GALLERY_INTENT_CALLED) {
            Uri originalUri = data.getData();
            String selectedImagePath = "";

            String[] projection = {MediaStore.Images.Media.DATA};

            Cursor imageCursor = getContentResolver().query(originalUri, projection, null, null, null);
            imageCursor.moveToFirst();

            int columnIndex = imageCursor.getColumnIndex(projection[0]);
            selectedImagePath = imageCursor.getString(columnIndex);
            imageCursor.close();

            Log.e("Photopath:", selectedImagePath);

            // Copy Image
            try {
                // random int for the syncronisation feature
                Random r = new Random();
                copyImage(new File(selectedImagePath), new File(directory.getAbsolutePath() + File.separator + "image_" + System.currentTimeMillis() / 1000 + "_" + r.nextInt(1000) + ".jpg"));
            } catch (IOException e) {
            }
        }
    }

    // Get the Uri of Internal/External Storage for Media
    private Uri getUri() {
        String state = Environment.getExternalStorageState();
        if (!state.equalsIgnoreCase(Environment.MEDIA_MOUNTED))
            return MediaStore.Images.Media.INTERNAL_CONTENT_URI;

        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    }

    public void copyImage(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
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
        Menu navigationMenu =  mNavigation.getMenu();
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
}
