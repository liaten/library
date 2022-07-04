package com.example.library.helper;

import static com.example.library.helper.NetworkHelper.getJSONFromURL;

import android.os.AsyncTask;

import com.example.library.helper.response.JSONResponse;

import org.json.JSONObject;

import java.net.URL;

public class SearchForAttribute extends AsyncTask<String, Void, JSONObject> {

    public JSONResponse delegate;
//    private static final String TAG = "Username";
    public SearchForAttribute(JSONResponse delegate){
        this.delegate = delegate;
    }
    @Override
    protected JSONObject doInBackground(String... strings) {
        try{
            String searchable = strings[0];     // Искомый столбец
            String type = strings[1];           // Тип данных
            String typeValue = strings[2];      // Данные
            String table = strings[3];          // Таблица
            URL url = new URL("https://liaten.ru/db/search_for_attribute.php" +
                    "?type=" + type +
                    "&typeValue=" + typeValue +
                    "&searchable=" + searchable +
                    "&table=" + table);
            return new JSONObject(getJSONFromURL(url));
        } catch(Exception e){
//            e.printStackTrace();
            return new JSONObject();
        }
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        delegate.returnJSONObject(jsonObject);
    }
}
