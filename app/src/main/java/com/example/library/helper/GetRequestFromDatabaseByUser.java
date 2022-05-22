package com.example.library.helper;

import static com.example.library.helper.NetworkHelper.getJSONFromURL;

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
            String searchable = strings[0];     // Искомый столбец
            String type = strings[1];           // Тип данных
            String typeValue = strings[2];      // Данные
            URL url = new URL("https://liaten.ru/db/search_from_user.php?type=" +
                    type + "&typeValue=" + typeValue + "&searchable=" + searchable);
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
