package com.example.library.fragment.other;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.library.R;
import com.example.library.entity.Book;
import com.example.library.entity.ScrollDirection;
import com.example.library.helper.BookHelper;
import com.example.library.helper.ImageDownloader;
import com.example.library.helper.ListWaiter;
import com.example.library.response.ImageResponse;
import com.example.library.response.BookResponse;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class BooksExtendedList extends Fragment implements BookResponse, ImageResponse {

    private String headerText, link;
    private TextView headerTextView;
    private RecyclerView booksList;
    private ProgressBar LoadingL;


    private static final ArrayList<Drawable> covers = new ArrayList<>();
    private static final ArrayList<Integer> ids = new ArrayList<>();
    private static final ArrayList<String> coversIDs = new ArrayList<>();
    private static final ArrayList<String> descriptions = new ArrayList<>();
    private static final ArrayList<String> titles = new ArrayList<>();
    private static final ArrayList<String> authors = new ArrayList<>();

    private static final String TAG = "BooksExtendedList";

    public BooksExtendedList(String headerText, String link){
        this.headerText = headerText;
        this.link = link;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_books_extended, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViews();
        setOnClickListeners();
        setHeaderText();
        setBooksRecycler();
    }

    private void setOnClickListeners() {
    }

    private void setViews() {
        View v = requireView();
        headerTextView = v.findViewById(R.id.header);
        booksList = v.findViewById(R.id.books_list);
        LoadingL = v.findViewById(R.id.loading);
    }

    private void setBooksRecycler(){
        try {
            new BookHelper(this).execute(new URL(link));
        } catch (MalformedURLException ignored) {
        }
        Log.d(TAG, "setBooksRecycler: " + link);
    }

    private void setHeaderText(){
        headerTextView.setText(headerText);
    }

    @Override
    public void returnBooks(ArrayList<Book> output) {
        ids.clear();
        covers.clear();
        descriptions.clear();
        titles.clear();
        authors.clear();
        coversIDs.clear();
        for (Book book : output
        ) {
            ImageDownloader d = new ImageDownloader(this);
            String coverID = String.valueOf(book.getCoverID());
            d.execute("https://liaten.ru/libpics_small/" + coverID + ".jpg");
            String author = book.getAuthor();
            String title = book.getTitle();
            int id = book.getID();
            ids.add(id);
            titles.add(title);
            authors.add(author);
            coversIDs.add(coverID);
        }
        new ListWaiter(requireActivity(),output, ids, covers,
                titles,authors,coversIDs,
                LoadingL,booksList, ScrollDirection.vertical, link).start();
    }

    @Override
    public void returnBooks(ArrayList<Book> output, String table) {

    }

    @Override
    public void returnImage(Bitmap cover) {
        covers.add(new BitmapDrawable(cover));
    }

    @Override
    public void returnImage(Bitmap output, String table) {

    }

    @Override
    public void returnTable(String active_table) {

    }
}
