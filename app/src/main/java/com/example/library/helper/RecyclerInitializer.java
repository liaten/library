package com.example.library.helper;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Spanned;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.library.R;
import com.example.library.adapter.RecyclerViewAdapter;

import java.util.ArrayList;

public class RecyclerInitializer extends AsyncTask<RecyclerView, Void, Void> {

    private static FragmentActivity activity;
    private static ArrayList<Spanned> titles_authors;
    private static ArrayList<Drawable> covers;
    private static ArrayList<String> descriptions;
    private static ArrayList<String> titles;
    private static ArrayList<String> authors;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private RecyclerViewAdapter adapter;
    private DividerItemDecoration itemDecoration;

    public RecyclerInitializer(FragmentActivity activity,
                               ArrayList<Spanned> titles_authors, ArrayList<Drawable> covers,
                               ArrayList<String> descriptions, ArrayList<String> titles, ArrayList<String> authors) {
        RecyclerInitializer.activity = activity;
        RecyclerInitializer.titles_authors = titles_authors;
        RecyclerInitializer.covers = covers;
        RecyclerInitializer.descriptions = descriptions;
        RecyclerInitializer.titles = titles;
        RecyclerInitializer.authors = authors;
    }


    @Override
    protected Void doInBackground(RecyclerView... recyclerViews) {
        recyclerView = recyclerViews[0];
        layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL,false);
        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerViewAdapter(activity, titles_authors,covers,descriptions, titles, authors);
        recyclerView.setAdapter(adapter);
        itemDecoration = new DividerItemDecoration(activity, DividerItemDecoration.HORIZONTAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(activity, R.drawable.empty_divider_horizontal));
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setVisibility(View.VISIBLE);
    }
}
