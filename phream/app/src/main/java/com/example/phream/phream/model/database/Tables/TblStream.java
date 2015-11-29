package com.example.phream.phream.model.database.Tables;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.phream.phream.model.Stream;

public class TblStream {
    public static final String TABLE_NAME = "stream";
    public static final String COLUMN_ID = "streamid";
    public static final String COLUMN_STREAMNAME = "streamname";
    public static final String COLUMN_CREATED = "created";
    public static final String QUERY_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " + COLUMN_ID + " INTEGER PRIMARY KEY, " + COLUMN_STREAMNAME + " TEXT, " + COLUMN_CREATED + " INTEGER)";
    public static final String QUERY_UPDATE = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_CREATED + " DESC";

    public static void insert(SQLiteDatabase db, Stream stream) throws Exception
    {
        ContentValues values = new ContentValues();
        values.put(COLUMN_STREAMNAME, stream.getName());
        values.put(COLUMN_CREATED, stream.getCreated());
        long id = db.insertOrThrow(TABLE_NAME, null, values);
        stream.setId(id);
    }

    public static void update(SQLiteDatabase db, Stream stream)
    {
        ContentValues values = new ContentValues();
        values.put(COLUMN_STREAMNAME, stream.getName());
        values.put(COLUMN_CREATED, stream.getCreated());
        db.update(TABLE_NAME, values, COLUMN_ID + " = " + String.valueOf(stream.getId()), new String[0]);
    }

    public static Stream[] findAll(SQLiteDatabase db)
    {
        Cursor cursor = db.rawQuery(QUERY_UPDATE, null);
        Stream[] streams = new Stream[cursor.getCount()];

        if (cursor.moveToFirst()) {
            int i = 0;
            do {
                streams[i] = new Stream(
                        cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_STREAMNAME)),
                        cursor.getLong(cursor.getColumnIndex(COLUMN_CREATED))
                );
                ++i;
            } while (cursor.moveToNext());
        }

        cursor.close();
        return streams;
    }

    public static void deleteStream(SQLiteDatabase db, long id){
        String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " =  " + id;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            db.delete(TABLE_NAME, COLUMN_ID + "= ?", new String[]{String.valueOf(id)});
            cursor.close();
        }

    }
}
