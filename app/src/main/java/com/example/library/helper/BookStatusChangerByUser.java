package com.example.library.helper;

import static com.example.library.helper.NetworkHelper.getJSONFromURL;

import android.os.AsyncTask;

import com.example.library.response.JSONResponse;

import org.json.JSONObject;

import java.net.URL;

public class BookStatusChangerByUser extends AsyncTask<String, Void, JSONObject> {

    public JSONResponse delegate;

    public BookStatusChangerByUser(JSONResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected JSONObject doInBackground(String... strings) {
        try {
            String table = strings[0];
            String method = strings[1];
            String id_user = strings[2];
            String id_book = strings[3];
            String link = "https://liaten.ru/db/book_user_lists.php" +
                    "?table=" + table +
                    "&method=" + method +
                    "&id_user=" + id_user +
                    "&id_book=" + id_book;
            return new JSONObject(getJSONFromURL(new URL(link)));
        } catch (Exception e) {
            return new JSONObject();
        }
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        delegate.returnJSONObject(jsonObject);
    }
}
