package com.example.library.helper;

import android.graphics.Bitmap;

import com.example.library.entity.Book;

import org.json.JSONObject;

import java.util.ArrayList;

public interface AsyncResponse {
    void processFinish(Boolean output);
    void returnBooks(ArrayList<Book> output);
    void processFinish(Bitmap output);
    void returnJSONObject(JSONObject jsonObject);
}
