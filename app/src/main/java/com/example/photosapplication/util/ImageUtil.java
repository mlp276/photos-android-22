package com.example.photosapplication.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.example.photosapplication.model.Photo;

import java.io.IOException;
import java.io.InputStream;

public class ImageUtil {
    static Bitmap loadBitmapFromPhoto(Context context, Photo photo, int reqWidth, int reqHeight) throws IOException {
        Uri uri = photo.getUri();

        InputStream input = context.getContentResolver().openInputStream(uri);
        if (input == null) {
            return null;
        }

        // First decode bounds only
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(input, null, opts);
        input.close();

        // Compute sample size
        opts.inSampleSize = calculateInSampleSize(opts, reqWidth, reqHeight);
        opts.inJustDecodeBounds = false;

        input = context.getContentResolver().openInputStream(uri); // reopen
        if (input == null) {
            return null;
        }

        Bitmap bitmap = BitmapFactory.decodeStream(input, null, opts);
        input.close();

        return bitmap;
    }

    private static int calculateInSampleSize(BitmapFactory.Options opts, int reqW, int reqH) {
        int height = opts.outHeight;
        int width  = opts.outWidth;
        int inSampleSize = 1;

        if (height > reqH || width > reqW) {
            int halfH = height / 2;
            int halfW = width / 2;

            while ((halfH / inSampleSize) >= reqH &&
                    (halfW / inSampleSize) >= reqW) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

}
