package com.example.library.helper.response;

import android.graphics.Bitmap;

import com.example.library.entity.Book;

import java.util.ArrayList;

public interface BookResponse {
    void returnBooks(ArrayList<Book> output);
    void returnBooks(ArrayList<Book> output, String table);

    void returnBookCover(Bitmap cover);
    void returnBookCover(Bitmap output, String table);

    void returnTable(String active_table);
}

