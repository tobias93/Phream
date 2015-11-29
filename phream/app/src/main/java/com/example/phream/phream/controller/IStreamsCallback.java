package com.example.phream.phream.controller;

import com.example.phream.phream.model.Stream;

/**
 * Created by tobias on 06.11.15.
 */
public interface IStreamsCallback {
    void onStreamCreated(Stream stream);
    void onStreamUpdated(Stream stream);
    void onStreamDeleted(Stream stream);
    void onStreamListAvailable(Stream[] streams);
    void onStreamCreationError(Stream stream);
    void onStreamUpdateError(Stream stream);
    void onStreamDeletionError(Stream stream);
}
