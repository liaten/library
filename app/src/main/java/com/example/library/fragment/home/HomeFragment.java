package com.example.library.fragment.home;

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
import androidx.recyclerview.widget.RecyclerView;

import com.example.library.MainActivity;
import com.example.library.R;
import com.example.library.entity.Book;
import com.example.library.helper.AsyncResponse;
import com.example.library.helper.BookHelper;
import com.example.library.helper.FragmentHelper;
import com.example.library.helper.ImageDownloader;
import com.example.library.helper.RecyclerInitializer;

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
//    private static final String TAG = "HomeFragment";

    private TextView allNewsTextView = null;
    View.OnClickListener allNewsListener = view -> {
        new FragmentHelper((MainActivity) requireActivity(),
                false,true).execute(new NewBooksFragment());
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
        getTopNewBooks();
    }

    private void setViews() {
        View view = requireView();
        allNewsTextView = view.findViewById(R.id.all_news);
        newBooksList = view.findViewById(R.id.new_books_list);
        LoadingL = view.findViewById(R.id.loading);
        LoadingL.setVisibility(View.VISIBLE);
    }
    private void setOnClickListeners(){
        allNewsTextView.setOnClickListener(allNewsListener);
    }

    private void getTopNewBooks(){
        try {
            new BookHelper(this).execute(new URL("https://liaten.ru/db/new_books.php"));
        } catch (MalformedURLException ignored) {
        }
    }

    @Override
    public void processFinish(Boolean output) {
        //
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
        new RecyclerInitializer(requireActivity(), ids, covers, descriptions, titles, authors, coversIDs, LoadingL).execute(newBooksList);
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