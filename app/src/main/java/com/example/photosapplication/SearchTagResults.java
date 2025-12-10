package com.example.photosapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.photosapplication.model.Album;
import com.example.photosapplication.model.Photo;
import com.example.photosapplication.model.Tag;
import com.example.photosapplication.util.AppState;
import com.example.photosapplication.util.PhotoAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SearchTagResults extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PhotoAdapter adapter;
    private TextView noResultsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_results);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.searchResultsRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        noResultsTextView = findViewById(R.id.noResultsTextView);

        String tagType = getIntent().getStringExtra("tagType");
        String tagValue = getIntent().getStringExtra("tagValue");

        AppState currentState = ((PhotosApplication) getApplication()).getAppState();
        List<Photo> searchResults = searchPhotos(currentState, tagType, tagValue);

        if (searchResults.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            noResultsTextView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            noResultsTextView.setVisibility(View.GONE);
            adapter = new PhotoAdapter(this, new Album("Search Results", searchResults));
            recyclerView.setAdapter(adapter);
        }
    }

    private List<Photo> searchPhotos(AppState appState, String tagType, String tagValue) {
        if (tagType == null || tagValue == null) {
            return new ArrayList<>();
        }

        Predicate<Tag> filter = tag -> tag.getType().getName().equalsIgnoreCase(tagType) && tag.getValue().equalsIgnoreCase(tagValue);

        return appState.getAlbums().stream()
                .flatMap(album -> album.getPhotos().stream())
                .filter(photo -> photo.hasTag(filter))
                .collect(Collectors.toList());
    }
}
