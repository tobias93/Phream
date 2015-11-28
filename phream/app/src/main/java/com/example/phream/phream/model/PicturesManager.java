package com.example.phream.phream.model;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.example.phream.phream.model.database.DBManager;
import com.example.phream.phream.model.database.Tables.TblPicture;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class PicturesManager {

    private IPicturesCallback callback = null;
    private Stream stream = null;
    private Pictures[] pictures = null;

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
        AsyncTask<Stream, Integer, Pictures[]> finder = new AsyncTask<Stream, Integer, Pictures[]>() {
            @Override
            protected Pictures[] doInBackground(Stream... params) {
                SQLiteDatabase db = DBManager.getDB();
                return TblPicture.getAllPictures(db, params[0].getId());
            }

            @Override
            protected void onPostExecute(Pictures[] pics) {
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
    public void insertPicture(final Pictures picture) {

        picture.setStreamId(this.stream.getId());

        AsyncTask<Pictures, Integer, Boolean> inserter = new AsyncTask<Pictures, Integer, Boolean>() {
            @Override
            protected Boolean doInBackground(Pictures... params) {
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
    public void importInsertPicture(final Pictures picture) {

        picture.setStreamId(this.stream.getId());

        AsyncTask<Pictures, Integer, Boolean> importInserter = new AsyncTask<Pictures, Integer, Boolean>() {
            @Override
            protected Boolean doInBackground(Pictures... params) {

                try {
                    if(params[0].getGalleryFilepath() != null && params[0].getFilepath() != null){
                        copyImage(new File(params[0].getGalleryFilepath()), new File(params[0].getFilepath()));

                        SQLiteDatabase db = DBManager.getDB();
                        try {
                            TblPicture.insertPicture(db, params[0]);
                            params[0].setGalleryFilepath(null);
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
     * Deletes a picture to the database
     * <p/>
     * As the method works asynchronously, the result will be returned using the
     * "onPicturesDeleted" callback.
     */
    public void deletePicture(final Pictures picture) {
        AsyncTask<Pictures, Integer, Boolean> deleter = new AsyncTask<Pictures, Integer, Boolean>() {
            @Override
            protected Boolean doInBackground(Pictures... params) {
                SQLiteDatabase db = DBManager.getDB();
                try {
                    TblPicture.deletePicture(db, params[0]);
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
     * Update a picture in the database
     *
     * As the method works asynchronously, the result will be returned using the
     * "onPictureUpdated" callback.
     */
    public void updatePicture(final Pictures picture, String newPictureName) {
        AsyncTask<Pictures, Integer, Boolean> updater = new AsyncTask<Pictures, Integer, Boolean>() {
            @Override
            protected Boolean doInBackground(Pictures... params) {
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


    /**
     * Copies an image
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
