package com.example.library.helper;

import static com.example.library.helper.DateHelper.getJSONFromURL;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.net.URL;

public class GetRequestFromDatabaseByUser extends AsyncTask<String, Void, JSONObject> {

    public AsyncResponse delegate;
//    private static final String TAG = "Username";
    public GetRequestFromDatabaseByUser(AsyncResponse delegate){
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
