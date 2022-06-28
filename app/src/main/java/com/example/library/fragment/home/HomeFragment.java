package com.example.library.fragment.home;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.library.MainActivity;
import com.example.library.R;
import com.example.library.entity.Book;
import com.example.library.entity.ScrollDirection;
import com.example.library.fragment.other.BooksExtendedList;
import com.example.library.helper.BookHelper;
import com.example.library.helper.FragmentHelper;
import com.example.library.helper.ImageDownloader;
import com.example.library.helper.ListWaiter;
import com.example.library.helper.response.BookResponse;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class HomeFragment extends Fragment implements BookResponse {

    private static final ArrayList<Drawable> covers = new ArrayList<>();
    private static final ArrayList<Integer> ids = new ArrayList<>();
    private static final ArrayList<String> coversIDs = new ArrayList<>();
    private static final ArrayList<String> descriptions = new ArrayList<>();
    private static final ArrayList<String> titles = new ArrayList<>();
    private static final ArrayList<String> authors = new ArrayList<>();
    private RecyclerView newBooksList;
    private ProgressBar LoadingL;
    private TextView newBooksTextView, allNewsTextView;
    private static String link = "";

//    private static final String TAG = "HomeFragment";

    View.OnClickListener allNewsListener = view -> {
        new FragmentHelper((MainActivity) requireActivity(),
                false,true).execute(
                new BooksExtendedList(
                        newBooksTextView.getText().toString(),
                        link + "n&recsPerPage=12&page=1")
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
                    new BookHelper((BookResponse) fragment).execute(new URL(link + "y"));
                    break;
                case "n":
                    new BookHelper((BookResponse) fragment).execute(new URL(link + "n"));
                    break;
            }
        } catch (MalformedURLException ignored) {
        }
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
            new ImageDownloader(this).execute("https://liaten.ru/libpics_small/" + coverID + ".jpg");
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
                LoadingL,newBooksList, ScrollDirection.horizontal, null).start();
    }

    @Override
    public void returnBooks(ArrayList<Book> output, String table) {

    }

    @Override
    public void returnBookCover(Bitmap cover) {
        covers.add(new BitmapDrawable(cover));
    }

    @Override
    public void returnBookCover(Bitmap output, String table) {

    }

    @Override
    public void returnTable(String active_table) {

    }
}