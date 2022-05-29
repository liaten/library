package com.example.library.helper;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

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
    private static ArrayList<Integer> ids;
    private static ArrayList<Drawable> covers;
    private static ArrayList<String> descriptions;
    private static ArrayList<String> titles;
    private static ArrayList<String> authors;
    private static ArrayList<String> coversIDs;
    private static LinearLayout LoadingL;

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private RecyclerViewAdapter adapter;
    private DividerItemDecoration itemDecoration;

    private static final String TAG = "RecyclerInitializer";

    public RecyclerInitializer(FragmentActivity activity, ArrayList<Integer> ids,
                               ArrayList<Drawable> covers,
                               ArrayList<String> descriptions, ArrayList<String> titles,
                               ArrayList<String> authors, ArrayList<String> coversIDs, LinearLayout LoadingL) {
        RecyclerInitializer.activity = activity;
        RecyclerInitializer.ids = ids;
        RecyclerInitializer.covers = covers;
        RecyclerInitializer.descriptions = descriptions;
        RecyclerInitializer.titles = titles;
        RecyclerInitializer.authors = authors;
        RecyclerInitializer.coversIDs = coversIDs;
        RecyclerInitializer.LoadingL = LoadingL;
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
        adapter = new RecyclerViewAdapter(activity, ids, covers,descriptions, titles, authors, coversIDs);
        recyclerView.setAdapter(adapter);
        itemDecoration = new DividerItemDecoration(activity, DividerItemDecoration.HORIZONTAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(activity, R.drawable.empty_divider_horizontal));
        recyclerView.addItemDecoration(itemDecoration);
        LoadingL.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }
}
