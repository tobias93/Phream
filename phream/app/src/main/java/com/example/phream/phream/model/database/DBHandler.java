package com.example.phream.phream.model.database;

/**
 * Created by Philipp Pütz on 23.10.2015.
 */

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.content.Context;

import com.example.phream.phream.model.database.Tables.TblPicture;
import com.example.phream.phream.model.database.Tables.TblStream;

public class DBHandler extends SQLiteOpenHelper {

    // Construktor
    public DBHandler(Context context) {
        super(context, Database.DATABASE_NAME, null, Database.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Tables
        db.execSQL(TblStream.QUERY_CREATE_TABLE);
        db.execSQL(TblPicture.QUERY_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // As this is the first version, there are no upgrades to do.
    }
/*
    // Insert Methods
    public void insertPicture(String picturename, long created, String filename, int stream) {
        ContentValues values = new ContentValues();
        values.put(PICTURE_COLUMN_PICTURENAME, picturename);
        values.put(PICTURE_COLUMN_CREATED, created);
        values.put(PICTURE_COLUMN_FILENAME, filename);
        values.put(PICTURE_COLUMN_STREAM, stream);

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_PICTURE, null, values);
        db.close();
    }

    public void insertStream(String streamname, long created) {
        ContentValues values = new ContentValues();
        values.put(STREAM_COLUMN_STREAMNAME, streamname);
        values.put(STREAM_COLUMN_CREATED, created);

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_STREAM, null, values);
        db.close();
    }

    // Delete Methods
    public boolean deletePicture(int id) {

        boolean result = false;

        String query = "Select * FROM " + TABLE_PICTURE + " WHERE " + PICTURE_COLUMN_ID + " =  " + id;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            db.delete(TABLE_PICTURE, PICTURE_COLUMN_ID + "= ?", new String[]{String.valueOf(id)});
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }

    public void deleteStream(int id) {

        String query = "Select * FROM " + TABLE_STREAM + " WHERE " + STREAM_COLUMN_ID + " =  " + id;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            db.delete(TABLE_STREAM, STREAM_COLUMN_ID + "= ?", new String[]{String.valueOf(id)});
            cursor.close();
        }

        query = "Select * FROM " + TABLE_PICTURE + " WHERE " + PICTURE_COLUMN_STREAM + " =  " + id;

        cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            db.delete(TABLE_PICTURE, PICTURE_COLUMN_STREAM + "= ?", new String[]{String.valueOf(id)});
            cursor.close();
        }

        db.close();
    }


    // Update / Rename - Methods
    public void renamePicture(int id, String picturename) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(PICTURE_COLUMN_PICTURENAME, picturename);

        db.update(TABLE_PICTURE, values, PICTURE_COLUMN_ID + "= ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Update / Rename - Methods
    public void renameStream(int id, String streamname) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(STREAM_COLUMN_STREAMNAME, streamname);

        db.update(TABLE_STREAM, values, STREAM_COLUMN_ID + "= ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public ArrayList<TblStream> getAllStreams() {
        String query = "Select * FROM " + TABLE_STREAM;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        ArrayList<TblStream> streamList = new ArrayList<TblStream>();

        if (cursor.moveToFirst()) {
            do {
                streamList.add(new TblStream(cursor.getInt(cursor.getColumnIndex(STREAM_COLUMN_ID)), cursor.getString(cursor.getColumnIndex(STREAM_COLUMN_STREAMNAME)),
                        cursor.getLong(cursor.getColumnIndex(STREAM_COLUMN_CREATED))));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return streamList;
    }

    public Pictures getNextPicture(int streamId, int currentPictureId){

        String query = "Select * FROM " + TABLE_PICTURE + " WHERE " + PICTURE_COLUMN_STREAM + " = " + streamId + " and " + PICTURE_COLUMN_ID + " > " + currentPictureId;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Pictures picture = null;

        if (cursor.moveToFirst()) {
           picture = new Pictures(cursor.getString(cursor.getColumnIndex(PICTURE_COLUMN_FILENAME)), null);
            picture.setId(cursor.getInt(cursor.getColumnIndex(PICTURE_COLUMN_ID)));
            picture.setStored();
            picture.setName(cursor.getString(cursor.getColumnIndex(PICTURE_COLUMN_PICTURENAME)));
        }

        cursor.close();
        db.close();

        return picture;
    }
*/
}