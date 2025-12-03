package com.example.photosapplication;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class StateManager {
    private static final String FILENAME = "app_state.json";
    private static final Gson gson = new Gson();
    private static final String TAG = "StateManager";

    public static void save(Context context, AppState state) {
        try (FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE)) {
            String json = gson.toJson(state);
            fos.write(json.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            Log.e(TAG, "Could not save the app session.");
        }
    }

    public static AppState load(Context context) {
        try (FileInputStream fis = context.openFileInput(FILENAME)) {
            String json = new String(fis.readAllBytes(), StandardCharsets.UTF_8);
            return gson.fromJson(json, AppState.class);
        } catch (IOException e) {
            return new AppState();  // first launch or missing file
        }
    }
}