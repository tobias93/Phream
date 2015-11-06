package com.example.phream.phream.model;

import com.example.phream.phream.model.database.DBHandler;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Philipp Pütz on 23.10.2015.
 */
public class Model {

    private ArrayList<Stream> streamList = new ArrayList<>();

    // Create Streamobjects
    public ArrayList<Stream> getAllStreams() {
        DBHandler dbHandler = new DBHandler(null, null, null, 1); // Todo Set Context
        ArrayList<HashMap<String, String>> streamList = dbHandler.getAllStreams();

        for (int i = 0; i < streamList.size(); i++) {
            this.streamList.add(new Stream(Integer.parseInt(streamList.get(i).get("id")), streamList.get(i).get("name"),
                    Long.parseLong(streamList.get(i).get("created"))));
        }

        return this.streamList;
    }

}
