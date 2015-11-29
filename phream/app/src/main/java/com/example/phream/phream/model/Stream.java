package com.example.phream.phream.model;

public class Stream {
    private long id;
    private String name;
    private long created;

    public Stream( long id, String name, long created){
        this.name = name;
        this.id = id;
        this.created = created;
    }

    public Stream(String name){
        this.name = name;
        created = System.currentTimeMillis();
        id = -2;
    }

    // Getter & Setter
    public long getId(){
        return id;
    }

    public void setId(long value){
        this.id = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }
}
