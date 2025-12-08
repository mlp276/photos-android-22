package com.example.photosapplication.util;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.photosapplication.AlbumDetails;
import com.example.photosapplication.PhotoDetails;
import com.example.photosapplication.R;
import com.example.photosapplication.model.Album;
import com.example.photosapplication.model.Photo;

import java.io.IOException;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {
    private static final String TAG = "PhotoAdapter";
    private static final String[] options = {"Display Photo", "Remove Photo"};

    private final Context context;
    private final Album album;

    public PhotoAdapter(Context context, Album album) {
        this.context = context;
        this.album = album;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.photo_view, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Photo photo = album.getPhoto(position);

        Bitmap bmp = null;
        try {
            bmp = ImageUtil.loadBitmapFromPhoto(context, photo, 300, 300);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        holder.photoImageView.setImageBitmap(bmp);

        holder.photoImageView.setOnClickListener(v -> {
            showPhotoOptionsDialog(photo, position);
        });
    }

    @Override
    public int getItemCount() {
        return album.getPhotoCount();
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView photoImageView;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            photoImageView = itemView.findViewById(R.id.photoImageView);
        }
    }

    private void showPhotoOptionsDialog(Photo photo, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Photo Options");

        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0:
                    displayPhoto(photo, position);
                    break;
                case 1:
                    removePhoto(photo, position);
                    break;
            }
        });

        builder.show();
    }

    private void displayPhoto(Photo photo, int position) {
        Log.d(TAG, "Opened photo: " + photo.getUri());
        Intent intent = new Intent(context, PhotoDetails.class);
        intent.putExtra("albumName", album.getName());
        intent.putExtra("photoPosition", position);
        context.startActivity(intent);
    }

    private void removePhoto(Photo photo, int position) {
        album.removePhoto(photo);
        notifyItemRemoved(position);
        Log.d(TAG, "Successfully removed photo, album now has " + album.getPhotoCount() + " photos.");
    }
}