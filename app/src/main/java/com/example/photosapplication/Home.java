package com.example.photosapplication;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.photosapplication.model.Album;
import com.example.photosapplication.model.util.UniqueList;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    ListView albumListView;
    UniqueList<Album> albums;
    List<String> albumDisplayNames;
    ArrayAdapter<String> adapter;

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

        albumListView = findViewById(R.id.albumListView);

        albums = new UniqueList<Album>();
        albums.add(new Album("Hello World!"));

        albumDisplayNames = new ArrayList<String>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, albumDisplayNames);
        albumListView.setAdapter(adapter);

        albumListView.setOnItemClickListener((parent, view, position, id) -> {
            showAlbumsPopupMenu(view, position);
        });

        refreshDisplayList();
    }

    private void showAlbumsPopupMenu(View anchor, int position) {
        Album album = albums.get(position);

        PopupMenu popup = new PopupMenu(this, anchor);
        popup.getMenuInflater().inflate(R.menu.album_options_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();

            if (id == R.id.rename) {
                renameAlbum(album);
                return true;
            } else if (id == R.id.delete) {
                deleteAlbum(album);
                return true;
            } else if (id == R.id.open) {
                // TODO: openAlbum(album);
                return true;
            }

            return false;
        });

        popup.show();
    }

    private void renameAlbum(Album album) {
        EditText input = new EditText(this);
        input.setText(album.getName());

        new AlertDialog.Builder(this)
                .setTitle("Rename Album")
                .setView(input)
                .setPositiveButton("OK", (d, w) -> {
                    album.setName(input.getText().toString());
                    refreshDisplayList();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteAlbum(Album album) {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete this album?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    albums.remove(album);
                    refreshDisplayList();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void openAlbum(Album album) {

    }

    private void refreshDisplayList() {
        albumDisplayNames.clear();
        for (Album a : albums) {
            albumDisplayNames.add(a.getName());
        }
        adapter.notifyDataSetChanged();
    }
}