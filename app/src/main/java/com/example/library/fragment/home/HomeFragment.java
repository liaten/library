package com.example.library.fragment.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.library.MainActivity;
import com.example.library.R;
import com.example.library.entity.Book;
import com.example.library.fragment.other.BooksExtendedList;
import com.example.library.helper.AsyncResponse;
import com.example.library.helper.BookHelper;
import com.example.library.helper.FragmentHelper;
import com.example.library.helper.ImageDownloader;
import com.example.library.helper.ListWaiter;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class HomeFragment extends Fragment implements AsyncResponse {

    private static final ArrayList<Drawable> covers = new ArrayList<>();
    private static final ArrayList<Integer> ids = new ArrayList<>();
    private static final ArrayList<String> coversIDs = new ArrayList<>();
    private static final ArrayList<String> descriptions = new ArrayList<>();
    private static final ArrayList<String> titles = new ArrayList<>();
    private static final ArrayList<String> authors = new ArrayList<>();
    private RecyclerView newBooksList;
    private LinearLayout LoadingL;
    private TextView newBooksTextView, allNewsTextView;
    private static String link = "";

//    private static final String TAG = "HomeFragment";

    View.OnClickListener allNewsListener = view -> {
        new FragmentHelper((MainActivity) requireActivity(),
                false,true).execute(
                new BooksExtendedList(
                        newBooksTextView.getText().toString(),
                        link + "n&page=1&recsPerPage=12")
        );
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViews();
        setOnClickListeners();
        getTopNewBooks(this,"y");
    }

    private void setViews() {
        View v = requireView();
        allNewsTextView = v.findViewById(R.id.all_news);
        newBooksList = v.findViewById(R.id.new_books_list);
        LoadingL = v.findViewById(R.id.loading);
        newBooksTextView = v.findViewById(R.id.new_books_text_view);
        LoadingL.setVisibility(View.VISIBLE);
    }
    private void setOnClickListeners(){
        allNewsTextView.setOnClickListener(allNewsListener);
    }

    public static void getTopNewBooks(Fragment fragment, String limited){
        try {
            link = "https://liaten.ru/db/new_books.php?limited=";
            switch (limited){
                case "y":
                    new BookHelper((AsyncResponse) fragment).execute(new URL(link + "y"));
                    break;
                case "n":
                    new BookHelper((AsyncResponse) fragment).execute(new URL(link + "n"));
                    break;
            }
        } catch (MalformedURLException ignored) {
        }
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
            String coverID = String.valueOf(book.getCover());
            ImageDownloader d = new ImageDownloader(this);
            d.execute("https://liaten.ru/libpics_small/" + coverID + ".jpg");
            String author = book.getAuthor();
            String title = book.getTitle();
            ids.add(book.getID());
            descriptions.add(book.getDescription());
            titles.add(title);
            authors.add(author);
            coversIDs.add(coverID);
        }
        new ListWaiter(requireActivity(),output, ids, covers,
                descriptions,titles,authors,coversIDs,
                LoadingL,newBooksList, "horizontal", this).start();
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