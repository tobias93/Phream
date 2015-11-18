package com.example.phream.phream.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.example.phream.phream.model.database.DBManager;
import com.example.phream.phream.model.database.Tables.TblStream;

public class StreamManager {

    private IStreamsCallback callback = null;
    private Context context;

    /**
     * Constructor
     * @param c
     */
    public StreamManager(Context c) {
        this.context = c;
        DBManager.init(c);
    }

    /**
     * Sets the object on which the callback methods will be called.
     * @param callback
     */
    public void setCallback(IStreamsCallback callback){
        this.callback = callback;
    }

    /**
     * Returns the callback methods.
     * @return
     */
    public IStreamsCallback getCallback() {
        return this.callback;
    }

    /**
     * Queries the database for a list of all
     * streams.
     * As the method works asynchronously, the result will be returned using the
     * "onStreamListAviable" callback.
     */
    public void findAllStreams() {
        AsyncTask<Void, Integer, Stream[]> finder = new AsyncTask<Void, Integer, Stream[]>() {
            @Override
            protected Stream[] doInBackground(Void... params) {
                SQLiteDatabase db = DBManager.getDB();
                return TblStream.findAll(db);
            }

            @Override
            protected void onPostExecute(Stream[] streams) {
                callback.onStreamListAviable(streams);
            }
        };

        finder.execute();
    }

    /**
     * Persists a stream by adding it as a new entry to the database.
     * @param stream
     */
    public void insertStream(final com.example.phream.phream.model.Stream stream) {
        AsyncTask<com.example.phream.phream.model.Stream, Integer, Boolean> inserter = new AsyncTask<com.example.phream.phream.model.Stream, Integer, Boolean>() {
            @Override
            protected Boolean doInBackground(com.example.phream.phream.model.Stream... params) {
                assert(params.length == 1);
                SQLiteDatabase db = DBManager.getDB();
                try {
                    TblStream.insert(db, params[0]);
                    return true;
                } catch (Exception e)
                {
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (result) {
                    StreamManager.this.callback.onStreamCreated(stream);
                } else {
                    StreamManager.this.callback.onStreamCreationError(stream);
                }
            }
        };

        inserter.execute();

    }

    /**
     * Persists a stream by changing an already existing database entry.
     * @param stream
     */
    public void updateStream(com.example.phream.phream.model.Stream stream) {
        /*AsyncTask<Stream, Integer, Boolean> inserter = new AsyncTask<Stream, Integer, Boolean>() {
            @Override
            protected Boolean doInBackground(com.example.phream.phream.model.Stream... params) {
                assert (params.length == 1);
                SQLiteDatabase db = DBManager.getDB();
                try {

                } catch (Exception e) {
                    return false;
                }
            }
        };

        inserter.execute(stream);*/
    }

    public void deleteStream(com.example.phream.phream.model.Stream stream) {

    }
}
