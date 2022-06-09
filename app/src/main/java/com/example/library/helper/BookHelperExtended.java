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

public class BookHelperExtended extends AsyncTask<URL, Void, ArrayList<Book>> {

    public AsyncResponse delegate;
    private String active_table;
    private static final String TAG = "BookHelperExtended";


    public BookHelperExtended(AsyncResponse delegate, String active_table){
        this.delegate = delegate;
        this.active_table = active_table;
    }

    @Override
    protected ArrayList<Book> doInBackground(URL... urls) {
        ArrayList<Book> booksList = new ArrayList<>();
        try{
            JSONObject jsonObject = new JSONObject(getJSONFromURL(urls[0]));
            switch (jsonObject.getInt("success")){
                case 1:
                    Log.d(TAG, "doInBackground: " + jsonObject);
                    JSONArray books = jsonObject.getJSONArray("books");
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
                                book.getString("description")
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
        if(books!=null){
            delegate.returnBooks(books, active_table);
        }
        else {
            delegate.returnTable(active_table);
        }
    }
}
