package com.example.library.fragment.other;

import static com.example.library.MainActivity.scale;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.library.R;
import com.example.library.entity.Book;
import com.example.library.helper.AsyncResponse;
import com.example.library.helper.CreateUser;
import com.example.library.helper.ImageDownloader;
import com.example.library.helper.PostRequestBookUser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BookInfo extends Fragment implements AsyncResponse {

    private String title, author, description, coverID;
    private ImageView CoverView;
    private TextView TitleView, AuthorView, DescriptionView;
    private Button WishlistButton, SecondButton;
    private final int bookID;

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

    private void setViews() {
        View view = requireView();
        CoverView = view.findViewById(R.id.cover);
        TitleView = view.findViewById(R.id.title);
        AuthorView = view.findViewById(R.id.author);
        DescriptionView = view.findViewById(R.id.description);
        WishlistButton = view.findViewById(R.id.wishlist_button);
        SecondButton = view.findViewById(R.id.second_button);
    }

    private void setOnClickListeners() {
        WishlistButton.setOnClickListener(wishlistButtonListener);
        SecondButton.setOnClickListener(secondButtonListener);
    }

    View.OnClickListener wishlistButtonListener = view -> {
        if(WishlistButton.getText().equals(getResources().getString(R.string.add_to_wishlist))){
            WishlistButton.setText(getResources().getString(R.string.remove_from_wishlist));
        }
        else {
            WishlistButton.setText(getResources().getString(R.string.add_to_wishlist));
        }
    };

    View.OnClickListener secondButtonListener = view -> {
        if(SecondButton.getText().equals(getResources().getString(R.string.to_book))){
            SecondButton.setText(getResources().getString(R.string.to_unbook));
        }
        else {
            SecondButton.setText(getResources().getString(R.string.to_book));
        }
    };

    private void downloadBookCover(){
        ImageDownloader d = new ImageDownloader(this);
        d.execute("https://liaten.ru/libpics_medium/b" + coverID + ".jpg");
    }

    private void postRequestBookUser(String table, String method, String id_user, String id_book) {
        new PostRequestBookUser(this).execute(table, method, id_user, id_book);
    }

    @Override
    public void processFinish(Boolean output) {

    }

    @Override
    public void returnBooks(ArrayList<Book> output) {

    }

    @Override
    public void processFinish(Bitmap output) {
        Drawable image = new BitmapDrawable(output);
        int coverWidth = image.getIntrinsicWidth();
        int coverHeight = image.getIntrinsicHeight();
        double pixelsWidth = coverWidth * scale + 0.5f;
        double pixelsHeight = coverHeight * scale + 0.5f;
        double coefficient = pixelsHeight/600;
        pixelsWidth/=coefficient;
        pixelsHeight/=coefficient;
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams((int)pixelsWidth,(int)pixelsHeight);
        CoverView.setLayoutParams(lp);
        CoverView.setImageDrawable(image);
    }

    @Override
    public void returnJSONObject(JSONObject jsonObject) {

    }
}
