package com.example.photosapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.photosapplication.model.Album;
import com.example.photosapplication.model.Photo;
import com.example.photosapplication.util.PhotoAdapter;

public class AlbumDetails extends AppCompatActivity {

    Album album;
    RecyclerView recyclerView;
    Button addPhotosButton;
    private ActivityResultLauncher<Intent> pickImageLauncher;

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

        recyclerView = findViewById(R.id.photosRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        PhotoAdapter adapter = new PhotoAdapter(album.getPhotos(), this);
        recyclerView.setAdapter(adapter);

        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri uri = data.getData();
                            Photo photo = new Photo(uri);
                            album.addPhoto(photo);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
        );

        addPhotosButton = findViewById(R.id.addPhotosButton);
        addPhotosButton.setOnClickListener(v -> addPhoto());
    }

    private void addPhoto() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        pickImageLauncher.launch(intent);
    }
}