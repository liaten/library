package com.example.library.fragment.search;

import static com.example.library.MainActivity.getBottomNavigationView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.library.MainActivity;
import com.example.library.R;
import com.example.library.fragment.FragmentWithHeader;
import com.example.library.helper.FragmentHelper;

public class SearchFragment extends FragmentWithHeader {
    private Button findBookButton;
    View.OnClickListener findBookListener = view -> {
        new FragmentHelper((MainActivity) requireActivity(),
                false,true).execute(new SearchResultsFragment());
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
