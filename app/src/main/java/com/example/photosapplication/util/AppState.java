package com.example.photosapplication.util;

import com.example.photosapplication.model.Album;

import java.util.ArrayList;
import java.util.List;

public class AppState {
    private List<Album> albums;

    public AppState() {
        albums = new ArrayList<>();
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