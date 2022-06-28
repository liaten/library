package com.example.library;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.library.entity.Book;
import com.example.library.entity.Event;
import com.example.library.fragment.events.EventsFragment;
import com.example.library.fragment.home.HomeFragment;
import com.example.library.fragment.library.LibraryFragment;
import com.example.library.fragment.other.NoConnectionFragment;
import com.example.library.fragment.search.SearchFragment;
import com.example.library.helper.response.AsyncResponse;
import com.example.library.helper.CheckNetwork;
import com.example.library.helper.FragmentHelper;
import com.example.library.helper.response.NetworkResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements NetworkResponse {

    private static BottomNavigationView bottomNavigationView;
    public static boolean isNetworkEnabled = false;
    private static SharedPreferences sp;
    public static float scale;

    public static SharedPreferences getSP() {
        return sp;
    }

    @SuppressLint("NonConstantResourceId")
    private final BottomNavigationView.OnItemSelectedListener navigationItemSelectedListener
            = item -> {
        int selectedItemID = item.getItemId();
        switch (selectedItemID) {
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

    public static void checkNetwork(NetworkResponse networkResponse, Context context) {
        new CountDownTimer(Long.MAX_VALUE, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                new CheckNetwork(networkResponse).execute(context.getApplicationContext());
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
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setViews();
        setOnClickListeners();
        checkNetwork(this, this);
        sp = getSharedPreferences("login", MODE_PRIVATE);
        getSP().edit().putInt("enters",
                getSP().getInt("enters", 0) + 1
                ).apply();
        scale = getApplicationContext().getResources().getDisplayMetrics().density;

    }

    @Override
    public void NetworkCheckFinish(Boolean status) {
        if (status) {
            if (!isNetworkEnabled) {
                isNetworkEnabled = true;
                new FragmentHelper(this, true, true)
                        .execute(new HomeFragment());
                setBottomNavigationViewVisible();
            }
        } else {
            isNetworkEnabled = false;
            setBottomNavigationViewInvisible();
            new FragmentHelper(this, true, false)
                    .execute(new NoConnectionFragment());
        }
    }
}