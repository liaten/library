package com.example.library.fragment.other;

import static com.example.library.MainActivity.scale;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.library.MainActivity;
import com.example.library.R;
import com.example.library.entity.Book;
import com.example.library.helper.AsyncResponse;
import com.example.library.helper.GetRequestFromDatabaseByUser;
import com.example.library.helper.ImageDownloader;
import com.example.library.helper.PostRequestBookUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BookInfo extends Fragment implements AsyncResponse {

    private final String title, author, description, coverID;
    private String user_id = "";
    private String active_table = "";
    private String active_method = "";
    private ImageView CoverView;
    private TextView TitleView, AuthorView, DescriptionView;
    private Button WishlistButton, ToBookButton;

    private boolean wishlist_books_checked = false;
    private boolean reserved_books_checked = false;
    private int bookID;
    private static final String TAG = "BookInfo";
    private boolean cover_downloaded = false;

    public BookInfo(int bookID, String title, String author, String description, String coverID) {
        this.bookID = bookID;
        this.title = title;
        this.author = author;
        this.description = description;
        this.coverID = coverID;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_book_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViews();
        setOnClickListeners();
        setDetails();
        downloadBookCover();
        setBookStatus();
    }

    private void setDetails() {
        String regex = "[ЙЦКНГШЩЗХЪФВПРЛДЖЧСМТЬБ]*[ЁУЕЫАОЭЯИЮ][ЙЦКНГШЩЗХЪФВПРЛДЖЧСМТЬБ]*?(?=[ЦКНГШЩЗХФВПРЛДЖЧСМТБ]?[ЁУЕЫАОЭЯИЮ]|Й[АИУЕО])";
        Pattern myPattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = myPattern.matcher(author);
        String authorS = m.replaceAll("$0\u200b");
        m = myPattern.matcher(title);
        String titleS = m.replaceAll("$0\u200b");
        m = myPattern.matcher(description);
        String descriptionS = m.replaceAll("$0\u200b");
        TitleView.setText(titleS);
        AuthorView.setText(authorS);
        DescriptionView.setText(descriptionS);
    }
    private boolean userid_checked = false;

    private void setOnClickListeners() {
        WishlistButton.setOnClickListener(wishlistButtonListener);
        ToBookButton.setOnClickListener(bookButtonListener);
    }
    private LinearLayout LoadingL, LoadedL;

    View.OnClickListener wishlistButtonListener = view -> {
        active_table = "wishlist_books";
        if(WishlistButton.getText().equals(getResources().getString(R.string.add_to_wishlist))){
            WishlistButton.setText(getResources().getString(R.string.remove_from_wishlist));
            active_method = "insert";
        }
        else {
            WishlistButton.setText(getResources().getString(R.string.add_to_wishlist));
            active_method = "delete";
        }
        createPostRequestBookUser(active_table, active_method, user_id, String.valueOf(bookID));
    };
    View.OnClickListener bookButtonListener = view -> {
        active_table = "reserved_books";
        if(ToBookButton.getText().equals(getResources().getString(R.string.to_book))){
            ToBookButton.setText(getResources().getString(R.string.to_unbook));
            active_method = "insert";
        }
        else {
            ToBookButton.setText(getResources().getString(R.string.to_book));
            active_method = "delete";
        }
        createPostRequestBookUser(active_table, active_method, user_id, String.valueOf(bookID));
    };

    private void setViews() {
        View view = requireView();
        CoverView = view.findViewById(R.id.cover);
        TitleView = view.findViewById(R.id.title);
        AuthorView = view.findViewById(R.id.author);
        DescriptionView = view.findViewById(R.id.description);
        WishlistButton = view.findViewById(R.id.wishlist_button);
        ToBookButton = view.findViewById(R.id.second_button);
        LoadingL = view.findViewById(R.id.loading);
        LoadedL = view.findViewById(R.id.loaded);
        LoadingL.setVisibility(View.VISIBLE);
        LoadedL.setVisibility(View.GONE);
    }

    private void setBookStatus(){
        active_method = "select";
        getUserIDFromDB();
    }

    private void downloadBookCover(){
        ImageDownloader d = new ImageDownloader(this);
        d.execute("https://liaten.ru/libpics_medium/" + coverID + ".jpg");
    }

    private void createPostRequestBookUser(String table, String method, String id_user, String id_book) {
        new PostRequestBookUser(this).execute(table, method, id_user, id_book);
    }

    private void getUserIDFromDB() {
        String userid = MainActivity.getSP().getString("userid", "");
        if (userid.equals("")) {
            WishlistButton.setEnabled(false);
            ToBookButton.setEnabled(false);
            userid_checked = true;
            if (cover_downloaded) {
                showData();
            }
        } else {
            new GetRequestFromDatabaseByUser(this).execute("id", "userid", userid);
        }
    }

    private void showData() {
        LoadingL.setVisibility(View.GONE);
        LoadedL.setVisibility(View.VISIBLE);
    }

    @Override
    public void processFinish(Boolean output) {

    }

    @Override
    public void returnBooks(ArrayList<Book> output) {

    }

    @Override
    public void returnBooks(ArrayList<Book> output, String table) {

    }

    @Override
    public void returnTable(String table) {

    }

    @SuppressWarnings("deprecation")
    @Override
    public void processFinish(Bitmap output) {
        Drawable image = new BitmapDrawable(output);
        int coverWidth = image.getIntrinsicWidth();
        int coverHeight = image.getIntrinsicHeight();
        double pixelsWidth = coverWidth * scale + 0.5f;
        double pixelsHeight = coverHeight * scale + 0.5f;
        double coefficient = pixelsHeight / 600;
        pixelsWidth /= coefficient;
        pixelsHeight /= coefficient;
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams((int) pixelsWidth, (int) pixelsHeight);
        CoverView.setLayoutParams(lp);
        CoverView.setImageDrawable(image);
        cover_downloaded = true;
        if (userid_checked) {
            showData();
        }
    }

    @Override
    public void processFinish(Bitmap output, String table) {

    }

    @Override
    public void returnJSONObject(JSONObject jsonObject) {
        String alert;
        Log.d(TAG, "returnJSONObject: " + jsonObject);
        try {
            String type = jsonObject.getString("type");
            if (jsonObject.getBoolean("success")) {
                switch (type) {
                    case "id":
                        user_id = jsonObject.getString("id");
                        active_table = "wishlist_books";
                        createPostRequestBookUser(active_table, active_method, user_id, String.valueOf(bookID));
                        active_table = "reserved_books";
                        createPostRequestBookUser(active_table, active_method, user_id, String.valueOf(bookID));
                        break;
                    case "insert":
                        alert = "Книга добавлена в ";
                        switch (active_table) {
                            case "wishlist_books":
                                alert += "список желаемого";
                                break;
                            case "reserved_books":
                                alert += "\"забронированные\"";
                                break;
                        }
                        Toast.makeText(requireActivity(),
                                alert,
                                Toast.LENGTH_SHORT).show();
                        break;
                    case "delete":
                        alert = "Книга удалена из ";
                        switch (active_table){
                            case "wishlist_books":
                                alert += "списка желаемого";
                                break;
                            case "reserved_books":
                                alert += "списка забронированных";
                                break;
                        }
                        Toast.makeText(requireActivity(),
                                alert,
                                Toast.LENGTH_SHORT).show();
                        break;
                    case "select":
                        String select_result = jsonObject.getString("select");
                        String table = jsonObject.getString("table");
                        switch (select_result) {
                            case "No entries":
                                switch (table) {
                                    case "wishlist_books":
                                        WishlistButton.setText(getResources().getString(R.string.add_to_wishlist));
                                        wishlist_books_checked = true;
                                        break;
                                    case "reserved_books":
                                        ToBookButton.setText(getResources().getString(R.string.to_book));
                                        reserved_books_checked = true;
                                        break;
                                }
                                break;
                            default:
                                switch (table) {
                                    case "wishlist_books":
                                        WishlistButton.setText(getResources().getString(R.string.remove_from_wishlist));
                                        wishlist_books_checked = true;
                                        break;
                                    case "reserved_books":
                                        ToBookButton.setText(getResources().getString(R.string.to_unbook));
                                        reserved_books_checked = true;
                                        break;
                                }
                                break;
                        }
                        if (wishlist_books_checked && reserved_books_checked && cover_downloaded) {
                            showData();
                        }
                        break;
                }
            }
            else {
                if (type.equals("insert")) {
                    alert = "Книга уже в списке ";
                    switch (active_table) {
                        case "wishlist_books":
                            alert += "желаемого";
                            break;
                        case "reserved_books":
                            alert += "забронированных";
                            break;
                    }
                    Toast.makeText(requireActivity(),
                            alert,
                            Toast.LENGTH_SHORT).show();
                }
            }
        } catch (JSONException ignored) {
        }
    }
}
