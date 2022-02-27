package com.example.library;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HomeFragment extends Fragment {
    DateHelper dateHelper = null;
    TextView DayOfDate, DayOfWeek, MonthAndYear;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Находим текстовые поля среди родительского элемента
        DayOfDate = getView().findViewById(R.id.date_day);
        DayOfWeek = getView().findViewById(R.id.day_of_the_week);
        MonthAndYear = getView().findViewById(R.id.month_year);
        //Code = getView().findViewById(R.id.code);
        try {
            String date_url = "https://www.timeapi.io/api/Time/current/zone?timeZone=Europe/Moscow";
            URL url = new URL(date_url);
            dateHelper = new DateHelper();
            dateHelper.execute(url);
            //Toast.makeText(getActivity(),"date is downloaded",Toast.LENGTH_SHORT).show();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }





}

