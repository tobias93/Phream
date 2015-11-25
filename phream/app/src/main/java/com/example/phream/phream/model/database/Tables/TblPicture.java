package com.example.phream.phream.model.database.Tables;

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
}
