package com.example.library.fragment.profile;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.library.MainActivity;
import com.example.library.R;
import com.example.library.helper.BookHelper;
import com.example.library.helper.FragmentHelper;

public class ProfileFragment extends Fragment {

    private TextView booksOnHandsTextView, reservedBooksTextView, wishlistTextView;
    private LinearLayout booksOnHandsList;

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
        setFirstBookOnBookList();
    }

    private void setViews() {
        booksOnHandsTextView = requireView().findViewById(R.id.books_on_hands_view_all);
        reservedBooksTextView = requireView().findViewById(R.id.reserved_books_view_all);
        wishlistTextView = requireView().findViewById(R.id.wishlist_view_all);
        booksOnHandsList = requireView().findViewById(R.id.books_on_hands_list);
    }

    private void setFirstBookOnBookList(){
        LayoutInflater inflater = (LayoutInflater) requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout book = (LinearLayout) inflater.inflate(R.layout.fragment_book_with_cover,null);
        booksOnHandsList.addView(book,0);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) book.getLayoutParams();
        params.setMarginStart(50);
        book.setLayoutParams(params);
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
}
