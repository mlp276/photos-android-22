package com.example.photosapplication;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.photosapplication.model.Album;
import com.example.photosapplication.model.TagType;
import com.example.photosapplication.util.AppState;
import com.example.photosapplication.util.StateManager;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {
    private static final String TAG = "Home";
    private static final String[] options = {"Open Album", "Rename Album", "Remove Album"};

    List<Album> albums;
    ListView albumListView;
    List<String> albumDisplayNames;
    Button addAlbumsButton;
    Button searchTagsButton;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        AppState currentState = ((PhotosApplication) getApplication()).getAppState();
        albums = currentState.getAlbums();

        albumListView = findViewById(R.id.albumListView);
        albumDisplayNames = new ArrayList<String>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, albumDisplayNames);
        albumListView.setAdapter(adapter);

        albumListView.setOnItemClickListener((parent, view, position, id) -> {
            showAlbumsPopupMenu(view, position);
        });

        addAlbumsButton = findViewById(R.id.addAlbumsButton);
        addAlbumsButton.setOnClickListener(v -> addAlbum());

        searchTagsButton = findViewById(R.id.searchTagsButton);
        searchTagsButton.setOnClickListener(v -> showSearchTagTypeDialog());

        refreshView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        PhotosApplication app = (PhotosApplication) getApplication();
        StateManager.saveState(this, app.getAppState());
    }

    private void showAlbumsPopupMenu(View anchor, int position) {
        Album album = albums.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Album Options");

        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0:
                    openAlbum(album);
                    break;
                case 1:
                    renameAlbum(album);
                    break;
                case 2:
                    removeAlbum(album);
                    break;
            }
        });

        builder.show();
    }

    private void renameAlbum(Album album) {
        EditText input = new EditText(this);
        input.setText(album.getName());

        new AlertDialog.Builder(this)
                .setTitle("Rename Album")
                .setView(input)
                .setPositiveButton("OK", (d, w) -> {
                    String originalName = album.getName();
                    String newName = input.getText().toString();
                    album.setName(input.getText().toString());
                    Log.d(TAG, "Renamed album from " + originalName + " to " + newName + ".");
                    refreshView();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void removeAlbum(Album album) {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete this album?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    albums.remove(album);
                    Log.d(TAG, "Successfully removed album, now there are " + albums.size() + " albums.");
                    refreshView();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void openAlbum(Album album) {
        Log.d(TAG, "Opened album: " + album.getName());
        Intent intent = new Intent(this, AlbumDetails.class);
        intent.putExtra("albumName", album.getName());
        startActivity(intent);
    }

    private void addAlbum() {
        EditText input = new EditText(this);
        input.setHint("Album name");

        new AlertDialog.Builder(this)
                .setTitle("Add New Album")
                .setView(input)
                .setPositiveButton("Add", (dialog, which) -> {
                    String name = input.getText().toString().trim();
                    if (name.isEmpty() || !albums.add(new Album(name))) {
                        new AlertDialog.Builder(this)
                                .setTitle("Error Adding Album")
                                .setMessage("Could not add the album.")
                                .setPositiveButton("OK", null)
                                .show();
                    }
                    Log.d(TAG, "Added album " + name + ", now there are " + albums.size() + " albums.");
                    refreshView();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showSearchTagTypeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Search by Tag Type");

        String[] tagTypes = {TagType.PERSON.getName(), TagType.LOCATION.getName()};
        builder.setItems(tagTypes, (dialog, which) -> {
            showSearchTagValueDialog(tagTypes[which]);
        });

        builder.show();
    }

    private void showSearchTagValueDialog(String tagType) {
        EditText input = new EditText(this);
        input.setHint("Tag value");

        new AlertDialog.Builder(this)
                .setTitle("Search for " + tagType)
                .setView(input)
                .setPositiveButton("Search", (dialog, which) -> {
                    String tagValue = input.getText().toString().trim();
                    Intent intent = new Intent(this, SearchTagResults.class);
                    intent.putExtra("tagType", tagType);
                    intent.putExtra("tagValue", tagValue);
                    startActivity(intent);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    public void refreshView() {
        albumDisplayNames.clear();
        for (Album a : albums) {
            albumDisplayNames.add(a.getName());
        }
        adapter.notifyDataSetChanged();
    }
}
