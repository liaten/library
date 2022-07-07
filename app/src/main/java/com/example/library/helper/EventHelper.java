package com.example.library.helper;

import static com.example.library.helper.NetworkHelper.getJSONFromURL;

import android.os.AsyncTask;

import com.example.library.entity.Event;
import com.example.library.response.AsyncResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

public class EventHelper extends AsyncTask<URL, Void, ArrayList<Event>> {

    public AsyncResponse delegate;

    private static final String TAG = "EventHelper";

    public EventHelper(AsyncResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected ArrayList<Event> doInBackground(URL... urls) {
        ArrayList<Event> eventList = new ArrayList<>();
        try{
            JSONObject jsonObject = new JSONObject(getJSONFromURL(urls[0]));
            JSONArray events = jsonObject.getJSONArray("events");
//            Log.d(TAG, "doInBackground: " + events);
            for(int i=0;i<events.length();i++){
                JSONObject event = events.getJSONObject(i);
                eventList.add(new Event(
                        event.getInt("id"),
                        event.getInt("visitors"),
                        event.getInt("age"),
                        event.getInt("cover"),
                        event.getString("header"),
                        event.getString("place"),
                        event.getString("description"),
                        event.getString("short_description")
                ));
            }
        } catch (JSONException ignored) {
        }
        return eventList;
    }

    @Override
    protected void onPostExecute(ArrayList<Event> events) {
        super.onPostExecute(events);
        delegate.returnEvents(events);
    }
}
