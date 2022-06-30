package com.example.library.helper.response;

import android.graphics.Bitmap;

public interface ImageResponse {
    void returnImage(Bitmap cover);
    void returnImage(Bitmap output, String table);
}
