package com.example.library.fragment.profile;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.library.MainActivity;
import com.example.library.R;
import com.example.library.entity.Book;
import com.example.library.entity.ScrollDirection;
import com.example.library.fragment.other.BooksExtendedList;
import com.example.library.helper.BookHelperExtended;
import com.example.library.helper.FragmentHelper;
import com.example.library.helper.GetRequestFromDatabaseByUser;
import com.example.library.helper.ImageDownloader;
import com.example.library.helper.ListWaiter;
import com.example.library.helper.response.BookResponse;
import com.example.library.helper.response.JSONResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment implements BookResponse, JSONResponse {

    private static final Map<String,ArrayList<Integer>> id = new HashMap<>();
    private static final Map<String,ArrayList<Drawable>> cover = new HashMap<>();
    private static final Map<String,ArrayList<String>> coverID = new HashMap<>();
    private static final Map<String,ArrayList<String>> description = new HashMap<>();
    private static final Map<String,ArrayList<String>> title = new HashMap<>();
    private static final Map<String,ArrayList<String>> author = new HashMap<>();
    private static final Map<String, ProgressBar> loading = new HashMap<>();
    private static final Map<String,RecyclerView> recycler = new HashMap<>();
    private static final Map<String,TextView> alert = new HashMap<>();
    private static final Map<String,TextView> header = new HashMap<>();
    private static final Map<String,TextView> viewAll = new HashMap<>();
    private final String[] lists = new String[]
    {
            "wishlist_books",
            "reserved_books",
            "books_on_hands"
    };

    private TextView topTitle;
    private ProfileFragment profileFragment = this;

    private static final String TAG = "ProfileFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViews();
        setOnClickListeners();
        setTopTitle();
        updateLists();
    }

    private void setViews() {
        View v = requireView();

        topTitle = v.findViewById(R.id.full_name_profile_textview);

        viewAll.put(lists[0],v.findViewById(R.id.wishlist_view_all));
        viewAll.put(lists[1],v.findViewById(R.id.reserved_books_view_all));
        viewAll.put(lists[2],v.findViewById(R.id.books_on_hands_view_all));

        header.put(lists[0],v.findViewById(R.id.wishlist_header));
        header.put(lists[1],v.findViewById(R.id.reserved_header));
        header.put(lists[2],v.findViewById(R.id.books_on_hands_header));

        loading.put(lists[0],v.findViewById(R.id.wishlist_loading));
        loading.put(lists[1],v.findViewById(R.id.reserved_books_loading));
        loading.put(lists[2],v.findViewById(R.id.books_on_hands_loading));

        recycler.put(lists[0],v.findViewById(R.id.wishlist_recycler));
        recycler.put(lists[1],v.findViewById(R.id.reserved_books_recycler));
        recycler.put(lists[2],v.findViewById(R.id.books_on_hands_recycler));

        alert.put(lists[0],v.findViewById(R.id.wishlist_alert));
        alert.put(lists[1],v.findViewById(R.id.reserved_books_alert));
        alert.put(lists[2],v.findViewById(R.id.books_on_hands_alert));
    }

    @SuppressLint("SetTextI18n")
    private void setTopTitle(){
        topTitle.setText(MainActivity.getSP().getString("name", "Гость") +
                " " +
                MainActivity.getSP().getString("surname", ""));
    }

    private void setOnClickListeners() {
        viewAll.get(lists[0]).setOnClickListener(watchAllWishlistListener);
        viewAll.get(lists[1]).setOnClickListener(watchAllReservedListener);
        viewAll.get(lists[2]).setOnClickListener(watchAllOnHandsListener);
    }

    View.OnClickListener watchAllReservedListener = view -> {
        new FragmentHelper((MainActivity) requireActivity(),
                false,true).execute(
                new BooksExtendedList(
                        header.get(lists[1]).getText().toString(), "reserved"
                ));
    };

    View.OnClickListener watchAllWishlistListener = view -> {
        new FragmentHelper((MainActivity) requireActivity(),
                false,true).execute(
                new BooksExtendedList(
                        header.get(lists[0]).getText().toString(), "reserved"
                ));
    };

    View.OnClickListener watchAllOnHandsListener = view -> {
        new FragmentHelper((MainActivity) requireActivity(),
                false,true).execute(
                new BooksExtendedList(
                        header.get(lists[2]).getText().toString(), "reserved"
                ));
    };

    private void getBookListsByUser(String[] tables, String id_user) throws MalformedURLException {
        for (String table : tables) {
                id.put(table,new ArrayList<>());
                cover.put(table,new ArrayList<>());
                coverID.put(table,new ArrayList<>());
                description.put(table,new ArrayList<>());
                title.put(table,new ArrayList<>());
                author.put(table,new ArrayList<>());
                String link = "https://liaten.ru/db/books_from_booklists_by_user.php" +
                        "?table=" + table +
                        "&id_user=" + id_user +
                        "&limited=y";
                new BookHelperExtended(profileFragment, table).execute(new URL(link));
        }
    }

    private void updateLists() {
        String userid = MainActivity.getSP().getString("userid", "");
        if (userid.equals("")) {
            setAllListsEmpty(lists);
        } else {
            new GetRequestFromDatabaseByUser(this).execute("id", "userid", userid);
        }
    }

    @Override
    public void returnBooks(ArrayList<Book> output) {
    }

    @Override
    public void returnBooks(ArrayList<Book> output, String table) {
        id.get(table).clear();
        cover.get(table).clear();
        description.get(table).clear();
        title.get(table).clear();
        author.get(table).clear();
        coverID.get(table).clear();
        for (Book book : output
        ) {
            String coverID_s = String.valueOf(book.getCover());
            new ImageDownloader(this, table).execute
                    ("https://liaten.ru/libpics_small/" + coverID_s + ".jpg");
            id.get(table).add(book.getID());
            description.get(table).add(book.getDescription());
            title.get(table).add(book.getTitle());
            author.get(table).add(book.getAuthor());
            coverID.get(table).add(coverID_s);
        }
        new ListWaiter(requireActivity(),
                output,
                id.get(table),
                cover.get(table),
                description.get(table),
                title.get(table),
                author.get(table),
                coverID.get(table),
                loading.get(table),
                recycler.get(table),
                ScrollDirection.horizontal,
                null
        ).start();
    }

    @Override
    public void returnBookCover(Bitmap cover) {

    }

    @Override
    public void returnBookCover(Bitmap output, String table) {
        cover.get(table).add(new BitmapDrawable(output));
    }

    // Если возвращает таблицу, то значит она пустая!
    @Override
    public void returnTable(String table) {
        setListEmpty(table);
    }

    private void setListEmpty(String table){
        loading.get(table).setVisibility(View.GONE);
        recycler.get(table).setVisibility(View.GONE);
        alert.get(table).setVisibility(View.VISIBLE);
    }

    private void setAllListsEmpty(String[] tables) {
        for(String table: tables){
            loading.get(table).setVisibility(View.GONE);
            recycler.get(table).setVisibility(View.GONE);
            alert.get(table).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void returnJSONObject(JSONObject jsonObject) {
        Log.d(TAG, "returnJSONObject: " + jsonObject);
        try {
            if (jsonObject.getBoolean("success")) {
                if (jsonObject.getString("type").equals("id")) {
                    getBookListsByUser(lists, jsonObject.getString("id"));
                }
            }
        } catch (JSONException | MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
