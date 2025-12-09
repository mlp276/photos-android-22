package com.example.photosapplication.util;

import com.example.photosapplication.model.Album;

import java.util.List;

public class AppState {
    private final List<Album> albums;

    public AppState() {
        albums = new UniqueList<Album>();
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public Album getAlbumByName(String name) {
        for (Album a : albums) {
            if (a.getName().equals(name)) return a;
        }
        return null;
    }
}