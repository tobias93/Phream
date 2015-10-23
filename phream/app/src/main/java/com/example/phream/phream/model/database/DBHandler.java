package com.example.phream.phream.model.database;

/**
 * Created by Philipp Pütz on 23.10.2015.
 */

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.content.Context;
import android.content.ContentValues;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;

public class DBHandler extends SQLiteOpenHelper {

    // Database
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "phream.db";

    // Table stream
    private static final String TABLE_STREAM = "stream";
    public static final String STREAM_COLUMN_ID = "streamid";
    public static final String STREAM_COLUMN_STREAMNAME = "streamname";
    public static final String STREAM_COLUMN_CREATED = "created";

    // Table picture
    private static final String TABLE_PICTURE = "picture";
    public static final String PICTURE_COLUMN_ID = "pictureid";
    public static final String PICTURE_COLUMN_PICTURENAME = "picturename";
    public static final String PICTURE_COLUMN_CREATED = "created";
    public static final String PICTURE_COLUMN_FILENAME = "filename";
    public static final String PICTURE_COLUMN_STREAM = "streamid";

    // Construktor
    public DBHandler(Context context, String name,
                     SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Tables
        String CREATE_STREAM_TABLE = "CREATE TABLE " +
                TABLE_STREAM + "("
                + STREAM_COLUMN_ID + " INTEGER PRIMARY KEY," + STREAM_COLUMN_STREAMNAME
                + " TEXT," + STREAM_COLUMN_CREATED + " INTEGER" + ")";

        db.execSQL(CREATE_STREAM_TABLE);

        String CREATE_PICTURE_TABLE = "CREATE TABLE " +
                TABLE_PICTURE + "("
                + PICTURE_COLUMN_ID + " INTEGER PRIMARY KEY," + PICTURE_COLUMN_PICTURENAME
                + " TEXT," + PICTURE_COLUMN_CREATED + " INTEGER," + PICTURE_COLUMN_FILENAME + " TEXT," +
                PICTURE_COLUMN_STREAM + " INTEGER" + ")";

        db.execSQL(CREATE_PICTURE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_STREAM);
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_PICTURE);
        //onCreate(db);
    }

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
    public void renamePicture(int id, String picturename){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(PICTURE_COLUMN_PICTURENAME, picturename);

        db.update(TABLE_PICTURE, values, PICTURE_COLUMN_ID + "= ?", new String[] { String.valueOf(id) });
        db.close();
    }

    // Update / Rename - Methods
    public void renameStream(int id, String streamname){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(STREAM_COLUMN_STREAMNAME, streamname);

        db.update(TABLE_STREAM, values, STREAM_COLUMN_ID + "= ?", new String[] { String.valueOf(id) });
        db.close();
    }

    public ArrayList<HashMap<String, String>> getAllStreams(){
        String query = "Select * FROM " + TABLE_STREAM;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        ArrayList<HashMap<String, String>> streamList = new ArrayList<HashMap<String, String>>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> streams = new HashMap<String, String>();
                streams.put("id", cursor.getString(cursor.getColumnIndex(STREAM_COLUMN_ID)));
                streams.put("name", cursor.getString(cursor.getColumnIndex(STREAM_COLUMN_STREAMNAME)));
                streams.put("created", cursor.getString(cursor.getColumnIndex(STREAM_COLUMN_CREATED)));
                streamList.add(streams);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return streamList;
    }

}