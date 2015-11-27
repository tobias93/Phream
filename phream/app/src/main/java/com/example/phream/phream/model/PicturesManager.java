package com.example.phream.phream.model;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.example.phream.phream.model.database.DBManager;
import com.example.phream.phream.model.database.Tables.TblPicture;
import com.example.phream.phream.model.database.Tables.TblStream;

import java.util.ArrayList;

/**
 * Created by Philipp Pütz on 27.11.2015.
 */
public class PicturesManager {

    private IPicturesCallback callback = null;
    private Stream stream = null;
    private Pictures[] pictures = null;

    public PicturesManager(Stream stream){
        this.stream = stream;
    }


    /**
     * Sets the object on which the callback methods will be called.
     * @param callback
     */
    public void setCallback(IPicturesCallback callback){
        this.callback = callback;
    }

    /**
     * Returns the callback methods.
     * @return
     */
    public IPicturesCallback getCallback() {
        return this.callback;
    }

    /**
     * Queries the database for a list of all
     * pictures for a given stream.
     * As the method works asynchronously, the result will be returned using the
     * "onPicturesListUpdated" callback.
     */
    public void findAllPictures() {
        AsyncTask<Void, Integer, Pictures[]> finder = new AsyncTask<Void, Integer, Pictures[]>() {
            @Override
            protected Pictures[] doInBackground(Void... params) {
                SQLiteDatabase db = DBManager.getDB();
                return TblPicture.getAllPictures(db, stream.getId());
            }

            @Override
            protected void onPostExecute(Pictures[] pics) {
                pictures = pics;
                callback.onPicturesListUpdated(pics);
            }
        };

        finder.execute();
    }

    /**
     * Adds a picture to the database
     *
     * As the method works asynchronously, the result will be returned using the
     * "onPicturesCreated" callback.
     */
    public void insertPicture(final Pictures picture) {
        AsyncTask<Pictures, Integer, Boolean> finder = new AsyncTask<Pictures, Integer, Boolean>() {
            @Override
            protected Boolean doInBackground(Pictures... params) {
                SQLiteDatabase db = DBManager.getDB();
                return null;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (result) {
                    callback.onPictureCreated(picture);
                } else {
                    callback.onPictureCreatedError(picture);
                }
            }
        };

        finder.execute();
    }
}
