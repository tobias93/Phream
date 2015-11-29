package com.example.phream.phream.controller;

import com.example.phream.phream.model.Picture;

/**
 * Created by tobias on 06.11.15.
 */
public interface IPicturesCallback {
    void onPicturesListUpdated(Picture[] pictures);
    void onPictureCreated(Picture picture);
    void onPictureCreatedError(Picture picture);
    void onPictureDeleted();
    void onPictureDeletedError(Picture picture);
    void onPictureUpdated();
    void onPictureUpdatedError(Picture picture);
}
