package com.example.phream.phream.model.database.Tables;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.phream.phream.model.Pictures;

/**
 * Created by tobias on 10.11.15.
 */
public class TblPicture {
    public static final String TABLE_NAME = "picture";
    public static final String COLUMN_ID = "pictureid";
    public static final String COLUMN_PICTURENAME = "picturename";
    public static final String COLUMN_CREATED = "created";
    public static final String COLUMN_FILENAME = "filename";
    public static final String COLUMN_STREAM = "streamid";
    public static final String QUERY_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_PICTURENAME + " TEXT," + COLUMN_CREATED + " INTEGER," + COLUMN_FILENAME + " TEXT," + COLUMN_STREAM + " INTEGER" + ")";

    public static Pictures[] getAllPictures(SQLiteDatabase db, long streamId) {

        String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_STREAM + " = " + streamId;

        Cursor cursor = db.rawQuery(query, null);
        Pictures[] pictures = new Pictures[cursor.getCount()];

        if (cursor.moveToFirst()) {
            int i = 0;
            do {
                pictures[i] = new Pictures(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_PICTURENAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_FILENAME)),
                        cursor.getLong(cursor.getColumnIndex(COLUMN_CREATED)),
                        cursor.getLong(cursor.getColumnIndex(COLUMN_STREAM)));
                ++i;
            } while (cursor.moveToNext());
        }

        cursor.close();
        return pictures;
    }

    public static void insertPicture(SQLiteDatabase db, Pictures picture) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_PICTURENAME, picture.getName());
        values.put(COLUMN_CREATED, picture.getCreated());
        values.put(COLUMN_FILENAME, picture.getFilepath());
        values.put(COLUMN_STREAM, picture.getStream());

        long id = db.insertOrThrow(TABLE_NAME, null, values);
        picture.setId(id);
        db.close();
    }

    public static void deletePicture(SQLiteDatabase db, Pictures picture) {

        String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " =  " + picture.getId();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            db.delete(TABLE_NAME, COLUMN_ID + "= ?", new String[]{String.valueOf(picture.getId())});
            cursor.close();
        }
        db.close();
    }

    public static void updatePicture(SQLiteDatabase db, Pictures picture) {
        ContentValues values = new ContentValues();

        values.put(COLUMN_PICTURENAME, picture.getName());
        values.put(COLUMN_CREATED, picture.getCreated());
        values.put(COLUMN_FILENAME, picture.getFilepath());
        values.put(COLUMN_STREAM, picture.getStream());

        db.update(TABLE_NAME, values, COLUMN_ID + "= ?", new String[]{String.valueOf(picture.getId())});
        db.close();
    }

}
