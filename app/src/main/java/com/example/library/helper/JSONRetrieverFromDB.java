package com.example.library.helper;

import static com.example.library.helper.NetworkHelper.getJSONFromURL;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class JSONRetrieverFromDB extends AsyncTask<String[], Void, JSONObject> {
    private static final String TAG = "JSONRetrieverFromDB";

    @Override
    protected JSONObject doInBackground(String[]... strings) {
        String link = strings[0][0];
        String[] attributeNames = strings[1];
        String[] attributeValues = strings[2];
        for (int i = 0; i < attributeNames.length; i++) {
            if (i == 0) {
                link += "?";
            } else {
                link += "&";
            }
            link+=attributeNames[i] + "=" + attributeValues[i];
        }
        Log.d(TAG, "doInBackground: link: " + link);
        try {
            return new JSONObject(getJSONFromURL(new URL(link)));
        } catch (JSONException | MalformedURLException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }
}
