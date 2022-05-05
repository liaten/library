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

import com.example.library.entity.Book;
import com.example.library.fragment.NoConnectionFragment;
import com.example.library.fragment.events.EventsFragment;
import com.example.library.fragment.home.HomeFragment;
import com.example.library.fragment.library.LibraryFragment;
import com.example.library.fragment.search.SearchFragment;
import com.example.library.helper.AsyncResponse;
import com.example.library.helper.CheckNetwork;
import com.example.library.helper.FragmentHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements AsyncResponse {

    //private static final String TAG = "MainActivity";
    private static BottomNavigationView bottomNavigationView;
    public static boolean isNetworkEnabled = false;
    public static SharedPreferences sp;
    public static float scale;

    @SuppressLint("NonConstantResourceId")
    private final BottomNavigationView.OnItemSelectedListener navigationItemSelectedListener
            = item -> {
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
        new CountDownTimer(Long.MAX_VALUE, 1000) {
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
        sp = getSharedPreferences("login", MODE_PRIVATE);
        scale = getApplicationContext().getResources().getDisplayMetrics().density;
    }

    @Override
    public void processFinish(Boolean output) {
        if (output) {
            if (!isNetworkEnabled) {
                isNetworkEnabled = true;
                //Toast.makeText(getApplicationContext(), "Есть интернет соединение",Toast.LENGTH_SHORT).show();
                new FragmentHelper(this, true, true)
                        .execute(new HomeFragment());
                //bottomNavigationView.setVisibility(View.VISIBLE);
                setBottomNavigationViewVisible();
            }
        } else {
            isNetworkEnabled = false;
            //Toast.makeText(getApplicationContext(), "Нет интернет соединения", Toast.LENGTH_SHORT).show();
            setBottomNavigationViewInvisible();
            //bottomNavigationView.setVisibility(View.GONE);
            new FragmentHelper(this, true, false)
                    .execute(new NoConnectionFragment());
        }
    }

    @Override
    public void returnBooks(ArrayList<Book> output) {
        //
    }

    @Override
    public void processFinish(Bitmap output) {
        //
    }

    @Override
    public void returnJSONObject(JSONObject jsonObject) {

    }
}