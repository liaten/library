package com.example.library.fragment.home;

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
import com.example.library.fragment.profile.ProfileFragment;
import com.example.library.helper.FragmentHelper;
import com.example.library.helper.RecyclerInitializer;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private static final ArrayList<Spanned> titles = new ArrayList<>();
    private static final ArrayList<Drawable> covers = new ArrayList<>();
    private RecyclerView newBooksList;

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
        covers.add(getResources().getDrawable(R.drawable.little_prince));
        String author = "Антуан Де Сент-Экзюпери";
        String title = "Маленький принц";
        String author_title = author + "<br><b>" + title + "</b>";
        Spanned sp = Html.fromHtml(author_title);
        titles.add(sp);
        covers.add(getResources().getDrawable(R.drawable.b01));
        titles.add(sp);
        new RecyclerInitializer(requireActivity(), titles, covers).execute(newBooksList);
    }

    private void setViews() {
        allNewsTextView = requireView().findViewById(R.id.all_news);
        newBooksList = requireView().findViewById(R.id.new_books_list);
    }
    private void setOnClickListeners(){
        allNewsTextView.setOnClickListener(allNewsListener);
    }
}