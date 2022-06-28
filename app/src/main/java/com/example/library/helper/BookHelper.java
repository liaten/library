package com.example.library.helper;

import static com.example.library.helper.NetworkHelper.getJSONFromURL;

import android.os.AsyncTask;

import com.example.library.entity.Book;
import com.example.library.helper.response.BookResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

public class BookHelper extends AsyncTask<URL, Void, ArrayList<Book>> {

    public BookResponse delegate;

    private static final String TAG = "BookHelper";

    public BookHelper(BookResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected ArrayList<Book> doInBackground(URL... urls) {
        ArrayList<Book> booksList = new ArrayList<>();
        try{
            JSONObject jsonObject = new JSONObject(getJSONFromURL(urls[0]));
            JSONArray books = jsonObject.getJSONArray("books");
            for(int i=0;i<books.length();i++){
                JSONObject book = books.getJSONObject(i);
                booksList.add(new Book(
                        book.getInt("id"),
                        book.getString("title"),
                        book.getString("author"),
                        book.getInt("cover"),
                        book.getString("theme"),
                        book.getString("description")
                ));
            }
        } catch (JSONException ignored) {
        }
        return booksList;
    }

    @Override
    protected void onPostExecute(ArrayList<Book> books) {
        super.onPostExecute(books);
        delegate.returnBooks(books);
    }
}
