package com.example.library.response;

import com.example.library.entity.Book;

import java.util.ArrayList;

public interface BookResponse {
    void returnBooks(ArrayList<Book> output);
    void returnBooks(ArrayList<Book> output, String table);

    void returnTable(String active_table);
}

