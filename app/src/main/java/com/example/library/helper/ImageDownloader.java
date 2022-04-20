package com.example.library.helper;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.InputStream;

public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
    @SuppressLint("StaticFieldLeak")
    ImageView bmImage;
    public AsyncResponse delegate = null;

    @SuppressWarnings("deprecation")
    public ImageDownloader(@Nullable ImageView bmImage) {
        this.bmImage = bmImage;
    }
    public ImageDownloader(){
        //
    }

    @Nullable
    protected Bitmap doInBackground(@NonNull String... urls) {
        String urlDisplay = urls[0];
        Bitmap image = null;
        try {
            InputStream in = new java.net.URL(urlDisplay).openStream();
            image = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Image Downloading Error", e.getMessage());
            e.printStackTrace();
        }
        return image;
    }

    protected void onPostExecute(@Nullable Bitmap result) {
        if(bmImage!=null){
            bmImage.setImageBitmap(result);
        }
        else {
            delegate.processFinish(result);
        }
    }
}