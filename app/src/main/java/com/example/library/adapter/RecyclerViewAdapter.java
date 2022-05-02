package com.example.library.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.library.MainActivity;
import com.example.library.R;
import com.example.library.fragment.BookInfo;
import com.example.library.helper.FragmentHelper;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private final ArrayList<Spanned> titles_authors;
    private final ArrayList<Drawable> covers;
    private final ArrayList<String> descriptions;
    private final ArrayList<String> titles;
    private final ArrayList<String> authors;
    private final Context context;
    final float scale;
    private static final int COVER_HEIGHT = 148;


    public RecyclerViewAdapter(Context context, ArrayList<Spanned> titles_authors,
                               ArrayList<Drawable> covers, ArrayList<String> descriptions,
                               ArrayList<String> titles, ArrayList<String> authors
    ) {
        this.context = context;
        this.titles_authors = titles_authors;
        this.covers = covers;
        this.descriptions = descriptions;
        this.titles = titles;
        this.authors = authors;
        scale = context.getResources().getDisplayMetrics().density;
        //Log.d(TAG, "scale: " + scale);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_book_with_cover,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int coverWidth = covers.get(position).getIntrinsicWidth();
        int coverHeight = covers.get(position).getIntrinsicHeight();
        double pixelsWidth = coverWidth * scale + 0.5f;
        double pixelsHeight = coverHeight * scale + 0.5f;
        double coefficient = pixelsHeight/400;
        pixelsWidth/=coefficient;
        pixelsHeight/=coefficient;
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams((int)pixelsWidth,(int)pixelsHeight);
        holder.cover.setLayoutParams(lp);
        holder.title.setText(titles_authors.get(position));
        holder.cover.setImageDrawable(covers.get(position));
        View.OnClickListener bookListener = view ->{
//            Toast.makeText(context,titles.get(position),Toast.LENGTH_SHORT).show();
            new FragmentHelper((MainActivity) context,
                    false,true).execute(new BookInfo(
                            titles.get(position), authors.get(position),descriptions.get(position)
            ));
        };
        holder.cover.setOnClickListener(bookListener);
        holder.title.setOnClickListener(bookListener);
    }

    @Override
    public int getItemCount() {
        return titles_authors.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        ImageView cover;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.cover);
            title = itemView.findViewById(R.id.title);
        }
    }
}