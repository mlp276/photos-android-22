package com.example.photosapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.photosapplication.model.Album;
import com.example.photosapplication.model.util.UniqueList;

public class AlbumDetails extends AppCompatActivity implements PageView {

    Album album;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_album_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        album = getIntent().getSerializableExtra("album_object", Album.class);
        if (album == null) {
            Toast.makeText(this, "Error: Album not found.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        Log.d("AlbumDetails", album.getName());

        refreshView();
    }

    public void refreshView() {

    }
}