package com.example.library.helper;

import android.graphics.Bitmap;

import java.util.ArrayList;

public interface AsyncResponse {
    void processFinish(Boolean output);
    void returnBooks(ArrayList<Book> output);
    void processFinish(Bitmap output);
}
