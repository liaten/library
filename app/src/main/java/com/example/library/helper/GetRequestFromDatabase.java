package com.example.library.helper;

import static com.example.library.helper.DateHelper.getJSONFromURL;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;

public class GetRequestFromDatabase extends AsyncTask<String, Void, JSONObject> {

    public AsyncResponse delegate = null;
    private static final String TAG = "Username";
    public GetRequestFromDatabase(AsyncResponse delegate){
        this.delegate = delegate;
    }
    @Override
    protected JSONObject doInBackground(String... strings) {
        try{
            String userid = strings[0];
            String searchable = strings[1];
            URL url = new URL("https://liaten.ru/db/"+searchable+".php?userid="+userid);
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
