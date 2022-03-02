package com.example.library;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DateHelper extends AsyncTask<URL, Fragment, String> {

    public String getRussianDaysOfWeek(String dayOfWeek) {
        switch (dayOfWeek) {
            case "Sunday":
                return "Воскресенье";
            case "Monday":
                return "Понедельник";
            case "Tuesday":
                return "Вторник";
            case "Wednesday":
                return "Среда";
            case "Thursday":
                return "Четверг";
            case "Friday":
                return "Пятница";
            case "Saturday":
                return "Суббота";
        }
        return null;
    }

    public String getRussianMonths(int month) {
        switch (month) {
            case 1:
                return "Январь";
            case 2:
                return "Февраль";
            case 3:
                return "Март";
            case 4:
                return "Апрель";
            case 5:
                return "Май";
            case 6:
                return "Июнь";
            case 7:
                return "Июль";
            case 8:
                return "Август";
            case 9:
                return "Сентябрь";
            case 10:
                return "Октябрь";
            case 11:
                return "Ноябрь";
            case 12:
                return "Декабрь";
        }
        return null;
    }

    protected String result = "";
    @SuppressLint("StaticFieldLeak")
    TextView DayOfDate, DayOfWeek, MonthAndYear;
    Fragment session_fragment = null;
    boolean isDataUpdated = false;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        session_fragment = MainActivity.getSelectedFragment();
        DayOfDate = session_fragment.getView().findViewById(R.id.date_day);
        DayOfWeek = session_fragment.getView().findViewById(R.id.day_of_the_week);
        MonthAndYear = session_fragment.getView().findViewById(R.id.month_year);
        if (!isDataUpdated) {
            DayOfWeek.setVisibility(View.INVISIBLE);
            DayOfDate.setVisibility(View.INVISIBLE);
            MonthAndYear.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected String doInBackground(URL... urls) {
        try {
            HttpURLConnection connection = (HttpURLConnection) urls[0].openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            connection.setRequestProperty("Content-Type", "application/json");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = bufferedReader.readLine()) != null) {
                response.append(inputLine);
            }
            bufferedReader.close();
            //Log.d(TAG,"Date: " + response.toString()); // Print result to logcat
            result = response.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String _result) {
        super.onPostExecute(_result);
        try {
            JSONObject jsonObject = new JSONObject(result);
            int day = jsonObject.getInt("day");
            int year = jsonObject.getInt("year");
            int month = jsonObject.getInt("month");
            String dayOfWeek = jsonObject.getString("dayOfWeek");
            String russianDayOfWeek = getRussianDaysOfWeek(dayOfWeek);
            String monthAndYear = getRussianMonths(month) + " " + year;
            DayOfWeek.setText(russianDayOfWeek);
            DayOfDate.setText(String.valueOf(day));
            MonthAndYear.setText(monthAndYear);
            if (!isDataUpdated) {
                DayOfWeek.setVisibility(View.VISIBLE);
                DayOfDate.setVisibility(View.VISIBLE);
                MonthAndYear.setVisibility(View.VISIBLE);
            }
            isDataUpdated = true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.d(TAG,"Date: " + json_date_result); // Print result to logcat
        //dateHelper.cancel(true);
    }
}