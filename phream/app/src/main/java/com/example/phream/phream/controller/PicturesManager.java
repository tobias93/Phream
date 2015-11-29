package com.example.phream.phream.controller;

import android.content.ContentResolver;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.example.phream.phream.model.IPicturesCallback;
import com.example.phream.phream.model.Picture;
import com.example.phream.phream.model.Stream;
import com.example.phream.phream.model.database.DBManager;
import com.example.phream.phream.model.database.Tables.TblPicture;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class PicturesManager {

    private IPicturesCallback callback = null;
    private Stream stream = null;
    private Picture[] pictures = null;

    public PicturesManager(Stream stream) {
        this.stream = stream;
    }

    /**
     * Sets the object on which the callback methods will be called.
     *
     * @param callback
     */
    public void setCallback(IPicturesCallback callback) {
        this.callback = callback;
    }

    /**
     * Returns the callback methods.
     *
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
        AsyncTask<Stream, Integer, Picture[]> finder = new AsyncTask<Stream, Integer, Picture[]>() {
            @Override
            protected Picture[] doInBackground(Stream... params) {
                SQLiteDatabase db = DBManager.getDB();
                return TblPicture.getAllPictures(db, params[0].getId());
            }

            @Override
            protected void onPostExecute(Picture[] pics) {
                PicturesManager.this.pictures = pics;
                callback.onPicturesListUpdated(pics);
            }
        };

        finder.execute(stream);
    }

    /**
     * Inserts a picture to the database
     * <p/>
     * As the method works asynchronously, the result will be returned using the
     * "onPicturesCreated" callback.
     */
    public void insertPicture(final Picture picture) {

        picture.setStreamId(this.stream.getId());

        AsyncTask<Picture, Integer, Boolean> inserter = new AsyncTask<Picture, Integer, Boolean>() {
            @Override
            protected Boolean doInBackground(Picture... params) {
                SQLiteDatabase db = DBManager.getDB();
                try {
                    TblPicture.insertPicture(db, params[0]);
                    return true;
                } catch (Exception e) {
                    return false;
                }
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

        inserter.execute(picture);
        findAllPictures();
    }

    /**
     * Imports & Inserts a picture to the database
     * <p/>
     * As the method works asynchronously, the result will be returned using the
     * "onPicturesCreated" callback.
     */
    public void importInsertPicture(final Picture picture, final ContentResolver cr) {

        picture.setStreamId(this.stream.getId());

        AsyncTask<Picture, Integer, Boolean> importInserter = new AsyncTask<Picture, Integer, Boolean>() {
            @Override
            protected Boolean doInBackground(Picture... params) {

                try {
                    if(params[0].getImportUri() != null && params[0].getFilepath() != null){
                        InputStream imageStream = cr.openInputStream(params[0].getImportUri());
                        copyImage(imageStream, new File(params[0].getFilepath()));
                        params[0].setImportUri(null);

                        SQLiteDatabase db = DBManager.getDB();
                        try {
                            TblPicture.insertPicture(db, params[0]);

                            return true;
                        } catch (Exception e) {
                            return false;
                        }
                    }
                    else{
                        return false;
                    }
                } catch (IOException e) {
                    return false;
                }

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

        importInserter.execute(picture);
        findAllPictures();
    }

    /**
     * Deletes a picture in the database
     * <p/>
     * As the method works asynchronously, the result will be returned using the
     * "onPicturesDeleted" callback.
     */
    public void deletePicture(final Picture picture) {
        AsyncTask<Picture, Integer, Boolean> deleter = new AsyncTask<Picture, Integer, Boolean>() {
            @Override
            protected Boolean doInBackground(Picture... params) {
                SQLiteDatabase db = DBManager.getDB();
                try {
                    TblPicture.deletePicture(db, params[0]);
                    File deleterFile = new File(params[0].getFilepath());
                    deleterFile.delete();
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (result) {
                    callback.onPictureDeleted();
                } else {
                    callback.onPictureDeletedError(picture);
                }
            }
        };

        deleter.execute(picture);
        findAllPictures();
    }

    /**
     * Deletes all picture in the database to a given stream
     * <p/>
     * As the method works asynchronously, the result will be returned using the
     * "onPicturesDeleted" callback.
     */
    public void deleteAllPictures(){
        for (int i = 0; i < pictures.length; ++i){
            deletePicture(pictures[i]);
        }
    }


    /**
     * Update a picture in the database
     *
     * As the method works asynchronously, the result will be returned using the
     * "onPictureUpdated" callback.
     */
    public void updatePicture(final Picture picture) {
        AsyncTask<Picture, Integer, Boolean> updater = new AsyncTask<Picture, Integer, Boolean>() {
            @Override
            protected Boolean doInBackground(Picture... params) {
                SQLiteDatabase db = DBManager.getDB();
                try {
                    TblPicture.updatePicture(db, params[0]);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (result) {
                    callback.onPictureUpdated();
                } else {
                    callback.onPictureUpdatedError(picture);
                }
            }
        };

        updater.execute(picture);
        findAllPictures();
    }

    public Picture getPicture(int position){
        return this.pictures[position];
    }


    /**
     * Copies an image
     */
    public void copyImage(InputStream in, File dst) throws IOException {
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

}
