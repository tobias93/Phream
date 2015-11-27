package com.example.phream.phream.model;

import android.graphics.Bitmap;

/**
 * Created by Philipp Pütz on 23.10.2015.
 */
public class Pictures {
    private long id;
    private String name;
    private String filename;
    private long created;
    private long streamId;

    public Pictures(long pictureId, String pictureName, String filename, long created, long streamid){
        this.id = pictureId;
        this.name = pictureName;
        this.filename = filename;
        this.created = created;
        this.streamId = streamid;
    }


    // Getter & Setter

    public long getId() {
        return id;
    }

    public void setId(long id){
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

    public long getStream() {
        return streamId;
    }



}
