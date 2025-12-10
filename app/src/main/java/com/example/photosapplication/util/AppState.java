package com.example.photosapplication.util;

import android.net.Uri;

import com.example.photosapplication.model.Album;
import com.example.photosapplication.model.Photo;
import com.example.photosapplication.model.TagType;

import java.util.ArrayList;
import java.util.List;

public class AppState {
    private static final String TAG = "AppState";
    private final List<Album> albums;
    private transient final List<TagType> tagTypes;

    public AppState() {
        albums = new ArrayList<>();
        tagTypes = new ArrayList<>();
        initializeTagTypes();
    }

    /**
     * Initializes the predefined tag types.
     */
    private void initializeTagTypes() {
        tagTypes.add(TagType.LOCATION);
        tagTypes.add(TagType.PERSON);
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public List<TagType> getTagTypes() {
        return tagTypes;
    }

    public Album getAlbumByName(String name) {
        for (Album a : albums) {
            if (a.getName().equals(name)) return a;
        }
        return null;
    }

    public Photo getPhotoByUri(Uri photoUri) {
        for (Album album : albums) {
            for (Photo photo : album.getPhotos()) {
                if (photo.getUri().equals(photoUri)) {
                    return photo;
                }
            }
        }
        return null;
    }

    public Album getAlbumOfPhoto(Photo photo) {
        for (Album album : albums) {
            if (album.getPhotos().contains(photo)) {
                return album;
            }
        }
        return null;
    }
}
