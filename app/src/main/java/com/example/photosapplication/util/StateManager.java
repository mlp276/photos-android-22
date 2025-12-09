package com.example.photosapplication.util;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class StateManager {
    private static final String TAG = "StateManager";
    private static final Gson gson = new Gson();
    private static final String FILE_NAME = "app_state.json";

    public static void saveState(Context context, AppState appState) {
        String json = gson.toJson(appState);

        try (FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)) {
            fos.write(json.getBytes());
        } catch (Exception e) {
            Log.e(TAG, "Could not save the app session.");
        }
    }

    public static AppState loadState(Context context) {
        try {
            FileInputStream fis = context.openFileInput(FILE_NAME);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            return gson.fromJson(builder.toString(), AppState.class);

        } catch (Exception e) {
            Log.e(TAG, "Could not load the app session.");
            return null;
        }
    }
}