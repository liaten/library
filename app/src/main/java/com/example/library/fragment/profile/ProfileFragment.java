package com.example.library.fragment.profile;

import static com.example.library.MainActivity.setBottomNavigationViewUncheckable;
import static com.example.library.MainActivity.setSelectedFragment;
import static com.example.library.fragment.library.LibraryFragment.setFragmentOnParent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.library.R;
import com.example.library.fragment.FragmentWithHeader;
import com.example.library.helper.BookHelper;

public class ProfileFragment extends FragmentWithHeader {

    private TextView booksOnHandsTextView, reservedBooksTextView, wishlistTextView, authorTextView,
            titleTextView;

    View.OnClickListener booksOnHandsListener = view -> {
        setSelectedFragment(new BooksOnHandsFragment());
        setBottomNavigationViewUncheckable();
        setFragmentOnParent(this);
    };

    View.OnClickListener reservedBooksListener = view -> {
        setSelectedFragment(new ReservedBooksFragment());
        setBottomNavigationViewUncheckable();
        setFragmentOnParent(this);
    };

    View.OnClickListener wishlistListener = view -> {
        setSelectedFragment(new WishlistFragment());
        setBottomNavigationViewUncheckable();
        setFragmentOnParent(this);
    };

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
        testTextViews();
    }

    private void setViews() {
        booksOnHandsTextView = requireView().findViewById(R.id.books_on_hands_view_all);
        reservedBooksTextView = requireView().findViewById(R.id.reserved_books_view_all);
        wishlistTextView = requireView().findViewById(R.id.wishlist_view_all);
        authorTextView = requireView().findViewById(R.id.author_test);
        titleTextView = requireView().findViewById(R.id.title_test);
    }

    private void setOnClickListeners() {
        booksOnHandsTextView.setOnClickListener(booksOnHandsListener);
        reservedBooksTextView.setOnClickListener(reservedBooksListener);
        wishlistTextView.setOnClickListener(wishlistListener);
    }

    private void testTextViews() {
        String name = "Антуан";
        String surname = "де Сент-Экзюпери";
        String title = "Маленький принц";
        new BookHelper(authorTextView, "author").execute(surname, name);
        new BookHelper(titleTextView, "title").execute(title);
    }
}
