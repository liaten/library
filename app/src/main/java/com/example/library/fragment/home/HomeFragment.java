package com.example.library.fragment.home;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
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

    private static final ArrayList<Spanned> titles_authors = new ArrayList<>();
    private static final ArrayList<Drawable> covers = new ArrayList<>();
    private static final ArrayList<String> coversIDs = new ArrayList<>();
    private static final ArrayList<String> descriptions = new ArrayList<>();
    private static final ArrayList<String> titles = new ArrayList<>();
    private static final ArrayList<String> authors = new ArrayList<>();
    private RecyclerView newBooksList;
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
        getBooks();
    }

    private void getBooks(){
        covers.clear();
        titles_authors.clear();
        getTopNewBooks();

    }

    private void setViews() {
        allNewsTextView = requireView().findViewById(R.id.all_news);
        newBooksList = requireView().findViewById(R.id.new_books_list);
    }
    private void setOnClickListeners(){
        allNewsTextView.setOnClickListener(allNewsListener);
    }

    private void getTopNewBooks(){
        BookHelper bookHelper = new BookHelper();
        bookHelper.delegate = this;
        String topBooksURL = "https://liaten.ru/db/new_7_books.php";
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
        covers.clear();
        descriptions.clear();
        titles.clear();
        authors.clear();
        coversIDs.clear();
        for (Book book : output
        ) {
            ImageDownloader d = new ImageDownloader(this);
            String coverID = String.valueOf(book.getCover());
            if(coverID.length()<2){
                coverID = "0" + coverID;
            }
            d.execute("https://liaten.ru/libpics_small/b" + coverID + ".jpg");
            String author = book.getAuthor();
            String title = book.getTitle();
            coversIDs.add(coverID);
            descriptions.add(book.getDescription());
            titles.add(title);
            authors.add(author);
        }
        new RecyclerInitializer(requireActivity(), covers, descriptions, titles, authors, coversIDs).execute(newBooksList);
    }

    @Override
    public void processFinish(Bitmap output) {
        Drawable image = new BitmapDrawable(output);
        covers.add(image);
    }

    @Override
    public void returnJSONObject(JSONObject jsonObject) {

    }
}