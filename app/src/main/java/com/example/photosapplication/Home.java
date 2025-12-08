package com.example.photosapplication;

import static java.util.stream.Collectors.toList;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.photosapplication.model.Album;
import com.example.photosapplication.model.UniqueList;
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

        // Retrieve the saved state of the application
        AppState currentState = ((PhotosApplication) getApplication()).getAppState();
        albums = currentState.getAlbums();

        // Set the array adapter to the albums list view
        albumListView = findViewById(R.id.albumListView);
        albumDisplayNames = new ArrayList<String>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, albumDisplayNames);
        albumListView.setAdapter(adapter);

        // Provide functionality to each item of the list of albums
        albumListView.setOnItemClickListener((parent, view, position, id) -> {
            showAlbumsPopupMenu(view, position);
        });

        // Provide functionality to the 'Add Album' button
        addAlbumsButton = findViewById(R.id.addAlbumsButton);
        addAlbumsButton.setOnClickListener(v -> addAlbum());

        refreshView();
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

        // Initialize the alert dialog to prompt for renaming the album
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
        // Initialize the alert dialog to prompt confirmation of album removal
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
        // Create an intent to switch activities to open the album
        Log.d(TAG, "Opened album: " + album.getName());
        Intent intent = new Intent(this, AlbumDetails.class);
        intent.putExtra("albumName", album.getName());
        startActivity(intent);
    }

    private void addAlbum() {
        EditText input = new EditText(this);
        input.setHint("Album name");

        // Initialize the alert dialog to prompt for the new album
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

    public void refreshView() {
        // Refresh the view after a change in the albums list
        albumDisplayNames.clear();
        for (Album a : albums) {
            albumDisplayNames.add(a.getName());
        }
        adapter.notifyDataSetChanged();
    }
}