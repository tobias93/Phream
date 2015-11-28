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
/*
    public void addPicture(Picture picture){
        DBHandler dbHandler = new DBHandler(null, null, null, 1); // Todo Set Context
        dbHandler.insertPicture(picture.getName(), picture.getCreated(), picture.getFilepath(), id);
        picture.setStored();
    }*/

    public Picture getPicture(){
     return null;
    }
/*
    public void delete(Picture picture){
        if (picture.getStored() == true){
            DBHandler dbHandler = new DBHandler(null, null, null, 1); // Todo Set Context
            dbHandler.deletePicture(picture.getId());
            picture.setNotStored();
        }

    }*/
/*
    public void renamePicture(Picture picture){

    }*/


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
