package com.example.library.helper;

import static com.example.library.helper.NetworkHelper.getJSONFromURL;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.net.URL;

public class PostRequestBookUser extends AsyncTask<String, Void, JSONObject> {

    public AsyncResponse delegate;

    public PostRequestBookUser(AsyncResponse delegate) {
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
            URL url = new URL(link);
            return new JSONObject(getJSONFromURL(url));
        } catch (Exception e) {
            return new JSONObject();
        }
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        delegate.returnJSONObject(jsonObject);
    }
}
