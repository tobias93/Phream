package com.example.phream.phream.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {

    private static Context context;
    private static DBHandler handler;
    private static SQLiteDatabase db;

    public static void init(Context context)
    {
        DBManager.context = context.getApplicationContext();
    }

    public static synchronized SQLiteDatabase getDB() throws NullPointerException
    {
        if (db == null)
        {
            handler = new DBHandler(DBManager.context);
            db = handler.getWritableDatabase();
        }
        return db;
    }

}
