package com.example.library.helper;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.library.entity.Book;

import java.util.ArrayList;

public class ListWaiter extends Thread {
    private final ArrayList<Book> output;
    private final FragmentActivity activity;
    private ArrayList<Integer> ids;
    private ArrayList<Drawable> covers;
    private ArrayList<String> coversIDs;
    private ArrayList<String> descriptions;
    private ArrayList<String> titles;
    private ArrayList<String> authors;
    private LinearLayout LoadingL;
    private RecyclerView recycler;
    private static final String TAG = "ListWaiter";

    public ListWaiter(FragmentActivity activity, ArrayList<Book> output, ArrayList<Integer> ids,
                      ArrayList<Drawable> covers,
                      ArrayList<String> descriptions, ArrayList<String> titles,
                      ArrayList<String> authors, ArrayList<String> coversIDs, LinearLayout LoadingL,
                      RecyclerView recycler){

        this.output = output;
        this.activity = activity;
        this.ids = ids;
        this.covers = covers;
        this.descriptions = descriptions;
        this.titles = titles;
        this.authors = authors;
        this.coversIDs = coversIDs;
        this.LoadingL = LoadingL;
        this.recycler = recycler;

    }
    public void run() {
        while (output.size() > covers.size()) {
            try {
//                Log.d(TAG, "output.size = " + output.size() + " covers.size = " + covers.size());
                Thread.sleep(1);
            } catch (InterruptedException ignored) {
            }
        }
        new RecyclerInitializer(activity, ids, covers,
                descriptions, titles, authors,
                coversIDs, LoadingL).execute(recycler);
    }
}