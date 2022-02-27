package com.example.library;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
    String json_date_result = "";

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

    class DateHelper extends AsyncTask<URL, Fragment, String> {

        protected String result = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            DayOfWeek.setVisibility(View.INVISIBLE);
            DayOfDate.setVisibility(View.INVISIBLE);
            MonthAndYear.setVisibility(View.INVISIBLE);
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
            //Code.setText(result);
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
                DayOfWeek.setVisibility(View.VISIBLE);
                DayOfDate.setVisibility(View.VISIBLE);
                MonthAndYear.setVisibility(View.VISIBLE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Log.d(TAG,"Date: " + json_date_result); // Print result to logcat
            dateHelper.cancel(true);
        }
    }

}