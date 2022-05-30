package com.example.library.adapter;

import static com.example.library.MainActivity.scale;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.library.MainActivity;
import com.example.library.R;
import com.example.library.fragment.other.BookInfo;
import com.example.library.helper.FragmentHelper;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

//    private static final String TAG = "RecyclerViewAdapter";

    private final ArrayList<Drawable> covers;

    private final ArrayList<String> descriptions, titles, authors, coversIDs;

    private final ArrayList<Integer> ids;

    private final Context context;
    private static String orientation;

    private final String hyphen_regex = "[ЙЦКНГШЩЗХЪФВПРЛДЖЧСМТЬБ]*[ЁУЕЫАОЭЯИЮ][ЙЦКНГШЩЗХЪФВПРЛДЖЧСМТЬБ]*?(?=[ЦКНГШЩЗХФВПРЛДЖЧСМТБ]?[ЁУЕЫАОЭЯИЮ]|Й[АИУЕО])";
    private final Pattern hyphenPattern = Pattern.compile(hyphen_regex, Pattern.CASE_INSENSITIVE);

    public RecyclerViewAdapter(Context context, ArrayList<Integer> ids,
                               ArrayList<Drawable> covers, ArrayList<String> descriptions,
                               ArrayList<String> titles, ArrayList<String> authors,
                               ArrayList<String> coversIDs, String orientation
    ) {
        this.context = context;
        this.ids = ids;
        this.covers = covers;
        this.descriptions = descriptions;
        this.titles = titles;
        this.authors = authors;
        this.coversIDs = coversIDs;
        RecyclerViewAdapter.orientation = orientation;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (orientation){
            case "vertical":
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_book_with_cover_vertical,parent,false)
                );
            case "horizontal":
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_book_with_cover_horizontal,parent,false)
                );
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int coverWidth = covers.get(position).getIntrinsicWidth();
        int coverHeight = covers.get(position).getIntrinsicHeight();
        double pixelsWidth = coverWidth * scale + 0.5f;
        double pixelsHeight = coverHeight * scale + 0.5f;
        double coefficient = 0;
        switch (orientation){
            case "horizontal":
                coefficient = pixelsHeight / 400;
                break;
            case "vertical":
                coefficient = pixelsHeight / 250;
                break;
        }
        pixelsWidth /= coefficient;
        pixelsHeight /= coefficient;
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams((int) pixelsWidth, (int) pixelsHeight);
        holder.cover.setLayoutParams(lp);
        String author = setHyphenation(authors.get(position));
        String title = setHyphenation(titles.get(position));
        Spanned sp = Html.fromHtml(author + "<br><b>" + title + "</b>");
        holder.title.setText(sp);
        holder.cover.setImageDrawable(covers.get(position));
        View.OnClickListener bookListener = view -> {
            new FragmentHelper((MainActivity) context,
                    false, true).execute(new BookInfo(
                    ids.get(position),
                    titles.get(position),
                    authors.get(position),
                    descriptions.get(position),
                    coversIDs.get(position)
            ));
        };
        holder.layout.setOnClickListener(bookListener);
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    private String setHyphenation(String text) {
        return hyphenPattern.matcher(text).replaceAll("$0\u200b");
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView cover;
        LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.cover);
            title = itemView.findViewById(R.id.title);
            switch (orientation){
                case "vertical":
                    layout = itemView.findViewById(R.id.book_vertical_layout);
                    break;
                case "horizontal":
                    layout = itemView.findViewById(R.id.book_horizontal_layout);
                    break;
            }
        }
    }
}