package com.example.library.fragment.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.library.MainActivity;
import com.example.library.R;
import com.example.library.helper.FragmentHelper;

public class HomeFragment extends Fragment {

    private TextView allNewsTextView = null;
    View.OnClickListener allNewsListener = view -> {
        new FragmentHelper((MainActivity) requireActivity(),
                false,false).execute(new NewBooksFragment());
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
    }

    private void setViews() {
        allNewsTextView = requireView().findViewById(R.id.all_news);
    }
    private void setOnClickListeners(){
        allNewsTextView.setOnClickListener(allNewsListener);
    }
}