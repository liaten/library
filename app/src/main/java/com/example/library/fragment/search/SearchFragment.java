package com.example.library.fragment.search;

import static com.example.library.MainActivity.setBottomNavigationViewUncheckable;
import static com.example.library.MainActivity.setSelectedFragment;
import static com.example.library.fragment.library.LibraryFragment.setFragmentOnParent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.library.R;
import com.example.library.fragment.FragmentWithHeader;

public class SearchFragment extends FragmentWithHeader {
    private Button findBookButton;
    View.OnClickListener findBookListener = view -> {
        setSelectedFragment(new SearchResultsFragment());
        setBottomNavigationViewUncheckable();
        setFragmentOnParent(this);
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViews();
        setOnClickListeners();
    }

    private void setViews() {
        findBookButton = requireView().findViewById(R.id.find_book);
    }

    private void setOnClickListeners() {
        findBookButton.setOnClickListener(findBookListener);
    }
}
