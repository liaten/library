package com.example.library.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.library.R;

public class BookInfo extends Fragment {

    private String title, author, description;
    private ImageView CoverView;
    private TextView TitleView, AuthorView, DescriptionView;

    public BookInfo(String title, String author, String description) {
        this.title = title;
        this.author = author;
        this.description = description;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_book_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViews();
        setDetails();
    }

    private void setDetails() {
        TitleView.setText(title);
        AuthorView.setText(author);
        DescriptionView.setText(description);
    }

    private void setViews() {
        View view = requireView();
        CoverView = view.findViewById(R.id.cover);
        TitleView = view.findViewById(R.id.title);
        AuthorView = view.findViewById(R.id.author);
        DescriptionView = view.findViewById(R.id.description);
    }
}
