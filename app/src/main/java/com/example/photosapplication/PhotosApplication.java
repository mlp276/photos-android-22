package com.example.photosapplication;

import android.app.Application;

import com.example.photosapplication.util.AppState;
import com.example.photosapplication.util.StateManager;

import java.util.Objects;

public class PhotosApplication extends Application {
    private AppState appState;

    @Override
    public void onCreate() {
        super.onCreate();
        AppState loaded = StateManager.loadState(getApplicationContext());
        appState = Objects.requireNonNullElseGet(loaded, AppState::new);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        StateManager.saveState(getApplicationContext(), appState);
    }

    public AppState getAppState() {
        return appState;
    }
}