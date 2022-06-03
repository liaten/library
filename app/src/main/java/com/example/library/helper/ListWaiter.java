package com.example.library.helper;

import android.graphics.drawable.Drawable;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
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
    private ProgressBar LoadingL;
    private RecyclerView recycler;
    private String orientation;
    private Fragment fragment;
    private int page = 0;
    private static final String TAG = "ListWaiter";

    public ListWaiter(FragmentActivity activity, ArrayList<Book> output, ArrayList<Integer> ids,
                      ArrayList<Drawable> covers,
                      ArrayList<String> descriptions, ArrayList<String> titles,
                      ArrayList<String> authors, ArrayList<String> coversIDs, ProgressBar LoadingL,
                      RecyclerView recycler, String orientation, Fragment fragment){

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
        this.orientation = orientation;
        this.fragment = fragment;
    }

    public ListWaiter(FragmentActivity activity, ArrayList<Book> output, ArrayList<Integer> ids,
                      ArrayList<Drawable> covers,
                      ArrayList<String> descriptions, ArrayList<String> titles,
                      ArrayList<String> authors, ArrayList<String> coversIDs, ProgressBar LoadingL,
                      RecyclerView recycler, String orientation, Fragment fragment, int page){

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
        this.orientation = orientation;
        this.fragment = fragment;
        this.page = page;
    }

    public void run() {
        while (output.size() > covers.size()) {
            try {
//                Log.d(TAG, "output.size = " + output.size() + " covers.size = " + covers.size());
                Thread.sleep(1);
            } catch (InterruptedException ignored) {
            }
        }
        if (page == 0) {
            new RecyclerInitializer(activity, ids, covers,
                    descriptions, titles, authors,
                    coversIDs, LoadingL, orientation, fragment).execute(recycler);
        } else {
            new RecyclerInitializer(activity, ids, covers,
                    descriptions, titles, authors,
                    coversIDs, LoadingL, orientation, fragment, page).execute(recycler);
        }
    }
}