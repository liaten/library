package com.example.library.helper;

import static com.example.library.helper.NetworkHelper.getJSONFromURL;

import android.os.AsyncTask;
import android.util.Log;

import com.example.library.entity.Book;
import com.example.library.response.BookResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

public class BookHelper extends AsyncTask<URL, Void, ArrayList<Book>> {

    public BookResponse delegate;
    private String active_table;
    private static final String TAG = "BookHelper";

    public BookHelper(BookResponse delegate){
        this.delegate = delegate;
    }

    public BookHelper(BookResponse delegate, String active_table){
        this.delegate = delegate;
        this.active_table = active_table;
    }

    @Override
    protected ArrayList<Book> doInBackground(URL... urls) {
        ArrayList<Book> booksList = new ArrayList<>();
        try{
            JSONObject jsonObject = new JSONObject(getJSONFromURL(urls[0]));
            Log.d(TAG, "jsonObject: " + jsonObject);
            switch (jsonObject.getInt("success")){
                case 1:
                    Log.d(TAG, "doInBackground: " + jsonObject);
                    JSONArray books = jsonObject.getJSONArray("books");
                    for(int i=0;i<books.length();i++){
                        JSONObject book = books.getJSONObject(i);
                        booksList.add(new Book(
                                book.getInt("id"),
                                book.getString("title"),
                                book.getString("author"),
                                book.getInt("cover")
                        ));
                    }
                    break;
                case 0:
                    return null;
            }
        } catch (JSONException ignored) {
        }
        return booksList;
    }

    @Override
    protected void onPostExecute(ArrayList<Book> books) {
        super.onPostExecute(books);
        if(active_table!=null && books!=null){
            delegate.returnBooks(books, active_table);
        }
        else if(active_table != null){
            delegate.returnTable(active_table);
        }
        else if(books!=null){
            delegate.returnBooks(books);
        }
    }
}
