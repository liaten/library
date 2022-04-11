package com.example.library.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.library.R;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<Spanned> titles;
    private ArrayList<Drawable> covers;
    private Context context;
    final float scale;
    private static final int COVER_HEIGHT = 148;


    public RecyclerViewAdapter(Context context, ArrayList<Spanned> titles, ArrayList<Drawable> covers) {
        this.context = context;
        this.titles = titles;
        this.covers = covers;
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
        int pixelsWidth = (int) (coverWidth * scale + 0.5f);
        Log.d(TAG, "coverWidth: " + coverWidth);
        int pixelsHeight = (int) (coverHeight * scale + 0.5f);
        Log.d(TAG, "coverHeight: " + coverHeight);
        int coefficient = pixelsHeight/400;
        pixelsWidth/=coefficient;
        pixelsHeight/=coefficient;
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(pixelsWidth,pixelsHeight);
        holder.cover.setLayoutParams(lp);
        //Log.d(TAG, "onCreateViewHolder: called");
        holder.title.setText(titles.get(position));
        holder.cover.setImageDrawable(covers.get(position));
        View.OnClickListener bookListener = view ->{
            //Log.d(TAG, "onClick: clicked on a book: " + titles.get(position));
            Toast.makeText(context,titles.get(position),Toast.LENGTH_SHORT).show();
        };
        holder.cover.setOnClickListener(bookListener);
        holder.title.setOnClickListener(bookListener);
    }

    @Override
    public int getItemCount() {
        return titles.size();
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
