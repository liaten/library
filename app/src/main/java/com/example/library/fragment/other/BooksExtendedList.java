package com.example.library.fragment.other;

import static com.example.library.fragment.home.HomeFragment.getTopNewBooks;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.library.R;
import com.example.library.entity.Book;
import com.example.library.fragment.home.HomeFragment;
import com.example.library.helper.AsyncResponse;

import org.json.JSONObject;

import java.util.ArrayList;

public class BooksExtendedList extends Fragment implements AsyncResponse {

    private String headerText, type;
    private TextView headerTextView;
    private RecyclerView booksList;

    private static final String TAG = "BooksExtendedList";

    public BooksExtendedList(String headerText, String type){
        this.headerText = headerText;
        this.type = type;
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
    }

    private void setBooksRecycler(){
        switch (type){
            case "new_books":
                getTopNewBooks(this,"n");
                break;
            case "reserved":
                break;
            case "wishlist":
                break;
            case "on_hands":
                break;
        }
    }

    private void setHeaderText(){
        headerTextView.setText(headerText);
    }

    @Override
    public void processFinish(Boolean output) {

    }

    @Override
    public void returnBooks(ArrayList<Book> output) {
        Log.d(TAG, "returnBooks: " + output);
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
