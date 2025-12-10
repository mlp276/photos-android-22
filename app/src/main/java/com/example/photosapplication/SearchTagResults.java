package com.example.photosapplication;

import android.os.Bundle;
import android.util.Log;
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
import com.example.photosapplication.util.UniqueList;

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

        int searchMode = getIntent().getIntExtra("searchMode", -1);
        String tagType1 = getIntent().getStringExtra("tagType1");
        String tagValue1 = getIntent().getStringExtra("tagValue1");
        String tagType2 = getIntent().getStringExtra("tagType2");
        String tagValue2 = getIntent().getStringExtra("tagValue2");

        Log.d("SearchTagResults", "searchMode: " + searchMode);
        Log.d("SearchTagResults", "tagType1: " + tagType1);
        Log.d("SearchTagResults", "tagValue1: " + tagValue1);
        if (tagType2 != null) {
            Log.d("SearchTagResults", "tagType2: " + tagType2);
        }
        if (tagValue2 != null) {
            Log.d("SearchTagResults", "tagValue2: " + tagValue2);
        }

        AppState currentState = ((PhotosApplication) getApplication()).getAppState();
        List<Photo> searchResults = searchPhotos(currentState, searchMode, tagType1, tagValue1, tagType2, tagValue2);

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

    private List<Photo> searchPhotos(
            AppState appState,
            int searchMode,
            String tagType1,
            String tagValue1,
            String tagType2,
            String tagValue2
    ) {
        Predicate<Photo> photoFilter = buildTagSearchPredicate(searchMode, tagType1, tagValue1, tagType2, tagValue2);
        if (photoFilter == null) {
            return new UniqueList<Photo>();
        }

        return appState.getAlbums().stream()
                .flatMap(album -> album.getPhotos().stream())
                .filter(photoFilter)
                .distinct()
                .collect(Collectors.toList());
    }

    private Predicate<Photo> buildTagSearchPredicate(
            int searchMode, // 1=single, 2=both, 3=either
            String tagType1,
            String tagValue1,
            String tagType2,
            String tagValue2
    ) {
        if (searchMode < 0) {
            return null;
        }

        if (searchMode == 1 && (tagType1 == null || tagValue1 == null)) {
            return null;
        }
        Predicate<Tag> tagFilter1 = (tag -> tag.getType().getName().equalsIgnoreCase(tagType1) &&
                tag.getValue().equalsIgnoreCase(tagValue1)
        );

        if (searchMode != 1 && (tagType2 == null || tagValue2 == null)) {
            return null;
        }
        Predicate<Tag> tagFilter2 = (tag -> tag.getType().getName().equalsIgnoreCase(tagType2) &&
                tag.getValue().equalsIgnoreCase(tagValue2)
        );

        Predicate<Photo> photoFilter1 = (photo -> photo.hasTag(tagFilter1));
        Predicate<Photo> photoFilter2 = (photo -> photo.hasTag(tagFilter2));

        switch (searchMode) {
            case 1: // ONE TAG
                return photoFilter1;
            case 2: // BOTH TAGS
                return photoFilter1.and(photoFilter2);
            case 3: // EITHER TAG
                return photoFilter1.or(photoFilter2);
            default:
                throw new IllegalArgumentException("Invalid search mode");
        }
    }
}
