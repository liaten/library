package com.example.library.fragment.profile;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.library.MainActivity;
import com.example.library.R;
import com.example.library.entity.Book;
import com.example.library.fragment.other.BooksExtendedList;
import com.example.library.helper.AsyncResponse;
import com.example.library.helper.BookHelperExtended;
import com.example.library.helper.FragmentHelper;
import com.example.library.helper.GetRequestFromDatabaseByUser;
import com.example.library.helper.ImageDownloader;
import com.example.library.helper.RecyclerInitializer;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ProfileFragment extends Fragment implements AsyncResponse {

    private static final ArrayList<Integer> wishlist_ids = new ArrayList<>();
    private static final ArrayList<Drawable> wishlist_covers = new ArrayList<>();
    private static final ArrayList<String> wishlist_coversIDs = new ArrayList<>();
    private static final ArrayList<String> wishlist_descriptions = new ArrayList<>();
    private static final ArrayList<String> wishlist_titles = new ArrayList<>();
    private static final ArrayList<String> wishlist_authors = new ArrayList<>();

    private static final ArrayList<Integer> reserved_ids = new ArrayList<>();
    private static final ArrayList<Drawable> reserved_covers = new ArrayList<>();
    private static final ArrayList<String> reserved_coversIDs = new ArrayList<>();
    private static final ArrayList<String> reserved_descriptions = new ArrayList<>();
    private static final ArrayList<String> reserved_titles = new ArrayList<>();
    private static final ArrayList<String> reserved_authors = new ArrayList<>();

    private TextView booksOnHandsTextView, reservedBooksTextView, wishlistTextView, topTitle, booksOnHandsHeader, reservedHeader, wishlistHeader;
    private String user_id = "";
    private RecyclerView reservedBooksRecycler, wishlistRecycler, onHandsRecycler;
    private LinearLayout LoadingReserved, LoadingWishlist;
    private TextView onHandsAlert, bookedAlert, wishlistAlert;
    private String active_header;

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
        getUserIDFromDB();
    }

    private void setViews() {
        View v = requireView();
        booksOnHandsTextView = v.findViewById(R.id.books_on_hands_view_all);
        reservedBooksTextView = v.findViewById(R.id.reserved_books_view_all);
        wishlistTextView = v.findViewById(R.id.wishlist_view_all);
        reservedBooksRecycler = v.findViewById(R.id.booked_recycler);
        wishlistRecycler = v.findViewById(R.id.wishlist_recycler);
        topTitle = v.findViewById(R.id.full_name_profile_textview);
        LoadingWishlist = v.findViewById(R.id.loading_wishlist);
        LoadingReserved = v.findViewById(R.id.loading_booked);
        onHandsAlert = v.findViewById(R.id.on_hands_alert);
        bookedAlert = v.findViewById(R.id.booked_alert);
        wishlistAlert = v.findViewById(R.id.wishlist_alert);
        onHandsRecycler = v.findViewById(R.id.on_hands_recycler);
        booksOnHandsHeader = v.findViewById(R.id.books_on_hands_header);
        reservedHeader = v.findViewById(R.id.reserved_header);
        wishlistHeader = v.findViewById(R.id.wishlist_header);
    }

    private void setTopTitle(){
        String name = MainActivity.getSP().getString("name", "Гость");
        String surname = MainActivity.getSP().getString("surname", "");
        topTitle.setText(name + " " + surname);
    }

    private void setOnClickListeners() {
        booksOnHandsTextView.setOnClickListener(watchAllOnHandsListener);
        reservedBooksTextView.setOnClickListener(watchAllReservedListener);
        wishlistTextView.setOnClickListener(watchAllWishlistListener);
    }

    View.OnClickListener watchAllReservedListener = view -> {
        new FragmentHelper((MainActivity) requireActivity(),
                false,true).execute(
                new BooksExtendedList(
                        reservedHeader.getText().toString(), "reserved"
                ));
    };

    View.OnClickListener watchAllWishlistListener = view -> {
        new FragmentHelper((MainActivity) requireActivity(),
                false,true).execute(
                new BooksExtendedList(
                        wishlistHeader.getText().toString(),"wishlist"
                ));
    };

    View.OnClickListener watchAllOnHandsListener = view -> {
        new FragmentHelper((MainActivity) requireActivity(),
                false,true).execute(
                new BooksExtendedList(
                        booksOnHandsHeader.getText().toString(), "on_hands"
                ));
    };

    private void getBookListsByUser(String[] tables, String id_user, String limited) {
        for (String table : tables) {
            try {
                String link = "https://liaten.ru/db/books_from_booklists_by_user.php" +
                        "?table=" + table +
                        "&id_user=" + id_user +
                        "&limited=" + limited;
                new BookHelperExtended(this, table).execute(new URL(link));
            } catch (MalformedURLException ignored) {
            }
        }
    }

    private void getUserIDFromDB() {
        String userid = MainActivity.getSP().getString("userid", "");
        if (!userid.equals("")) {
            new GetRequestFromDatabaseByUser(this).execute("id", "userid", userid);
        }
    }

    public class InitList extends Thread {
        private final ArrayList<Book> output;
        private final FragmentActivity activity;
        private ArrayList<Integer> ids;
        private ArrayList<Drawable> covers;
        private ArrayList<String> coversIDs;
        private ArrayList<String> descriptions;
        private ArrayList<String> titles;
        private ArrayList<String> authors;
        private LinearLayout LoadingL;
        private RecyclerView recycler;

        InitList(FragmentActivity activity, ArrayList<Book> output, ArrayList<Integer> ids,
                 ArrayList<Drawable> covers,
                 ArrayList<String> descriptions, ArrayList<String> titles,
                 ArrayList<String> authors, ArrayList<String> coversIDs, LinearLayout LoadingL,
                 RecyclerView recycler){

            this.output = output;
            this.activity = activity;
            this.ids = ids;
            this.covers = covers;
            this.descriptions = descriptions;
            this.titles = titles;
            this.authors = authors;
            this.coversIDs = coversIDs;
            this.LoadingL = LoadingL;
            this.recycler = recycler;

        }
        public void run() {
            while (output.size() > covers.size()) {
                try {
                    Log.d(TAG, "output.size = " + output.size() + " covers.size = " + covers.size());
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {
                }
            }
            new RecyclerInitializer(activity, ids, covers,
                    descriptions, titles, authors,
                    coversIDs, LoadingL).execute(recycler);
//            switch (list){
//                case "wishlist":
//                    new RecyclerInitializer(activity, wishlist_ids, covers,
//                            wishlist_descriptions, wishlist_titles, wishlist_authors,
//                            wishlist_coversIDs, LoadingWishlist).execute(wishlistRecycler);
//                    break;
//                case "reserved":
//                    new RecyclerInitializer(activity, reserved_ids, covers,
//                            reserved_descriptions, reserved_titles, reserved_authors, reserved_coversIDs,
//                            LoadingReserved).execute(reservedBooksRecycler);
//                    break;
//            }
        }
    }

    @Override
    public void processFinish(Boolean output) {
    }

    @Override
    public void returnBooks(ArrayList<Book> output) {
    }

    @Override
    public void returnBooks(ArrayList<Book> output, String table) {
        switch (table) {
            case "wishlist_books":
                wishlist_ids.clear();
                wishlist_covers.clear();
                wishlist_descriptions.clear();
                wishlist_titles.clear();
                wishlist_authors.clear();
                wishlist_coversIDs.clear();
                for (Book book : output
                ) {
                    ImageDownloader d = new ImageDownloader(this, table);
                    String coverID = String.valueOf(book.getCover());
                    d.execute("https://liaten.ru/libpics_small/" + coverID + ".jpg");
                    wishlist_ids.add(book.getID());
                    wishlist_descriptions.add(book.getDescription());
                    wishlist_titles.add(book.getTitle());
                    wishlist_authors.add(book.getAuthor());
                    wishlist_coversIDs.add(coverID);
                }
                new InitList(requireActivity(),output, wishlist_ids, wishlist_covers,
                        wishlist_descriptions,wishlist_titles,wishlist_authors,wishlist_coversIDs,
                        LoadingWishlist,wishlistRecycler).start();
                break;
            case "reserved_books":
                reserved_ids.clear();
                reserved_covers.clear();
                reserved_descriptions.clear();
                reserved_titles.clear();
                reserved_authors.clear();
                reserved_coversIDs.clear();
                for (Book book : output
                ) {
                    ImageDownloader d = new ImageDownloader(this, table);
                    String coverID = String.valueOf(book.getCover());
                    d.execute("https://liaten.ru/libpics_small/" + coverID + ".jpg");
                    reserved_ids.add(book.getID());
                    reserved_descriptions.add(book.getDescription());
                    reserved_titles.add(book.getTitle());
                    reserved_authors.add(book.getAuthor());
                    reserved_coversIDs.add(coverID);
                }
                new InitList(requireActivity(),output, reserved_ids, reserved_covers,
                        reserved_descriptions,reserved_titles,reserved_authors,reserved_coversIDs,
                        LoadingReserved,reservedBooksRecycler).start();
//                new InitList(requireActivity(),output, reserved_covers,"reserved").start();
                break;
        }
    }

    @Override
    public void returnTable(String table) {
        switch (table){
            case "wishlist_books":
                LoadingWishlist.setVisibility(View.GONE);
                wishlistRecycler.setVisibility(View.GONE);
                wishlistAlert.setVisibility(View.VISIBLE);
                break;
            case "reserved_books":
                LoadingReserved.setVisibility(View.GONE);
                reservedBooksRecycler.setVisibility(View.GONE);
                bookedAlert.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void processFinish(Bitmap output) {
    }

    @Override
    public void processFinish(Bitmap output, String table) {
        switch (table) {
            case "wishlist_books":
                wishlist_covers.add(new BitmapDrawable(output));
                break;
            case "reserved_books":
                reserved_covers.add(new BitmapDrawable(output));
                break;
        }
    }

    @Override
    public void returnJSONObject(JSONObject jsonObject) {
        Log.d(TAG, "returnJSONObject: " + jsonObject);
        try {
            String type = jsonObject.getString("type");
            if (jsonObject.getBoolean("success")) {
                switch (type) {
                    case "id":
                        user_id = jsonObject.getString("id");
                        getBookListsByUser(new String[]{"wishlist_books", "reserved_books"}, user_id, "y");
                        break;
                }
            }
        } catch (JSONException ignored) {
        }
    }
}
