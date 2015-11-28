package com.example.phream.phream.model;

import android.net.Uri;

import java.io.InputStream;

public class Picture {
    private long id;
    private String name;
    private Uri importUri = null;
    private String filepath;
    private long created;
    private long streamId;

    public Picture(long pictureId, String pictureName, String filepath, long created, long streamid) {
        this.id = pictureId;
        this.name = pictureName;
        this.filepath = filepath;
        this.created = created;
        this.streamId = streamid;
    }

    public Picture(String pictureName, String filepath, long created) {
        this.name = pictureName;
        this.filepath = filepath;
        this.created = created;
    }

    public Picture(String pictureName) {
        this.name = pictureName;
    }


    // Getter & Setter

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setStreamId(long id) {
        this.streamId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setCreated(long created) {
        this.created = created;
    }


    public long getCreated() {
        return created;
    }

    public long getStream() {
        return streamId;
    }

    public void setImportUri(Uri value) {
        this.importUri = value;
    }

    public Uri getImportUri() {
        return this.importUri;
    }


}
