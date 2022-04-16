package com.example.library.fragment.home;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
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
import com.example.library.helper.Book;
import com.example.library.helper.DatabaseHelper;
import com.example.library.helper.FragmentHelper;
import com.example.library.helper.RecyclerInitializer;

import java.util.ArrayList;
import java.util.Date;

public class HomeFragment extends Fragment {

    private static final ArrayList<Spanned> titles = new ArrayList<>();
    private static final ArrayList<Drawable> covers = new ArrayList<>();
    private RecyclerView newBooksList;
    private static final String TAG = "HomeFragment";
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
        ArrayList<Book> books = getTopNewBooks();
        Resources resources = getResources();
        for (Book book : books
             ) {
            final int resourceID = resources.getIdentifier("b" + book.getCover(),"drawable",
                    getContext().getPackageName());
            covers.add(resources.getDrawable(resourceID));
            String author = book.getAuthor();
            String title = book.getTitle();
            Spanned sp = Html.fromHtml(author + "<br><b>" + title + "</b>");
            titles.add(sp);
        }
        new RecyclerInitializer(requireActivity(), titles, covers).execute(newBooksList);
    }

    private void setViews() {
        allNewsTextView = requireView().findViewById(R.id.all_news);
        newBooksList = requireView().findViewById(R.id.new_books_list);
        db = new DatabaseHelper(this.requireActivity());
    }
    private void setOnClickListeners(){
        allNewsTextView.setOnClickListener(allNewsListener);
    }

    private ArrayList<Book> getTopNewBooks(){
        Cursor cursor = db.getTopNewBooks(7);
        ArrayList<Book> books = new ArrayList<>();
        if (cursor.getCount() != 0){
            while (cursor.moveToNext()) {
                books.add(new Book(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        new Date(cursor.getLong(5))
                ));
            }
        }
        return books;
    }
}