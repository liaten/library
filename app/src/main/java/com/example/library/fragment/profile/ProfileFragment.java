package com.example.library.fragment.profile;

import android.content.res.Resources;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.library.MainActivity;
import com.example.library.R;
import com.example.library.adapter.RecyclerViewAdapter;
import com.example.library.helper.FragmentHelper;
import com.example.library.helper.RecyclerInitializer;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    private TextView booksOnHandsTextView, reservedBooksTextView, wishlistTextView;
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
    }

    private void setViews() {
        booksOnHandsTextView = requireView().findViewById(R.id.books_on_hands_view_all);
        reservedBooksTextView = requireView().findViewById(R.id.reserved_books_view_all);
        wishlistTextView = requireView().findViewById(R.id.wishlist_view_all);
        booksOnHandsList = requireView().findViewById(R.id.books_on_hands_list);
        reservedBooksTextList = requireView().findViewById(R.id.reserved_books_list);
        wishlistList = requireView().findViewById(R.id.wishlist_books_list);
    }

    private void getBooks(){
        covers.clear();
        titles.clear();
        Resources resources = getResources();
        int resourceId1 = resources.getIdentifier("b13", "drawable",
                getContext().getPackageName());
        covers.add(resources.getDrawable(resourceId1));
        String author = "Антуан Де Сент-Экзюпери";
        String title = "Маленький принц";
        Spanned sp = Html.fromHtml(author + "<br><b>" + title + "</b>");
        titles.add(sp);
        int resourceId2 = resources.getIdentifier("b13", "drawable",
                getContext().getPackageName());
        covers.add(resources.getDrawable(resourceId2));
        titles.add(sp);
        new RecyclerInitializer(requireActivity(), titles, covers).execute(booksOnHandsList);
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
