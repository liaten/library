package com.example.library.response;

import android.graphics.Bitmap;

public interface ImageResponse {
    void returnImage(Bitmap cover);
    void returnImage(Bitmap output, String table);
}
