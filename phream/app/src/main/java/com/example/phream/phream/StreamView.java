package com.example.phream.phream;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.phream.phream.model.RecyclerViewAdapter;
import com.example.phream.phream.model.Stream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StreamView.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StreamView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StreamView extends Fragment {

    // Constants
    static final int PICK_CAMERA_REQUEST = 1;
    static final int GALLERY_INTENT_CALLED = 2;
    private File directory;

    // Communication with parent view
    private OnFragmentInteractionListener mListener;

    // UI
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton mCameraFab;
    private FloatingActionButton mGaleryFab;

    //...
    private String takenPhotoPath;
    private long takenPhotoTimestamp;

    //---- Lifecycle stuff -------------------------------------------------------------------------

    public StreamView() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StreamView.
     */
    // TODO: Rename and change types and number of parameters
    public static StreamView newInstance(String param1, String param2) {
        StreamView fragment = new StreamView();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
        String[] strings = {"Hallo", "Hallo", "Hallo", "Hallo", "Hallo", "Hallo", "Hallo", "Hallo", "Hallo", "Hallo", "Hallo", "Hallo", "Hallo", "Hallo", "Hallo", "Hallo", "Hallo", "Hallo", "Hallo", "Hallo", "Hallo", "Hallo", "Hallo", "Hallo",};
        mAdapter = new RecyclerViewAdapter(strings);
        mRecyclerView.setAdapter(mAdapter);

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

        if (resultCode != Activity.RESULT_OK)  return;

        if(requestCode == PICK_CAMERA_REQUEST){
            importCameraImage();
        }

        if (requestCode == GALLERY_INTENT_CALLED) {
            importGalleryImage(data);
        }
    }

    //---- Photo import ----------------------------------------------------------------------------

    /**
     * Start the galery intent
     */
    public void openGallery(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.main_select_image_gallery)), GALLERY_INTENT_CALLED);
    }

    /**
     * Start the camera intent
     */
    public void openCamera(View v) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go

            File photoFile = new File(directory.getAbsolutePath() + File.separator + imageNameGenerator());
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
        dialogBuilder.setPositiveButton(R.string.main_addstream_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Add the picture
                String pictureName = pictureNameEditText.getText().toString();
                // TODO  Generate a new pictures object
            }
        });

        dialogBuilder.setView(pictureNameEditText);
        dialogBuilder.show();

        // Reset members
        takenPhotoPath = null;
    }

    private void importGalleryImage(Intent data) {
        if (null == data) return;

        Uri originalUri = data.getData();
        String selectedImagePath = "";

        String[] projection = {MediaStore.Images.Media.DATA};

        Cursor imageCursor = getActivity().getContentResolver().query(originalUri, projection, null, null, null);
        imageCursor.moveToFirst();

        int columnIndex = imageCursor.getColumnIndex(projection[0]);
        selectedImagePath = imageCursor.getString(columnIndex);
        imageCursor.close();

        Log.e("Photopath:", selectedImagePath);

        // Copy Image
        try {
            copyImage(new File(selectedImagePath), new File(directory.getAbsolutePath() + File.separator + imageNameGenerator()));
        } catch (IOException e) {
        }
    }
    /**
     * Creates a unique filename from unix timestamp and a random number
     */
    private String imageNameGenerator() {
        // random int for the syncronisation feature
        Random r = new Random();
        takenPhotoTimestamp = System.currentTimeMillis() / 1000;
        return "image_" + takenPhotoTimestamp + "_" + r.nextInt(10000) + ".jpg";
    }

    /**
     * copies an image
     */
    public void copyImage(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        Log.e("PhotopathCopyed:", dst.getAbsolutePath());

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }
}
