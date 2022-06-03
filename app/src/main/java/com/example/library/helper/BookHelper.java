package com.example.library.helper;

import static com.example.library.helper.NetworkHelper.getJSONFromURL;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import com.example.library.entity.Book;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class BookHelper extends AsyncTask<URL, Void, ArrayList<Book>> {

    public AsyncResponse delegate;

    private static final String TAG = "BookHelper";

    public BookHelper(AsyncResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected ArrayList<Book> doInBackground(URL... urls) {
        ArrayList<Book> booksList = new ArrayList<>();
        try{
            JSONObject jsonObject = new JSONObject(getJSONFromURL(urls[0]));
            JSONArray books = jsonObject.getJSONArray("books");
//            Log.d(TAG, "doInBackground: " + books);
            for(int i=0;i<books.length();i++){
                JSONObject book = books.getJSONObject(i);
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                booksList.add(new Book(
                        book.getInt("id"),
                        book.getString("title"),
                        book.getString("author"),
                        book.getInt("cover"),
                        book.getString("theme"),
                        sdf.parse(book.getString("date")),
                        book.getString("description")
                ));
            }
        } catch (JSONException | ParseException ignored) {
        }
        return booksList;
    }

    @Override
    protected void onPostExecute(ArrayList<Book> books) {
        super.onPostExecute(books);
        delegate.returnBooks(books);
    }
}
