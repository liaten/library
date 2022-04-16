package com.example.library;

import static com.example.library.helper.DatabaseHelper.getSQLDate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import com.example.library.helper.DatabaseHelper;
import com.example.library.helper.FragmentHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity implements AsyncResponse {

    //private static final String TAG = "MainActivity";
    private static BottomNavigationView bottomNavigationView;
    public static boolean isNetworkEnabled = false;
    public SharedPreferences sp;
    DatabaseHelper db;

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
        db = new DatabaseHelper(this);
        setContentView(R.layout.activity_main);
        setViews();
        setOnClickListeners();
        checkNetwork(this, this);
        checkBooks();
        sp = getSharedPreferences("login", MODE_PRIVATE);
    }

    @Override
    public void processFinish(Boolean output) {
        if (output) {
            if (output != isNetworkEnabled) {
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

    private void checkBooks(){
        if (getBooksNumber() == 0) {
            //Toast.makeText(getApplicationContext(), "Книг в базе нет", Toast.LENGTH_SHORT).show();
            addBooks();
        }
    }

    private void addBooks(){
        db.addBook("Маленький принц",
                "Антуан Де Сент-Экзюпери",
                "13",
                "Классические сказки зарубежных писателей",
                getSQLDate(13, 7,2000));
        db.addBook("Время всегда хорошее",
                "Андрей Жвалевский",
                "01",
                "Мистика. Фантастика. Фэнтези.",
                getSQLDate(12, 5,2002));
        db.addBook("Вратарь и море",
                "Мария Парр",
                "03",
                "Повести и рассказы о детях",
                getSQLDate(12, 5,2001));
        db.addBook("Программирование для детей на языке Python",
                "Банкрашков А.В.",
                "04",
                "Программирование и электроника для детей",
                getSQLDate(12, 5,2017));
        db.addBook("Когда прилетит комета",
                "Янсон Туве Марика",
                "05",
                "Художественная литература",
                getSQLDate(12, 5,2017));
        db.addBook("Вафельное сердце",
                "Мария Парр",
                "06",
                "Приключения",
                getSQLDate(12, 5,2020));
        db.addBook("Мастер и Маргарита",
                "Михаил Буглагов",
                "07",
                "Художественная литература",
                getSQLDate(12, 5,2020));
        db.addBook("Льюис Кэролл",
                "Алиса в стране чудес",
                "08",
                "Сказки, фольклор и мифы",
                getSQLDate(12, 5,2020));
        db.addBook("Мария Парр",
                "Тоня Глиммердал",
                "09",
                "Современная проза для детей",
                getSQLDate(12, 5,2020));
        db.addBook("Чудо",
                "Паласио Р. Дж.",
                "10",
                "Современная проза для детей",
                getSQLDate(12, 5,2020));
        db.addBook("Дочь реки",
                "Дэниел Кларк, Джеймс Кларк",
                "11",
                "Комиксы",
                getSQLDate(12, 5,2020));

    }

    private int getBooksNumber(){
        Cursor cursor = db.getBooksCount();
        int booksNumber = 0;
        if (cursor.getCount() != 0){
            while (cursor.moveToNext()) {
                booksNumber = cursor.getInt(0);
            }
        }
        return booksNumber;
    }
}