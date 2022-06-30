package com.example.library.helper;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.library.helper.response.ImageResponse;

import java.io.InputStream;

public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
    @SuppressLint("StaticFieldLeak")
    private ImageView bmImage = null;
    private ImageResponse delegate = null;
    private String table = null;

    @SuppressWarnings("deprecation")
    public ImageDownloader(@Nullable ImageView bmImage) {
        this.bmImage = bmImage;
    }

    public ImageDownloader(ImageResponse delegate) {
        this.delegate = delegate;
    }

    public ImageDownloader(ImageResponse delegate, String table) {
        this.delegate = delegate;
        this.table = table;
    }

    @Nullable
    protected Bitmap doInBackground(@NonNull String... urls) {
        String urlDisplay = urls[0];
        Bitmap image = null;
        try {
            InputStream in = new java.net.URL(urlDisplay).openStream();
            image = BitmapFactory.decodeStream(in);
        } catch (Exception ignored) {
        }
        return image;
    }

    protected void onPostExecute(@Nullable Bitmap image) {
        if(bmImage!=null){
            bmImage.setImageBitmap(image);
        }
        else {
            if (table == null) {
                delegate.returnImage(image);
            } else {
                delegate.returnImage(image, table);
            }
        }
    }
}