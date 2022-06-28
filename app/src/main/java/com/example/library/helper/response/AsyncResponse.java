package com.example.library.helper.response;

import com.example.library.entity.Event;

import org.json.JSONObject;

import java.util.ArrayList;

public interface AsyncResponse {

    void returnEvents(ArrayList<Event> output);

    void returnJSONObject(JSONObject jsonObject);
}
