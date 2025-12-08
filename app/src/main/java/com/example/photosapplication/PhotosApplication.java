package com.example.photosapplication;

import android.app.Application;

import com.example.photosapplication.util.AppState;

public class PhotosApplication extends Application {
    private AppState appState;

    @Override
    public void onCreate() {
        super.onCreate();
        appState = new AppState();
    }

    public AppState getAppState() {
        return appState;
    }
}