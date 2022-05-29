package com.example.library.fragment.other;


import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.library.R;
import com.example.library.entity.Book;
import com.example.library.helper.AsyncResponse;
import com.example.library.helper.ImageDownloader;
import com.example.library.helper.RecyclerInitializer;

import org.json.JSONObject;

import java.util.ArrayList;

public class BooksExtendedList extends Fragment implements AsyncResponse {

    private String headerText, link;
    private TextView headerTextView;
    private RecyclerView booksList;
    private LinearLayout LoadingL;

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
        Log.d(TAG, "setBooksRecycler: " + link);
    }

    private void setHeaderText(){
        headerTextView.setText(headerText);
    }

    @Override
    public void processFinish(Boolean output) {

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
            String coverID = String.valueOf(book.getCover());
            d.execute("https://liaten.ru/libpics_small/" + coverID + ".jpg");
            String author = book.getAuthor();
            String title = book.getTitle();
            int id = book.getID();
            ids.add(id);
            descriptions.add(book.getDescription());
            titles.add(title);
            authors.add(author);
            coversIDs.add(coverID);
        }
        new RecyclerInitializer(requireActivity(), ids, covers, descriptions, titles, authors, coversIDs, LoadingL).execute(booksList);
    }

    @Override
    public void returnBooks(ArrayList<Book> output, String table) {

    }

    @Override
    public void returnTable(String table) {

    }

    @Override
    public void processFinish(Bitmap output) {

    }

    @Override
    public void processFinish(Bitmap output, String table) {

    }

    @Override
    public void returnJSONObject(JSONObject jsonObject) {

    }
}
