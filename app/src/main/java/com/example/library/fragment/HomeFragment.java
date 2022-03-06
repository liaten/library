package com.example.library.fragment;

import static com.example.library.MainActivity.setBottomNavigationViewUncheckable;
import static com.example.library.MainActivity.setSelectedFragment;
import static com.example.library.fragment.LibraryFragment.setFragmentOnParent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.library.R;

public class HomeFragment extends FragmentWithHeader {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    View.OnClickListener allNewsListener = view -> {
        setSelectedFragment(new NewBooksFragment());
        setBottomNavigationViewUncheckable();
        setFragmentOnParent(this);
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViews();
    }

    private void setViews() {
        TextView allNewsTextView = getView().findViewById(R.id.all_news);
        allNewsTextView.setOnClickListener(allNewsListener);
    }
}