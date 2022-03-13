package com.example.library;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.library.fragment.events.EventsFragment;
import com.example.library.fragment.home.HomeFragment;
import com.example.library.fragment.library.LibraryFragment;
import com.example.library.fragment.search.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private static Fragment selectedFragment = new HomeFragment();
    private static BottomNavigationView bottomNavigationView;
    @SuppressLint("NonConstantResourceId")
    private final BottomNavigationView.OnItemSelectedListener navigationItemSelectedListener = item -> {
        switch (item.getItemId()) {
            case R.id.nav_home:
                setSelectedFragment(new HomeFragment());
                break;
            case R.id.nav_search:
                setSelectedFragment(new SearchFragment());
                break;
            case R.id.nav_library:
                setSelectedFragment(new LibraryFragment());
                break;
            case R.id.nav_events:
                setSelectedFragment(new EventsFragment());
                break;
        }
        setBottomNavigationViewCheckable();
        setSelectedFragmentToContainer();
        return true;
    };

    public static Fragment getSelectedFragment() {
        return selectedFragment;
    }

    public static void setSelectedFragment(Fragment fragment) {
        selectedFragment = fragment;
    }

    public static void setBottomNavigationViewUncheckable() {
        bottomNavigationView.getMenu().setGroupCheckable(0, false, true);
    }

    public static void setBottomNavigationViewCheckable() {
        bottomNavigationView.getMenu().setGroupCheckable(0, true, true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(navigationItemSelectedListener);
        setSelectedFragmentToContainer();
    }

    public void setSelectedFragmentToContainer() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
    }
}