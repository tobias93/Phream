package com.example.phream.phream.model;

import com.example.phream.phream.model.database.DBHandler;

import java.util.ArrayList;

/**
 * Created by Philipp Pütz on 23.10.2015.
 */
public class Model {

    private ArrayList<Stream> streamList = new ArrayList<>();

    // Create Streamobjects
    public ArrayList<Stream> getAllStreams() {
        DBHandler dbHandler = new DBHandler(null, null, null, 1); // Todo Set Context
        return dbHandler.getAllStreams();
    }

}
