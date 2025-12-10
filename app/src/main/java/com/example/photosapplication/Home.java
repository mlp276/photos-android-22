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
import com.example.photosapplication.util.SearchPhotoInputParameters;
import com.example.photosapplication.util.StateManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Home extends AppCompatActivity {
    private static final String TAG = "Home";
    private static final String[] options = {"Open Album", "Rename Album", "Remove Album"};

    List<Album> albums;
    ListView albumListView;
    List<String> albumDisplayNames;
    Button addAlbumsButton;
    Button searchTagsButton;
    private ArrayAdapter<String> adapter;
    private SearchPhotoInputParameters inputTagTypeParameters;

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
        searchTagsButton.setOnClickListener(v -> showSearchNumberOfTagsDialog());

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

    private void showSearchNumberOfTagsDialog() {
        inputTagTypeParameters = new SearchPhotoInputParameters();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Search by Tag Type: Number of Tags");

        String[] numberOfTags = {"1", "2"};
        builder.setItems(numberOfTags, (dialog, which) -> {
            showSearchTagTypeDialog(Integer.parseInt(numberOfTags[which]));
        });

        builder.show();
    }

    private void showSearchTagTypeDialog(int numberOfTags) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Search by Tag Type");

        String[] tagTypes = {TagType.PERSON.getName(), TagType.LOCATION.getName()};
        builder.setItems(tagTypes, (dialog, which) -> {
            String chosenTagType = tagTypes[which];
            if (inputTagTypeParameters.tagType1 == null) {
                inputTagTypeParameters.tagType1 = chosenTagType;
            } else {
                inputTagTypeParameters.tagType2 = chosenTagType;
            }
            showSearchTagValueDialog(numberOfTags, chosenTagType);
        });

        builder.show();
    }

    private void showSearchTagValueDialog(int numberOfTags, String tagType) {
        EditText input = new EditText(this);
        input.setHint("Partial tag value");

        new AlertDialog.Builder(this)
                .setTitle("Enter partial value for " + tagType)
                .setView(input)
                .setPositiveButton("Find Tags", (dialog, which) -> {
                    String partialTagValue = input.getText().toString().trim();
                    showMatchingTagsDialog(numberOfTags, tagType, partialTagValue);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showMatchingTagsDialog(int numberOfTags, String tagType, String partialTagValue) {
        AppState currentState = ((PhotosApplication) getApplication()).getAppState();
        List<String> matchingTags = currentState.getUniqueTagValues(tagType).stream()
                .filter(tag -> tag.toLowerCase().startsWith(partialTagValue.toLowerCase()))
                .collect(Collectors.toList());

        if (matchingTags.isEmpty()) {
            new AlertDialog.Builder(this)
                    .setTitle("No Matching Tags")
                    .setMessage("There are no tags that start with the provided value.")
                    .setPositiveButton("OK", null)
                    .show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a Tag");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, matchingTags);
        builder.setAdapter(adapter, (dialog, which) -> {
            String selectedTag = matchingTags.get(which);
            if (inputTagTypeParameters.tagValue1 == null) {
                inputTagTypeParameters.tagValue1 = selectedTag;
            } else {
                inputTagTypeParameters.tagValue2 = selectedTag;
            }

            if (numberOfTags > 1) {
                showSearchTagTypeDialog(numberOfTags - 1);
            } else {
                showSearchTagSearchModeDialog();
            }
        });

        builder.show();
    }

    private void showSearchTagSearchModeDialog() {
        boolean condition = inputTagTypeParameters.tag2IsNull();
        requestUserConfirmationIfNeeded(condition, result -> {
            Intent intent = new Intent(this, SearchTagResults.class);
            if (result) {
                intent.putExtra("searchMode", inputTagTypeParameters.searchMode);
                intent.putExtra("tagType1", inputTagTypeParameters.tagType1);
                intent.putExtra("tagValue1", inputTagTypeParameters.tagValue1);
                intent.putExtra("tagType2", inputTagTypeParameters.tagType2);
                intent.putExtra("tagValue2", inputTagTypeParameters.tagValue2);
            } else {
                intent.putExtra("searchMode", 1);
                intent.putExtra("tagType1", inputTagTypeParameters.tagType1);
                intent.putExtra("tagValue1", inputTagTypeParameters.tagValue1);
                intent.putExtra("tagType2", (Bundle) null);
                intent.putExtra("tagValue2", (Bundle) null);
            }
            startActivity(intent);
        });
    }

    public interface DialogResultCallback {
        void onResult(boolean userAccepted);
    }

    public void requestUserConfirmationIfNeeded(boolean condition, DialogResultCallback callback) {
        if (condition) {
            callback.onResult(false);
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Search by Tag Type: Search Mode");
        String[] searchModes = {"BOTH", "EITHER"};

        builder.setItems(searchModes, (dialog, which) -> {
            String searchModeString = searchModes[which];
            if (searchModeString.equals("BOTH")) {
                inputTagTypeParameters.searchMode = 2;
            } else if (searchModeString.equals("EITHER")) {
                inputTagTypeParameters.searchMode = 3;
            }
            callback.onResult(true);
        });

        builder.show();
    }

    public void refreshView() {
        albumDisplayNames.clear();
        for (Album a : albums) {
            albumDisplayNames.add(a.getName());
        }
        adapter.notifyDataSetChanged();
    }
}
