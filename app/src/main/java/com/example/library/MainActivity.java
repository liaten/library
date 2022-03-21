package com.example.library;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.library.fragment.events.EventsFragment;
import com.example.library.fragment.home.HomeFragment;
import com.example.library.fragment.library.LibraryFragment;
import com.example.library.fragment.search.SearchFragment;
import com.example.library.helper.FragmentHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    //private static Fragment selectedFragment = new HomeFragment();
    private static BottomNavigationView bottomNavigationView;
    @SuppressLint("NonConstantResourceId")
    private final BottomNavigationView.OnItemSelectedListener navigationItemSelectedListener = item -> {
        switch (item.getItemId()) {
            case R.id.nav_home:
                new FragmentHelper(this,
                        bottomNavigationView,
                        "check").execute(new HomeFragment());
                break;
            case R.id.nav_search:
                new FragmentHelper(this,
                        bottomNavigationView,
                        "check").execute(new SearchFragment());
                break;
            case R.id.nav_library:
                new FragmentHelper(this,
                        bottomNavigationView,
                        "check").execute(new LibraryFragment());
                break;
            case R.id.nav_events:
                new FragmentHelper(this,
                        bottomNavigationView,
                        "check").execute(new EventsFragment());
                break;
        }
        return true;
    };

    public static BottomNavigationView getBottomNavigationView() {
        return bottomNavigationView;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setViews();
        setOnClickListeners();
        new FragmentHelper(this, bottomNavigationView, "check").execute(new HomeFragment());
    }

    private void setViews() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }

    private void setOnClickListeners() {
        bottomNavigationView.setOnItemSelectedListener(navigationItemSelectedListener);
    }
}