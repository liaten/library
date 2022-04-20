package com.example.library.fragment.home;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.library.MainActivity;
import com.example.library.R;
import com.example.library.fragment.profile.ProfileFragment;
import com.example.library.helper.AsyncResponse;
import com.example.library.helper.Book;
import com.example.library.helper.BookHelper;
import com.example.library.helper.DatabaseHelper;
import com.example.library.helper.FragmentHelper;
import com.example.library.helper.ImageDownloader;
import com.example.library.helper.RecyclerInitializer;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

public class HomeFragment extends Fragment implements AsyncResponse {

    private static final ArrayList<Spanned> titles = new ArrayList<>();
    private static final ArrayList<Drawable> covers = new ArrayList<>();
    private RecyclerView newBooksList;
    private static final String TAG = "HomeFragment";
    private ArrayList<Book> books;
    DatabaseHelper db;

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
        getBooks();
    }

    private void getBooks(){
        covers.clear();
        titles.clear();
        getTopNewBooks();

    }

    private void setViews() {
        allNewsTextView = requireView().findViewById(R.id.all_news);
        newBooksList = requireView().findViewById(R.id.new_books_list);
        db = new DatabaseHelper(this.requireActivity());
    }
    private void setOnClickListeners(){
        allNewsTextView.setOnClickListener(allNewsListener);
    }

    private void getTopNewBooks(){
        BookHelper bookHelper = new BookHelper();
        bookHelper.delegate = this;
        String topBooksURL = "https://liaten.ru/new_7_books.php";
        try {
            URL url = new URL(topBooksURL);
            bookHelper.execute(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processFinish(Boolean output) {
        //
    }

    @Override
    public void returnBooks(ArrayList<Book> output) {
        for (Book book : output
        ) {
            ImageDownloader d = new ImageDownloader();
            d.delegate = this;
            String cover = String.valueOf(book.getCover());
            if(cover.length()<2){
                cover = "0" + cover;
            }
            d.execute("https://liaten.ru/libpics_small/b" + cover + ".jpg");
            String author = book.getAuthor();
            String title = book.getTitle();
            Spanned sp = Html.fromHtml(author + "<br><b>" + title + "</b>");
            titles.add(sp);
        }
        new RecyclerInitializer(requireActivity(), titles, covers).execute(newBooksList);
    }

    @Override
    public void processFinish(Bitmap output) {
        covers.add(new BitmapDrawable(output));
    }
}