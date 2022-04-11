package com.example.library.fragment.profile;

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
        covers.add(getResources().getDrawable(R.drawable.b02));
        String title = "Ветер в ивах";
        String author = "Мишель Плесси";
        String title_author = author + "<br><b>" +title + "</b>";
        Spanned sp = Html.fromHtml(title_author);
        titles.add(sp);

        initRecyclerViewOnHands(booksOnHandsList, requireActivity(), titles, covers);
        initRecyclerViewOnHands(reservedBooksTextList, requireActivity(), titles, covers);
        initRecyclerViewOnHands(wishlistList, requireActivity(), titles, covers);
    }

    public static void initRecyclerViewOnHands(RecyclerView recyclerView, FragmentActivity activity,
                                               ArrayList<Spanned> titles, ArrayList<Drawable> covers) {
        //Log.d(TAG, "initRecyclerView: init recyclerView");
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(activity,titles,covers);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(activity, DividerItemDecoration.HORIZONTAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(activity,R.drawable.empty_divider_horizontal));
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
