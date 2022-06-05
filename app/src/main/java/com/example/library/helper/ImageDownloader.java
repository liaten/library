package com.example.library.helper;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.InputStream;

public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
    @SuppressLint("StaticFieldLeak")
    private ImageView bmImage = null;
    private AsyncResponse delegate = null;
    private String table = null;

    @SuppressWarnings("deprecation")
    public ImageDownloader(@Nullable ImageView bmImage) {
        this.bmImage = bmImage;
    }

    public ImageDownloader(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    public ImageDownloader(AsyncResponse delegate, String table) {
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

    protected void onPostExecute(@Nullable Bitmap result) {
        if(bmImage!=null){
            bmImage.setImageBitmap(result);
        }
        else {
            if (table == null) {
                delegate.processFinish(result);
            } else {
                delegate.processFinish(result, table);
            }
        }
    }
}