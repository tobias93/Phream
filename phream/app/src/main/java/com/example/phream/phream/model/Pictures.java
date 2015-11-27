package com.example.phream.phream.model;

import android.graphics.Bitmap;

/**
 * Created by Philipp Pütz on 23.10.2015.
 */
public class Pictures {
    private int id;
    private boolean stored = false;
    private String name;
    private String filename;
    private long created;
    private int stream;

    public Pictures(String filename, Stream stream){
        this.filename = filename;
    }

    public void addToGallery(){

    }

    // Getter & Setter

    public int getId() {
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilename() {
        return filename;
    }

    public long getCreated() {
        return created;
    }

    public int getStream() {
        return stream;
    }

    public void setStored() {
        this.stored = true;
    }

    public void setNotStored() {
        this.stored = false;
    }

    public boolean getStored() {
        return stored;
    }



}
