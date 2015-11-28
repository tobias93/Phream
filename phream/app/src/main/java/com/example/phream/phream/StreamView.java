package com.example.phream.phream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.phream.phream.model.IPicturesCallback;
import com.example.phream.phream.model.Picture;
import com.example.phream.phream.model.PicturesManager;
import com.example.phream.phream.model.RecyclerViewAdapter;
import com.example.phream.phream.model.Stream;
import com.example.phream.phream.model.database.DBManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class StreamView extends Fragment implements IPicturesCallback {

    // Constants
    static final int PICK_CAMERA_REQUEST = 1;
    static final int GALLERY_PRE_KITKAT_INTENT_CALLED = 2;
    static final int GALLERY_KITKAT_INTENT_CALLED = 3;
    private File directory;

    // Communication with parent view
    private OnFragmentInteractionListener mListener;

    // UI
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton mCameraFab;
    private FloatingActionButton mGaleryFab;

    // Content Manger
    PicturesManager picturesManager;

    //...
    private String takenPhotoPath;
    private long PhotoTimestamp;

    //---- Lifecycle stuff -------------------------------------------------------------------------

    public StreamView() {
        // Required empty public constructor
    }

    // One solution to pass a object to fragments -> Parcelable aren't such easy to implement
    // http://stackoverflow.com/questions/10836525/passing-objects-in-to-fragments
    public void initPicturesManager(Stream stream) {
        this.picturesManager = new PicturesManager(stream);
        picturesManager.setCallback(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        DBManager.init(getContext());
        setRetainInstance(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_stream_view, menu);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Create App dir
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Log.d("Phream", "No SDCARD");
            //Creating an internal dir
            directory = getActivity().getDir("Phream", Context.MODE_PRIVATE);
            if (!directory.isDirectory()) {
                directory.mkdirs();
            }
        } else {
            directory = getActivity().getExternalFilesDir(null);
        }

        File nomedia = new File(directory.getAbsolutePath() + File.separator + ".nomedia");
        if (!nomedia.exists()) {
            try {
                nomedia.createNewFile();
            } catch (IOException e) {
                Log.d("Phream", "Couldn't create .nomedia file!");
            }
        }

        // Floating Action Button(s)
        mCameraFab = (FloatingActionButton) getActivity().findViewById(R.id.streamViewCameraFab);
        mGaleryFab = (FloatingActionButton) getActivity().findViewById(R.id.streamViewGaleryFab);
        mCameraFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera(v);
            }
        });
        mGaleryFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery(v);
            }
        });

        // Recyler view
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        picturesManager.findAllPictures();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mRecyclerView = null;
        mLayoutManager = null;
        mAdapter = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stream_view, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) return;

        if (requestCode == PICK_CAMERA_REQUEST) {
            importCameraImage();
        }

        if (requestCode == GALLERY_PRE_KITKAT_INTENT_CALLED || requestCode == GALLERY_KITKAT_INTENT_CALLED) {
            importGalleryImage(requestCode, data);
        }
    }

    //---- Picture list management -----------------------------------------------------------------

    @Override
    public void onPicturesListUpdated(Picture[] pictures) {
        mAdapter = new RecyclerViewAdapter(pictures);
        mRecyclerView.swapAdapter(mAdapter, false);

    }

    @Override
    public void onPictureCreated(Picture picture) {

    }

    @Override
    public void onPictureCreatedError(Picture picture) {
        Toast.makeText(this.getContext(), R.string.stream_view_callback_insert_error + picture.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPictureDeleted() {

    }

    @Override
    public void onPictureDeletedError(Picture picture) {
        Toast.makeText(this.getContext(), R.string.stream_view_callback_delete_error + picture.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPictureUpdated() {

    }

    @Override
    public void onPictureUpdatedError(Picture picture) {
        Toast.makeText(this.getContext(), R.string.stream_view_callback_update_error + picture.getName(), Toast.LENGTH_SHORT).show();
    }

    //---- Photo import ----------------------------------------------------------------------------

    /**
     * Start the gallery intent
     */
    public void openGallery(View v) {
        if (Build.VERSION.SDK_INT < 19) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.main_select_image_gallery)), GALLERY_PRE_KITKAT_INTENT_CALLED);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, GALLERY_KITKAT_INTENT_CALLED);
        }

//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.main_select_image_gallery)), GALLERY_PRE_KITKAT_INTENT_CALLED);
    }

    /**
     * Start the camera intent
     */
    public void openCamera(View v) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go

            File photoFile = new File(imagePathNameGenerator());
            takenPhotoPath = photoFile.getAbsolutePath();

            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(photoFile));
            startActivityForResult(takePictureIntent, PICK_CAMERA_REQUEST);
        }
    }

    private void importCameraImage() {

        if (takenPhotoPath == null) {
            return;
        }

        Log.e("Photopath:", takenPhotoPath);

        // Ask for pictures title
        // Input field for the name of the picture
        final EditText pictureNameEditText = new EditText(getActivity());
        pictureNameEditText.setHint(R.string.main_insert_picturename);
        pictureNameEditText.setSingleLine(true);

        // Dialog that shows the input text.
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
        dialogBuilder.setCancelable(false);
        dialogBuilder.setTitle(R.string.main_insert_picturename_title);
        dialogBuilder.setPositiveButton(R.string.main_insert_picturename_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Generate new Picture Object
                Picture picture = new Picture(pictureNameEditText.getText().toString(), takenPhotoPath, PhotoTimestamp);
                // Add the picture to the stream/picturemanager/database
                picturesManager.insertPicture(picture);

                // Reset members
                takenPhotoPath = null;

            }
        });

        dialogBuilder.setView(pictureNameEditText);
        dialogBuilder.show();

    }

    @SuppressLint("NewApi")
    private void importGalleryImage(int buildVersion, Intent data) {
        if (null == data) return;
        final Uri pictureUri = data.getData();

        // Ask for pictures title
        // Input field for the name of the picture
        final EditText pictureNameEditText = new EditText(getActivity());
        pictureNameEditText.setHint(R.string.main_insert_picturename);
        pictureNameEditText.setSingleLine(true);

        // Dialog that shows the input text.
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
        dialogBuilder.setCancelable(false);
        dialogBuilder.setTitle(R.string.main_insert_picturename_title);
        dialogBuilder.setPositiveButton(R.string.main_insert_picturename_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Generate new Picture Object
                Picture picture = new Picture(pictureNameEditText.getText().toString());

                picture.setImportUri(pictureUri);
                picture.setFilepath(imagePathNameGenerator());
                picture.setCreated(PhotoTimestamp);

                // Add the picture to the stream/picturemanager/database
                ContentResolver cr = getContext().getApplicationContext().getContentResolver();
                picturesManager.importInsertPicture(picture, cr);
            }
        });

        dialogBuilder.setView(pictureNameEditText);
        dialogBuilder.show();
    }

    // Get the MediaUri of Internal/External Storage for Media
    private Uri getMediaUri() {
        String state = Environment.getExternalStorageState();
        if (!state.equalsIgnoreCase(Environment.MEDIA_MOUNTED))
            return MediaStore.Images.Media.INTERNAL_CONTENT_URI;

        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    }

    /**
     * Creates a unique filename from unix timestamp and a random number
     */
    private String imagePathNameGenerator() {
        // random int for the syncronisation feature
        Random r = new Random();
        PhotoTimestamp = System.currentTimeMillis() / 1000;
        return directory.getAbsolutePath() + File.separator + "image_" + PhotoTimestamp + "_" + r.nextInt(10000) + ".jpg";
    }

}
