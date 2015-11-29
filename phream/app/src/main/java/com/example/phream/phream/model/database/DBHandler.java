package com.example.phream.phream.model.database;

/**
 * Created by Philipp Pütz on 23.10.2015.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.phream.phream.model.database.Tables.TblPicture;
import com.example.phream.phream.model.database.Tables.TblStream;

public class DBHandler extends SQLiteOpenHelper {

    // Constructor
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
}