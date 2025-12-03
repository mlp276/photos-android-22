package com.example.photosapplication;

import com.example.photosapplication.model.Album;

import java.util.ArrayList;
import java.util.List;

public class AppState {
    private final List<Album> albums = new ArrayList<>();
    public List<Album> getAlbums() {
        return albums;
    }
}