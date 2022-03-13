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
import com.example.library.fragment.home.NewBooksFragment;

public class ProfileFragment extends FragmentWithHeader {

    private TextView booksOnHandsTextView, reservedBooksTextView, wishlistTextView;
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
    }
    private void setViews() {
        booksOnHandsTextView = getView().findViewById(R.id.books_on_hands_view_all);
        reservedBooksTextView = getView().findViewById(R.id.reserved_books_view_all);
        wishlistTextView = getView().findViewById(R.id.wishlist_view_all);
    }
    private void setOnClickListeners(){
        booksOnHandsTextView.setOnClickListener(booksOnHandsListener);
        reservedBooksTextView.setOnClickListener(reservedBooksListener);
        wishlistTextView.setOnClickListener(wishlistListener);
    }
}
