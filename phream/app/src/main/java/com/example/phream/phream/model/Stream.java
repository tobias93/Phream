package com.example.phream.phream.model;

public class Stream {
    private int id;
    private String name;
    private long created;

    public Stream( int id, String name, long created){
        this.name = name;
        this.id = id;
        this.created = created;
    }

    public Stream(String name){
        this.name = name;
        created = System.currentTimeMillis();
    }
/*
    public void addPicture(Pictures picture){
        DBHandler dbHandler = new DBHandler(null, null, null, 1); // Todo Set Context
        dbHandler.insertPicture(picture.getName(), picture.getCreated(), picture.getFilename(), id);
        picture.setStored();
    }*/

    public Pictures getPicture(){
     return null;
    }
/*
    public void delete(Pictures picture){
        if (picture.getStored() == true){
            DBHandler dbHandler = new DBHandler(null, null, null, 1); // Todo Set Context
            dbHandler.deletePicture(picture.getId());
            picture.setNotStored();
        }

    }*/
/*
    public void renamePicture(Pictures picture){

    }*/


    // Getter & Setter
    public int getId(){
        return id;
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
