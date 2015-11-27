package com.example.phream.phream.model;

import java.util.ArrayList;

/**
 * Created by tobias on 06.11.15.
 */
public interface IPicturesCallback {
    void onPicturesListUpdated(Pictures[] pictures);
    void onPictureCreated(Pictures picture);
    void onPictureCreatedError(Pictures picture);
}
