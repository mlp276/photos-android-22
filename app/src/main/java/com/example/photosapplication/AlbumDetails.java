package com.example.photosapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.example.photosapplication.util.AppState;
import com.example.photosapplication.util.PhotoAdapter;
import com.example.photosapplication.util.StateManager;

public class AlbumDetails extends AppCompatActivity {
    private static final String TAG = "AlbumDetails";

    Album album;
    RecyclerView recyclerView;
    Button addPhotosButton;
    private PhotoAdapter adapter;
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

        // Get the album that was opened from the home page
        String albumName = getIntent().getStringExtra("albumName");
        AppState currentState = ((PhotosApplication) getApplication()).getAppState();
        album = currentState.getAlbumByName(albumName);

        // Set the custom Photo Adapter class to the photos view
        recyclerView = findViewById(R.id.photosRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new PhotoAdapter(this, album);
        recyclerView.setAdapter(adapter);

        // Provide functionality to the 'Add Photo' button
        addPhotosButton = findViewById(R.id.addPhotosButton);
        addPhotosButton.setOnClickListener(v -> addPhoto());

        // Provide functionality to retrieve photos from the storage to add
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data == null) {
                            new AlertDialog.Builder(this)
                                    .setTitle("Error Adding Photo")
                                    .setMessage("Could not add the photo.")
                                    .setPositiveButton("OK", null)
                                    .show();
                            return;
                        }

                        Uri uri = data.getData();
                        if (uri == null) {
                            new AlertDialog.Builder(this)
                                    .setTitle("Error Adding Photo")
                                    .setMessage("Could not add the photo.")
                                    .setPositiveButton("OK", null)
                                    .show();
                            return;
                        }
                        getContentResolver().takePersistableUriPermission(
                                uri,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION
                        );

                        Photo photo = new Photo(uri);
                        album.addPhoto(photo);
                        Log.d(TAG, "Added photo located at " + photo.getUri());
                        adapter.notifyDataSetChanged();
                    }
                }
        );
    }

    @Override
    protected void onPause() {
        super.onPause();
        PhotosApplication app = (PhotosApplication) getApplication();
        StateManager.saveState(this, app.getAppState());
    }

    private void addPhoto() {
        // Create an intent to get the photo from the storage
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        pickImageLauncher.launch(intent);
    }
}