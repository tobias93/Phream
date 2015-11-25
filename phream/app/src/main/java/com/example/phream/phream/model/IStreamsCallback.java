package com.example.phream.phream.model;

/**
 * Created by tobias on 06.11.15.
 */
public interface IStreamsCallback {
    void onStreamCreated(Stream stream);
    void onStreamUpdated(Stream stream);
    void onStreamDeleted(Stream stream);
    void onStreamListAviable(Stream[] streams);
    void onStreamCreationError(Stream stream);
    void onStreamUpdateError(Stream stream);
    void onStreamDeletionError(Stream stream);
}
