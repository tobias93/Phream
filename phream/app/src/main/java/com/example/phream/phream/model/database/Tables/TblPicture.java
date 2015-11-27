package com.example.phream.phream.model.database.Tables;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.phream.phream.model.Pictures;

import java.util.ArrayList;

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
                pictures[i] = new Pictures (cursor.getLong(cursor.getColumnIndex(COLUMN_ID)),
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


}
