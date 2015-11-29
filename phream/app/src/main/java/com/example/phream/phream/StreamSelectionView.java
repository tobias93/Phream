package com.example.phream.phream;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.phream.phream.model.IStreamsCallback;
import com.example.phream.phream.model.Stream;
import com.example.phream.phream.controller.StreamManager;
import com.example.phream.phream.model.database.DBManager;

public class StreamSelectionView extends Fragment implements IStreamsCallback{

    private OnFragmentInteractionListener mListener;

    // Controller
    StreamManager streamManager;

    // The current stream
    Stream activeStream;

    // the navigation view
    private NavigationView mNavigation;

    //---- Lifecycle Stuff -------------------------------------------------------------------------

    public StreamSelectionView() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        // Init database
        DBManager.init(getContext());

        // Init streamManager
        streamManager = new StreamManager();
        streamManager.setCallback(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // handle click events in the navigation
        mNavigation = (NavigationView) getActivity().findViewById(R.id.fragment_stream_selection_navigation);
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

        // Initialize the list of streams.
        streamManager.refreshListOfStreams();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mNavigation = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stream_selection_view, container, false);
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
        void onStreamSelected(Stream stream);
    }

    //---- Stream management -----------------------------------------------------------------------

    public void onStreamCreated(Stream stream){
        selectStream(stream, true);
    }

    public void onStreamUpdated(Stream stream){
        streamManager.refreshListOfStreams();
    }
    public void onStreamDeleted(Stream stream){ streamManager.refreshListOfStreams(); }
    public void onStreamListAvailable(Stream[] streams){

        // handle transitions between streams available and no streams available.
        if (activeStream == null && streams.length > 0) {
            selectStream(streams[0], false);
        } else if (streams.length == 0) {
            selectStream(null, false);
        }

        // Rebuild the menu
        boolean foundActiveStream = false;
        Menu navigationMenu = mNavigation.getMenu();
        Menu streamsMenu = navigationMenu.findItem(R.id.main_drawer_streams).getSubMenu();
        streamsMenu.clear();
        for (final Stream stream : streams) {
            MenuItem item = streamsMenu.add(stream.getName());
            item.setIcon(R.drawable.ic_folder_open_black_24dp);
            item.setCheckable(true);
            if (stream.getId() == activeStream.getId()) {
                item.setChecked(true);
                foundActiveStream = true;
            }
            item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    selectStream(stream, true);
                    return true;
                }
            });
        }

        // select the first stream, if the selected stream was deleted.
        if (activeStream != null && !foundActiveStream && streams.length > 0) {
            selectStream(streams[0], true);
        }

        // Temporary workaround for a bug in the android support library
        // grrrrr...
        // look here for further information:
        // http://stackoverflow.com/a/30706233/5440981
        MenuItem mi = navigationMenu.getItem(navigationMenu.size() - 1);
        mi.setTitle(mi.getTitle());
    }
    public void onStreamCreationError(Stream stream){}
    public void onStreamUpdateError(Stream stream){}
    public void onStreamDeletionError(Stream stream){}

    /**
     * Selects the given stream.
     */
    private  void selectStream(Stream stream, boolean refreshList)
    {
        activeStream = stream;
        mListener.onStreamSelected(stream);
        if (refreshList) streamManager.refreshListOfStreams();
    }

    /**
     * Shows a dialog to create a new stream
     * and creates it (if the user does not cancel the process.)
     */
    public void addStream() {
        // Input field for the name of the stream
        final EditText streamNameEditText = new EditText(getActivity());
        streamNameEditText.setHint(R.string.main_addstream_name);
        streamNameEditText.setSingleLine(true);

        // Dialog that shows the input text.
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
        dialogBuilder.setTitle(R.string.main_addstream_title);
        dialogBuilder.setPositiveButton(R.string.main_addstream_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Add the stream
                String streamName = streamNameEditText.getText().toString();
                Stream stream = new Stream(streamName);
                streamManager.insertStream(stream);
            }
        });
        dialogBuilder.setView(streamNameEditText);
        dialogBuilder.show();
    }

    public void renameStream(final Stream stream) {
        // Input field for the name of the stream
        final EditText streamNameEditText = new EditText(getActivity());
        streamNameEditText.setHint(R.string.main_renamestream_name);
        streamNameEditText.setSingleLine(true);

        // Dialog that shows the input text.
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
        dialogBuilder.setTitle(R.string.main_renamestream_title);
        dialogBuilder.setPositiveButton(R.string.main_renamestream_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Add the stream
                String streamName = streamNameEditText.getText().toString();
                stream.setName(streamName);
                streamManager.updateStream(stream);
            }
        });
        dialogBuilder.setView(streamNameEditText);
        dialogBuilder.show();
    }

    public void deleteStream(Stream stream) {
        streamManager.deleteStream(stream);
    }

}
