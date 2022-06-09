package com.example.library.helper;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.library.R;
import com.example.library.adapter.RecyclerViewAdapter;
import com.example.library.entity.Book;
import com.example.library.entity.Event;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class RecyclerInitializer extends AsyncTask<RecyclerView, Void, Void> implements AsyncResponse {

    private static ArrayList<Integer> ids;
    private static ArrayList<Drawable> covers;
    private static ArrayList<String> descriptions;
    private static ArrayList<String> titles;
    private static ArrayList<String> authors;
    private static ArrayList<String> coversIDs;

    private static FragmentActivity activity;
    private static ProgressBar LoadingL;
    private static int page;
    private static String orientation;
    private static String link;
    private static final String TAG = "RecyclerInitializer";

    private AsyncResponse asyncResponse = this;
    private boolean loading = true;
    private int pastVisibleItems, visibleItemCount, totalItemCount;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private RecyclerViewAdapter adapter;
    private DividerItemDecoration itemDecoration;

    public RecyclerInitializer(FragmentActivity activity, ArrayList<Integer> ids,
                               ArrayList<Drawable> covers,
                               ArrayList<String> descriptions, ArrayList<String> titles,
                               ArrayList<String> authors, ArrayList<String> coversIDs,
                               ProgressBar LoadingL, String orientation, String link) {
        RecyclerInitializer.activity = activity;
        RecyclerInitializer.ids = ids;
        RecyclerInitializer.covers = covers;
        RecyclerInitializer.descriptions = descriptions;
        RecyclerInitializer.titles = titles;
        RecyclerInitializer.authors = authors;
        RecyclerInitializer.coversIDs = coversIDs;
        RecyclerInitializer.LoadingL = LoadingL;
        RecyclerInitializer.orientation = orientation;
        RecyclerInitializer.page = 1;
        RecyclerInitializer.link = link;
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
                    link = Pattern.compile("[\\d]+$", Pattern.CASE_INSENSITIVE).matcher(link)
                            .replaceAll("") + page;
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

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (ids.size() > covers.size()) {
                        try {
//                            Log.d(TAG, "ids.size = " + ids.size() + " covers.size = " + covers.size());
                            Thread.sleep(1);
                        } catch (InterruptedException ignored) {
                        }
                    }
                    try {
                        synchronized (this){
                            activity.runOnUiThread(() -> {
                                adapter.notifyItemInserted(covers.size());
                                LoadingL.setVisibility(View.GONE);
                                loading = true;
                            });
                        }
                    }
                    catch (Exception ignored){
                    }
                }
            }).start();
        }
    }

    @Override
    public void returnEvents(ArrayList<Event> output) {

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
