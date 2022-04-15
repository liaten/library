package com.example.library;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.library.fragment.NoConnectionFragment;
import com.example.library.fragment.events.EventsFragment;
import com.example.library.fragment.home.HomeFragment;
import com.example.library.fragment.library.LibraryFragment;
import com.example.library.fragment.search.SearchFragment;
import com.example.library.helper.AsyncResponse;
import com.example.library.helper.CheckNetwork;
import com.example.library.helper.FragmentHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements AsyncResponse {
    private static final String TAG = "MainActivity";

    //private static Fragment selectedFragment = new HomeFragment();
    private static BottomNavigationView bottomNavigationView;
    public static boolean isNetworkEnabled = false;
    @SuppressLint("NonConstantResourceId")
    private final BottomNavigationView.OnItemSelectedListener navigationItemSelectedListener = item -> {
        switch (item.getItemId()) {
            case R.id.nav_home:
                new FragmentHelper(this,
                        true, true).execute(new HomeFragment());
                break;
            case R.id.nav_search:
                new FragmentHelper(this,
                        true, true).execute(new SearchFragment());
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

    public static void checkNetwork(AsyncResponse asyncResponse, Context context) {
        CountDownTimer t = new CountDownTimer(Long.MAX_VALUE, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                CheckNetwork checkNetwork = new CheckNetwork();
                checkNetwork.delegate = asyncResponse;
                checkNetwork.execute(context.getApplicationContext());
            }

            @Override
            public void onFinish() {
            }
        }.start();

    }

    private void setViews() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }

    private void setOnClickListeners() {
        bottomNavigationView.setOnItemSelectedListener(navigationItemSelectedListener);
    }

    public static void setBottomNavigationViewInvisible() {
        bottomNavigationView.setVisibility(View.GONE);
    }

    public static void setBottomNavigationViewVisible() {
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setViews();
        setOnClickListeners();
        checkNetwork(this, this);
    }

    @Override
    public void processFinish(Boolean output) {
        if (output) {
            if (output != isNetworkEnabled) {
                isNetworkEnabled = true;
                //Toast.makeText(getApplicationContext(), "Есть интернет соединение", Toast.LENGTH_SHORT).show();
                new FragmentHelper(this, true, true).execute(new HomeFragment());
                bottomNavigationView.setVisibility(View.VISIBLE);
            }
        } else {
            isNetworkEnabled = false;
            //Toast.makeText(getApplicationContext(), "Нет интернет соединения", Toast.LENGTH_SHORT).show();
            bottomNavigationView.setVisibility(View.GONE);
            new FragmentHelper(this, true, false).execute(new NoConnectionFragment());
        }
    }
}