package com.example.library.fragment.other;

import static com.example.library.MainActivity.scale;
import static com.example.library.entity.BookListTypes.reserved;
import static com.example.library.entity.BookListTypes.wish;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.example.library.helper.JSONRetrieverFromDB;
import com.example.library.helper.SearchForAttribute;
import com.example.library.helper.ImageDownloader;
import com.example.library.helper.BookStatusChangerByUser;
import com.example.library.entity.BookListTypes;
import com.example.library.entity.Tables;
import com.example.library.response.ImageResponse;
import com.example.library.response.JSONResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BookInfo extends Fragment implements JSONResponse, ImageResponse {

    private final String title, author, coverID;
    private String description;
    private String user_id = "";
    private BookListTypes active_table;
    private String active_method = "";
    private ImageView CoverView;
    private TextView TitleView, AuthorView, DescriptionView;
    private Button WishlistButton, ToBookButton;

    private LinearLayout LoadingL, LoadedL;
    private boolean userid_checked = false;

    private boolean wishlist_books_checked = false;
    private boolean reserved_books_checked = false;
    private int bookID;
    private static final String TAG = "BookInfo";
    private boolean cover_downloaded = false;

    public BookInfo(int bookID, String title, String author, String coverID) {
        this.bookID = bookID;
        this.title = title;
        this.author = author;
        this.coverID = coverID;
        description = "";
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

    private void getDescriptionFromDB() {
        new JSONRetrieverFromDB().execute(
                new String[]{"https://liaten.ru/db/search_for_attribute.php"},
                new String[]{"type","typeValue","searchable","table"},
                new String[]{"id",String.valueOf(bookID),"description",Tables.book.name()});
    }

    private void setOnClickListeners() {
        WishlistButton.setOnClickListener(wishlistButtonListener);
        ToBookButton.setOnClickListener(bookButtonListener);
    }

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
        getDescriptionFromDB();
    }

    private void downloadBookCover(){
        new ImageDownloader(this).execute
                ("https://liaten.ru/libpics_medium/" + coverID + ".jpg");
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
            new SearchForAttribute(this).execute("id", "userid", userid, Tables.user.name());
        }
    }

    private void showData() {
        LoadingL.setVisibility(View.GONE);
        LoadedL.setVisibility(View.VISIBLE);
    }

    @Override
    public void returnImage(Bitmap cover) {

        WindowManager wm = (WindowManager) requireContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenHeight = size.y;

        @SuppressWarnings("deprecation") Drawable image = new BitmapDrawable(cover);
        int coverWidth = image.getIntrinsicWidth();
        int coverHeight = image.getIntrinsicHeight();
        double pixelsWidth = coverWidth * scale + 0.5f;
        double pixelsHeight = coverHeight * scale + 0.5f;
        double coefficient = screenHeight / pixelsHeight / 6;

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
    public void returnImage(Bitmap output, String table) {

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
                        active_table = wish;
                        new BookStatusChangerByUser(this).execute(active_table.name(), active_method, user_id, String.valueOf(bookID));
                        active_table = BookListTypes.reserved;
                        new BookStatusChangerByUser(this).execute(active_table.name(), active_method, user_id, String.valueOf(bookID));
                        break;
                    case "description":
                        description = jsonObject.getString("description");
                        Log.d(TAG, "description: " + description);
                        break;
                    case "insert":
                        alert = "Книга добавлена в ";
                        switch (active_table) {
                            case wish:
                                alert += "список желаемого";
                                break;
                            case reserved:
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
                            case wish:
                                alert += "списка желаемого";
                                break;
                            case reserved:
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
                        case wish:
                            alert += "желаемого";
                            break;
                        case reserved:
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
    View.OnClickListener wishlistButtonListener = view -> {
        active_table = wish;
        if(WishlistButton.getText().equals(getResources().getString(R.string.add_to_wishlist))){
            WishlistButton.setText(getResources().getString(R.string.remove_from_wishlist));
            active_method = "insert";
        }
        else {
            WishlistButton.setText(getResources().getString(R.string.add_to_wishlist));
            active_method = "delete";
        }
        new BookStatusChangerByUser(this).execute(active_table.name(), active_method, user_id, String.valueOf(bookID));
    };
    View.OnClickListener bookButtonListener = view -> {
        active_table = reserved;
        if(ToBookButton.getText().equals(getResources().getString(R.string.to_book))){
            ToBookButton.setText(getResources().getString(R.string.to_unbook));
            active_method = "insert";
        }
        else {
            ToBookButton.setText(getResources().getString(R.string.to_book));
            active_method = "delete";
        }
        new BookStatusChangerByUser(this).execute(active_table.name(), active_method, user_id, String.valueOf(bookID));
    };
}