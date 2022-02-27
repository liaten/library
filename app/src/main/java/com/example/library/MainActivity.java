package com.example.library;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private static Fragment selectedFragment = new HomeFragment();
    private final BottomNavigationView.OnItemSelectedListener navigationItemSelectedListener = item -> {
        switch (item.getItemId()) {
            case R.id.nav_home:
                selectedFragment = new HomeFragment();
                break;
            case R.id.nav_search:
                selectedFragment = new SearchFragment();
                break;
            case R.id.nav_library:
                selectedFragment = new LibraryFragment();
                break;
            case R.id.nav_events:
                selectedFragment = new EventsFragment();
                break;
        }
        setSelectedFragment();
        return true;
    };

    public static Fragment getSelectedFragment(){
        return selectedFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(navigationItemSelectedListener);
        setSelectedFragment();
    }

    private void setSelectedFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
    }
}