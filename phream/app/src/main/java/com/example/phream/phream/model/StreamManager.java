package com.example.phream.phream.model;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.example.phream.phream.model.database.DBManager;
import com.example.phream.phream.model.database.Tables.TblStream;

public class StreamManager {

    private IStreamsCallback callback = null;

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
    public void refreshListOfStreams() {
        AsyncTask<Void, Integer, Stream[]> finder = new AsyncTask<Void, Integer, Stream[]>() {
            @Override
            protected Stream[] doInBackground(Void... params) {
                SQLiteDatabase db = DBManager.getDB();
                return TblStream.findAll(db);
            }

            @Override
            protected void onPostExecute(Stream[] streams) {
                callback.onStreamListAvailable(streams);
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
                    callback.onStreamCreated(stream);
                } else {
                    callback.onStreamCreationError(stream);
                }
            }
        };

        inserter.execute(stream);
    }

    /**
     * Persists a stream by changing an already existing database entry.
     * @param stream
     */
    public void updateStream(final com.example.phream.phream.model.Stream stream) {
        AsyncTask<com.example.phream.phream.model.Stream, Integer, Boolean> updater = new AsyncTask<com.example.phream.phream.model.Stream, Integer, Boolean>() {
            @Override
            protected Boolean doInBackground(com.example.phream.phream.model.Stream... params) {
                SQLiteDatabase db = DBManager.getDB();
                TblStream.update(db, params[0]);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (result) {
                    callback.onStreamUpdated(stream);
                } else {
                    callback.onStreamUpdateError(stream);
                }
            }
        };

        updater.execute(stream);
    }

    public void deleteStream(final com.example.phream.phream.model.Stream stream) {
        AsyncTask<com.example.phream.phream.model.Stream, Integer, Boolean> deleter = new AsyncTask<com.example.phream.phream.model.Stream, Integer, Boolean>() {
            @Override
            protected Boolean doInBackground(com.example.phream.phream.model.Stream... params) {
                SQLiteDatabase db = DBManager.getDB();
                TblStream.deleteStream(db, params[0].getId());
                return true;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (result) {
                    callback.onStreamDeleted(stream);
                } else {
                    callback.onStreamDeletionError(stream);
                }
            }
        };

        deleter.execute(stream);
    }
}
