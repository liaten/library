package com.example.library.helper;

import static com.example.library.helper.DateHelper.getJSONFromURL;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.net.URL;

public class SearchEmailByEmailFromDB extends AsyncTask<String, Void, JSONObject> {

    public AsyncResponse delegate;
//    private static final String TAG = "Username";
    public SearchEmailByEmailFromDB(AsyncResponse delegate){
        this.delegate = delegate;
    }
    @Override
    protected JSONObject doInBackground(String... strings) {
        try{
            String email = strings[0];
            URL url = new URL("https://liaten.ru/db/email.php?email="+email);
            return new JSONObject(getJSONFromURL(url));
        } catch(Exception e){
            e.printStackTrace();
            return new JSONObject();
        }
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        delegate.returnJSONObject(jsonObject);
    }
}
