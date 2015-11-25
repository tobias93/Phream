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

import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import android.util.Log;
import android.widget.EditText;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    static final int PICK_CAMERA_REQUEST = 1;
    static final int GALLERY_INTENT_CALLED = 2;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigation;

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

    public void startImageView(View view) {
        Intent intent = new Intent(this, ImageDetailView.class);
        intent.putExtra("ImagePath", "/storage/extSdCard/DCIM/Camera/20150101_113305_Richtone(HDR).jpg");
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        // if the navigation drawer is opened: close it! if not: exit!
        if (mDrawerLayout.isDrawerOpen(mNavigation)) {
            mDrawerLayout.closeDrawer(mNavigation);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent e) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            // your action...

            if (!mDrawerLayout.isDrawerOpen(mNavigation)) {
                mDrawerLayout.openDrawer(mNavigation);
            } else {
                mDrawerLayout.closeDrawer(mNavigation);
            }
            return true;
        }
        return super.onKeyDown(keyCode, e);
    }

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


    public void openCamera(View v) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, PICK_CAMERA_REQUEST);
    }

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
                copy(new File(selectedImagePath), new File(directory.getAbsolutePath() + File.separator + "image_" + System.currentTimeMillis() / 1000 + "_" + r.nextInt(1000) + ".jpg"));
            } catch (IOException e) {
            }
        }
    }


    public void copy(File src, File dst) throws IOException {
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

    // Get the Uri of Internal/External Storage for Media
    private Uri getUri() {
        String state = Environment.getExternalStorageState();
        if (!state.equalsIgnoreCase(Environment.MEDIA_MOUNTED))
            return MediaStore.Images.Media.INTERNAL_CONTENT_URI;

        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
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
