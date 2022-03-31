package com.example.library.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.library.R;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> titles;
    private ArrayList<String> authors;
    private ArrayList<Drawable> covers;
    private Context context;

    public RecyclerViewAdapter(Context context, ArrayList<String> titles, ArrayList<String> authors, ArrayList<Drawable> covers) {
        this.titles = titles;
        this.authors = authors;
        this.context = context;
        this.covers = covers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_book_with_cover,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onCreateViewHolder: called");
        holder.author.setText(authors.get(position));
        holder.title.setText(titles.get(position));
        holder.cover.setImageDrawable(covers.get(position));
        View.OnClickListener bookListener = view ->{
            Log.d(TAG, "onClick: clicked on a book: " + titles.get(position));
            Toast.makeText(context,titles.get(position)+" "+authors.get(position),Toast.LENGTH_SHORT).show();
        };
        holder.cover.setOnClickListener(bookListener);
        holder.title.setOnClickListener(bookListener);
        holder.author.setOnClickListener(bookListener);
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView author, title;
        ImageView cover;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.cover);
            author = itemView.findViewById(R.id.author);
            title = itemView.findViewById(R.id.title);
        }
    }
}
