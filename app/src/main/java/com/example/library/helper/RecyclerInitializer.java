package com.example.library.helper;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.library.R;
import com.example.library.adapter.RecyclerViewAdapter;
import com.example.library.entity.Book;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class RecyclerInitializer extends AsyncTask<RecyclerView, Void, Void> implements AsyncResponse {

    private static FragmentActivity activity;

    private static ArrayList<Integer> ids;
    private static ArrayList<Drawable> covers;
    private static ArrayList<String> descriptions;
    private static ArrayList<String> titles;
    private static ArrayList<String> authors;
    private static ArrayList<String> coversIDs;

    private static LinearLayout LoadingL;
    private static int page;
    private AsyncResponse asyncResponse = this;
    private static Fragment fragment;
    private boolean settings_set = false;

    private static String orientation;

    private boolean loading = true;

    private int pastVisibleItems, visibleItemCount, totalItemCount;

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private RecyclerViewAdapter adapter;
    private DividerItemDecoration itemDecoration;

    private static final String TAG = "RecyclerInitializer";

    public RecyclerInitializer(FragmentActivity activity, ArrayList<Integer> ids,
                               ArrayList<Drawable> covers,
                               ArrayList<String> descriptions, ArrayList<String> titles,
                               ArrayList<String> authors, ArrayList<String> coversIDs,
                               LinearLayout LoadingL, String orientation, Fragment fragment) {
        RecyclerInitializer.activity = activity;
        RecyclerInitializer.ids = ids;
        RecyclerInitializer.covers = covers;
        RecyclerInitializer.descriptions = descriptions;
        RecyclerInitializer.titles = titles;
        RecyclerInitializer.authors = authors;
        RecyclerInitializer.coversIDs = coversIDs;
        RecyclerInitializer.LoadingL = LoadingL;
        RecyclerInitializer.orientation = orientation;
        RecyclerInitializer.fragment = fragment;
        RecyclerInitializer.page = 1;
    }
    public RecyclerInitializer(FragmentActivity activity, ArrayList<Integer> ids,
                               ArrayList<Drawable> covers,
                               ArrayList<String> descriptions, ArrayList<String> titles,
                               ArrayList<String> authors, ArrayList<String> coversIDs,
                               LinearLayout LoadingL, String orientation, Fragment fragment, int page) {
        RecyclerInitializer.activity = activity;
        RecyclerInitializer.ids = ids;
        RecyclerInitializer.covers = covers;
        RecyclerInitializer.descriptions = descriptions;
        RecyclerInitializer.titles = titles;
        RecyclerInitializer.authors = authors;
        RecyclerInitializer.coversIDs = coversIDs;
        RecyclerInitializer.LoadingL = LoadingL;
        RecyclerInitializer.orientation = orientation;
        RecyclerInitializer.fragment = fragment;
        RecyclerInitializer.page = page;
    }

    @Override
    protected Void doInBackground(RecyclerView... recyclerViews) {
        recyclerView = recyclerViews[0];
        switch (orientation){
            case "vertical":
                layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false);
                break;
            case "horizontal":
                layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL,false);
                break;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerViewAdapter(activity, ids, covers,descriptions, titles, authors, coversIDs, orientation);
        recyclerView.setAdapter(adapter);
        switch (orientation){
            case "vertical":
                itemDecoration = new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL);
                itemDecoration.setDrawable(ContextCompat.getDrawable(activity, R.drawable.divider_books_vertical));
                recyclerView.addOnScrollListener(scrollListener);
                break;
            case "horizontal":
                itemDecoration = new DividerItemDecoration(activity, DividerItemDecoration.HORIZONTAL);
                itemDecoration.setDrawable(ContextCompat.getDrawable(activity, R.drawable.empty_divider_horizontal));
                break;
        }
        recyclerView.addItemDecoration(itemDecoration);
        LoadingL.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (dy > 0 ) { //check for scroll down
                visibleItemCount = layoutManager.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
                if (loading && (visibleItemCount + pastVisibleItems) >= totalItemCount) {
                    loading = false;
                    page++;
                    String link = "https://liaten.ru/db/new_books.php?limited=n&page="+page+"&recsPerPage=5";
                    Log.d(TAG, "onScrolled: " + page);
                    try {
                        new BookHelper(asyncResponse).execute(new URL(link));
                    } catch (MalformedURLException ignored) {
                    }
                }
            }
        }
    };



    @Override
    public void processFinish(Boolean output) {

    }

    @Override
    public void returnBooks(ArrayList<Book> output) {
        Log.d(TAG, "output_size: " + output.size());
        if(output.size()==0){
            LoadingL.setVisibility(View.GONE);
        }
        else {
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
            LoadingL.setVisibility(View.VISIBLE);

            class ListUpdater extends Thread{
                public void run() {
                    while (ids.size() > covers.size()) {
                        try {
//                            Log.d(TAG, "ids.size = " + ids.size() + " covers.size = " + covers.size());
                            Thread.sleep(1);
                        } catch (InterruptedException ignored) {
                        }
                    }
                    try {
                        adapter.notifyItemInserted(covers.size());
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    finally {
                        LoadingL.setVisibility(View.GONE);
                        loading = true;
                    }

                }
            }

            new ListUpdater().start();

        }
    }

    @Override
    public void returnBooks(ArrayList<Book> output, String table) {

    }

    @Override
    public void returnTable(String table) {

    }

    @Override
    public void processFinish(Bitmap output) {
        covers.add(new BitmapDrawable(output));

    }

    @Override
    public void processFinish(Bitmap output, String table) {

    }

    @Override
    public void returnJSONObject(JSONObject jsonObject) {

    }
}
