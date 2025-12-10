package com.example.photosapplication;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.helper.widget.Flow;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.photosapplication.model.Album;
import com.example.photosapplication.model.Photo;
import com.example.photosapplication.model.Tag;
import com.example.photosapplication.model.TagType;
import com.example.photosapplication.util.AppState;
import com.example.photosapplication.util.ImageUtil;
import com.example.photosapplication.util.StateManager;

import java.util.ArrayList;
import java.util.List;

public class PhotoDetails extends AppCompatActivity {

    AppState currentState;
    Album album;
    Photo photo;
    int position;
    ImageView displayedPhotoImageview;
    TextView photoTitleTextView;
    Button nextButton;
    Button previousButton;
    Button addTagButton;
    Button removeTagButton;
    Flow tagsFlow;
    private List<View> tagViews = new ArrayList<>();

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

        String albumName = getIntent().getStringExtra("albumName");
        position = getIntent().getIntExtra("photoPosition", 0);
        currentState = ((PhotosApplication) getApplication()).getAppState();
        album = currentState.getAlbumByName(albumName);
        photo = album.getPhoto(position);

        displayedPhotoImageview = findViewById(R.id.displayedPhotoImageview);
        photoTitleTextView = findViewById(R.id.photoTitleTextView);
        displayPhoto(photo);

        nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(v -> displayNextPhoto());
        previousButton = findViewById(R.id.previousButton);
        previousButton.setOnClickListener(v -> displayPreviousPhoto());

        addTagButton = findViewById(R.id.addTagButton);
        addTagButton.setOnClickListener(this::showAddTagPopup);
        removeTagButton = findViewById(R.id.removeTagButton);
        removeTagButton.setOnClickListener(this::showRemoveTagPopup);
        tagsFlow = findViewById(R.id.tagsFlow);
        refreshTags();
    }

    @Override
    protected void onPause() {
        super.onPause();
        PhotosApplication app = (PhotosApplication) getApplication();
        StateManager.saveState(this, app.getAppState());
    }

    private void displayPhoto(Photo photo) {
        displayedPhotoImageview.setImageURI(photo.getUri());
        photoTitleTextView.setText(ImageUtil.getFileName(this, photo.getUri()));
    }

    private void displayNextPhoto() {
        Photo nextPhoto = album.getNextPhoto(position);
        if (nextPhoto == null) return;
        photo = nextPhoto;
        position++;
        displayPhoto(photo);
        refreshTags();
    }

    private void displayPreviousPhoto() {
        Photo previousPhoto = album.getPreviousPhoto(position);
        if (previousPhoto == null) return;
        photo = previousPhoto;
        position--;
        displayPhoto(photo);
        refreshTags();
    }

    private void showAddTagPopup(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenu().add(TagType.PERSON.getName());
        popup.getMenu().add(TagType.LOCATION.getName());
        popup.setOnMenuItemClickListener(item -> {
            showAddTagDialog(item.getTitle().toString());
            return true;
        });
        popup.show();
    }

    private void showAddTagDialog(String tagType) {
        EditText input = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("Add " + tagType + " Tag")
                .setView(input)
                .setPositiveButton("OK", (dialog, which) -> {
                    String value = input.getText().toString();
                    try {
                        photo.addTag(tagType, value);
                        refreshTags();
                    } catch (Exception e) {
                        new AlertDialog.Builder(this)
                                .setTitle("Error")
                                .setMessage(e.getMessage())
                                .setPositiveButton("OK", null)
                                .show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showRemoveTagPopup(View view) {
        if (photo.getTags() == null || photo.getTags().isEmpty()) {
            return;
        }

        PopupMenu popup = new PopupMenu(this, view);
        for (Tag tag : photo.getTags()) {
            popup.getMenu().add(tag.toString());
        }

        popup.setOnMenuItemClickListener(item -> {
            String selectedTag = item.getTitle().toString();
            String[] parts = selectedTag.split(": ");
            photo.removeTag(parts[0], parts[1]);
            refreshTags();
            return true;
        });

        popup.show();
    }

    private void refreshTags() {
        ConstraintLayout layout = findViewById(R.id.main);

        for (View view : tagViews) {
            layout.removeView(view);
        }
        tagViews.clear();

        if (photo.getTags() == null || photo.getTags().isEmpty()) {
            tagsFlow.setReferencedIds(new int[0]);
            removeTagButton.setEnabled(false);
            return;
        }

        removeTagButton.setEnabled(true);

        int[] tagViewIds = new int[photo.getTags().size()];
        for (int i = 0; i < photo.getTags().size(); i++) {
            Tag tag = photo.getTags().get(i);
            TextView tagView = new TextView(this);
            tagView.setId(View.generateViewId());
            tagView.setText(tag.toString());
            tagView.setBackgroundResource(R.drawable.tag_background);
            layout.addView(tagView);
            tagViews.add(tagView);
            tagViewIds[i] = tagView.getId();
        }

        tagsFlow.setReferencedIds(tagViewIds);
    }
}
