package com.example.library.helper;

import static com.example.library.helper.DateHelper.getJSONFromURL;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BookHelper extends AsyncTask<URL, Void, ArrayList<Book>> {

    public AsyncResponse delegate = null;
    private static final String TAG = "BookHelper";

    @Override
    protected ArrayList<Book> doInBackground(URL... urls) {
        ArrayList<Book> booksList = new ArrayList<>();
        try{
            JSONObject jsonObject = new JSONObject(getJSONFromURL(urls[0]));
            JSONArray books = jsonObject.getJSONArray("books");
            for(int i=0;i<books.length();i++){
                JSONObject book = books.getJSONObject(i);
                //Log.d(TAG, "onPostExecute: " + book);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                booksList.add(new Book(
                        book.getInt("id"),
                        book.getString("title"),
                        book.getString("author"),
                        book.getInt("cover"),
                        book.getString("theme"),
                        sdf.parse(book.getString("date"))
                ));
            }
            //Log.d(TAG, "onPostExecute: " + booksList.size());
//            delegate.processFinish(booksList);
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
        return booksList;
    }

    @Override
    protected void onPostExecute(ArrayList<Book> books) {
        super.onPostExecute(books);
        //Log.d(TAG, "onPostExecute: " + books.size());
        delegate.returnBooks(books);
    }
}
