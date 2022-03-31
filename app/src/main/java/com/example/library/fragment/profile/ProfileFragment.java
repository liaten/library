package com.example.library.fragment.profile;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.library.MainActivity;
import com.example.library.R;
import com.example.library.adapter.RecyclerViewAdapter;
import com.example.library.helper.FragmentHelper;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    private TextView booksOnHandsTextView, reservedBooksTextView, wishlistTextView;
    private RecyclerView booksOnHandsList, reservedBooksTextList, wishlistList;
    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<String> authors = new ArrayList<>();
    private ArrayList<Drawable> covers = new ArrayList<>();
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
        booksOnHandsList = requireActivity().findViewById(R.id.books_on_hands_list);
        reservedBooksTextList = requireActivity().findViewById(R.id.reserved_books_list);
        wishlistList = requireActivity().findViewById(R.id.wishlist_books_list);
    }

    private void getBooks(){
        for (int i=0;i<7;i++){
            covers.add(getResources().getDrawable(R.drawable.little_prince));
            authors.add("Антуан де сент экзюпери");
            titles.add("Маленький принц");
        }
        initRecyclerViewOnHands(booksOnHandsList);
        initRecyclerViewOnHands(reservedBooksTextList);
        initRecyclerViewOnHands(wishlistList);
    }

    private void initRecyclerViewOnHands(RecyclerView recyclerView) {
        Log.d(TAG, "initRecyclerView: init recyclerView");
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(requireActivity(),titles,authors,covers);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(requireActivity(), DividerItemDecoration.HORIZONTAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(requireActivity(),R.drawable.empty_divider_horizontal));
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setVisibility(View.VISIBLE);
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
