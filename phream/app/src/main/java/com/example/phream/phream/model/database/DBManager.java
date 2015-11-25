package com.example.phream.phream.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by tobias on 10.11.15.
 */
public class DBManager {

    private static DBHandler handler;
    private static SQLiteDatabase db;

    public static void init(Context context)
    {
        if (handler == null)
        {
            handler = new DBHandler(context);
        }
    }

    public static SQLiteDatabase getDB() throws NullPointerException
    {
        if (db == null)
        {
            db = handler.getWritableDatabase();
        }
        return db;
    }

}
