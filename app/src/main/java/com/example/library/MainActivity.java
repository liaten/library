package com.example.library;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.library.fragment.events.EventsFragment;
import com.example.library.fragment.home.HomeFragment;
import com.example.library.fragment.library.LibraryFragment;
import com.example.library.fragment.search.SearchFragment;
import com.example.library.helper.AsyncResponse;
import com.example.library.helper.CheckNetwork;
import com.example.library.helper.FragmentHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements AsyncResponse {
    private boolean isNetworkEnabled;

    //private static Fragment selectedFragment = new HomeFragment();
    private static BottomNavigationView bottomNavigationView;
    @SuppressLint("NonConstantResourceId")
    private final BottomNavigationView.OnItemSelectedListener navigationItemSelectedListener = item -> {
        switch (item.getItemId()) {
            case R.id.nav_home:
                new FragmentHelper(this,
                        true,true).execute(new HomeFragment());
                break;
            case R.id.nav_search:
                new FragmentHelper(this,
                        true,true).execute(new SearchFragment());
                break;
            case R.id.nav_library:
                new FragmentHelper(this,
                        true,true).execute(new LibraryFragment());
                break;
            case R.id.nav_events:
                new FragmentHelper(this,
                        true,true).execute(new EventsFragment());
                break;
        }
        return true;
    };

    @NonNull
    public static BottomNavigationView getBottomNavigationView() {
        return bottomNavigationView;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setViews();
        setOnClickListeners();
        new FragmentHelper(this, true,true).execute(new HomeFragment());
        CheckNetwork checkNetwork = new CheckNetwork();
        checkNetwork.delegate = this;
        checkNetwork.execute(this.getApplicationContext());
    }

    private void setViews() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }

    private void setOnClickListeners() {
        bottomNavigationView.setOnItemSelectedListener(navigationItemSelectedListener);
    }

    @Override
    public void processFinish(Boolean output) {
        if(output){
            isNetworkEnabled = true;
            Toast.makeText(getApplicationContext(), "Есть интернет соединение", Toast.LENGTH_SHORT).show();
        }
        else {
            isNetworkEnabled = false;
            Toast.makeText(getApplicationContext(), "Нет интернет соединения", Toast.LENGTH_SHORT).show();
        }
    }
}