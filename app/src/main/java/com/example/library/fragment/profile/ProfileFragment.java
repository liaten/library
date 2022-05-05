package com.example.library.fragment.profile;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ProfileFragment extends Fragment implements AsyncResponse {

    private TextView booksOnHandsTextView, reservedBooksTextView, wishlistTextView, topTitle;
    private RecyclerView booksOnHandsList, reservedBooksTextList, wishlistList;
    private static final ArrayList<Spanned> titles = new ArrayList<>();
    private static final ArrayList<Drawable> covers = new ArrayList<>();
    private static final String TAG = "ProfileFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViews();
        setOnClickListeners();
        getBooks();
        setTopTitle();
    }


    private void setViews() {
        View v = requireView();
        booksOnHandsTextView = v.findViewById(R.id.books_on_hands_view_all);
        reservedBooksTextView = v.findViewById(R.id.reserved_books_view_all);
        wishlistTextView = v.findViewById(R.id.wishlist_view_all);
        booksOnHandsList = v.findViewById(R.id.books_on_hands_list);
        reservedBooksTextList = v.findViewById(R.id.reserved_books_list);
        wishlistList = v.findViewById(R.id.wishlist_books_list);
        topTitle = v.findViewById(R.id.full_name_profile_textview);
    }

    private void setTopTitle(){
        String name = MainActivity.sp.getString("name", "Гость");
        String surname = MainActivity.sp.getString("surname", "");
        topTitle.setText(name + " " + surname);
    }

    private void getBooks(){
        String topBooksURL = "https://liaten.ru/db/new_7_books.php";
        try {
            URL url = new URL(topBooksURL);
            new BookHelper(this).execute(url);
        } catch (MalformedURLException e) {
//            e.printStackTrace();
        }
    }

    private void setOnClickListeners() {
        booksOnHandsTextView.setOnClickListener(booksOnHandsListener);
        reservedBooksTextView.setOnClickListener(reservedBooksListener);
        wishlistTextView.setOnClickListener(wishlistListener);
    }

    View.OnClickListener booksOnHandsListener = view -> {
        new FragmentHelper((MainActivity) requireActivity(),
                false, true).execute(new BooksOnHandsFragment());
    };
    View.OnClickListener reservedBooksListener = view -> {
        new FragmentHelper((MainActivity) requireActivity(),
                false, true).execute(new ReservedBooksFragment());
    };
    View.OnClickListener wishlistListener = view -> {
        new FragmentHelper((MainActivity) requireActivity(),
                false, true).execute(new WishlistFragment());
    };

    @Override
    public void processFinish(Boolean output) {
        //
    }

    @Override
    public void returnBooks(ArrayList<Book> output) {

    }

    @Override
    public void processFinish(Bitmap output) {

    }

    @Override
    public void returnJSONObject(JSONObject jsonObject) {

    }
}
