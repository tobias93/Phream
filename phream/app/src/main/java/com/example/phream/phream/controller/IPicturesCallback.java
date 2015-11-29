package com.example.phream.phream.controller;

import com.example.phream.phream.model.Picture;

public interface IPicturesCallback {
    void onPicturesListUpdated(Picture[] pictures);
    void onPictureCreated(Picture picture);
    void onPictureCreatedError(Picture picture);
    void onPictureDeleted();
    void onPictureDeletedError(Picture picture);
    void onPictureUpdated();
    void onPictureUpdatedError(Picture picture);
}
