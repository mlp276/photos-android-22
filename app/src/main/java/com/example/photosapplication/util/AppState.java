package com.example.photosapplication.util;

import android.util.Log;

import com.example.photosapplication.model.Album;
import com.example.photosapplication.model.TagType;

import java.util.List;

public class AppState {
    private static final String TAG = "AppState";
    private final List<Album> albums;
    private transient final List<TagType> tagTypes;

    public AppState() {
        albums = new UniqueList<Album>();
        tagTypes = new UniqueList<TagType>();
        initializeTagTypes();
    }

    /**
     * Initializes the tag types of the user
     */
    private void initializeTagTypes() {
        if (!(addTagType("location", false) &&
        addTagType("person", true))) {
            Log.e(TAG, "Failed to load in the tag types.");
        }
    }

    /**
     * Add a tag type of the user
     *
     * @param tagTypeName the name of the tag type
     * @param multiValued whether the tag type will have multiple values associated with it
     * @return true if the tag type is successfully added
     */
    public boolean addTagType(String tagTypeName, boolean multiValued) {
        return tagTypes.add(new TagType(tagTypeName, multiValued));
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
}