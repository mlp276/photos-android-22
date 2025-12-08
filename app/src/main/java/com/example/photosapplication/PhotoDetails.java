package com.example.photosapplication;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.photosapplication.model.Album;
import com.example.photosapplication.model.Photo;
import com.example.photosapplication.util.AppState;
import com.example.photosapplication.util.ImageUtil;

import java.io.File;
import java.util.Objects;

public class PhotoDetails extends AppCompatActivity {

    AppState currentState;
    Album album;
    Photo photo;
    int position;
    ImageView displayedPhotoImageview;
    TextView photoTitleTextView;
    Button nextButton;
    Button previousButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_photo_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get the photo that was opened from the album details page
        String albumName = getIntent().getStringExtra("albumName");
        position = getIntent().getIntExtra("photoPosition", 0);
        currentState = ((PhotosApplication) getApplication()).getAppState();
        album = currentState.getAlbumByName(albumName);
        photo = album.getPhoto(position);

        // Get the id of the views related to the photo and use them to display it
        displayedPhotoImageview = findViewById(R.id.displayedPhotoImageview);
        photoTitleTextView = findViewById(R.id.photoTitleTextView);
        displayPhoto(photo);

        // Set the buttons to scroll between photos in the album
        nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(v -> displayNextPhoto());
        previousButton = findViewById(R.id.previousButton);
        previousButton.setOnClickListener(v -> displayPreviousPhoto());

    }

    private void displayPhoto(Photo photo) {
        displayedPhotoImageview.setImageURI(photo.getUri());
        photoTitleTextView.setText(ImageUtil.getFileName(this, photo.getUri()));
    }

    private void displayNextPhoto() {
        Photo nextPhoto = album.getNextPhoto(position);
        if (nextPhoto == null) {
            return;
        }

        photo = nextPhoto;
        position = position + 1;
        displayPhoto(photo);
    }

    private void displayPreviousPhoto() {
        Photo previousPhoto = album.getPreviousPhoto(position);
        if (previousPhoto == null) {
            return;
        }

        photo = previousPhoto;
        position = position - 1;
        displayPhoto(photo);
    }
}