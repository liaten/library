package com.example.library.helper;

import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.library.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DateHelper extends AsyncTask<URL, Void, String> {

    private static short day, month, year;
    @NonNull
    protected String result = "";
    private TextView DayOfDate, DayOfWeek, MonthAndYear;
    private boolean isDataUpdated = false;

    public static String getSQLDate(short day, short month, short year) {
        String dayStr, monthStr, yearStr;
        if (day < 10) {
            dayStr = "0" + String.valueOf(day);
        } else {
            dayStr = String.valueOf(day);
        }
        if (month < 10) {
            monthStr = "0" + String.valueOf(month);
        } else {
            monthStr = String.valueOf(month);
        }
        if (year < 10) {
            yearStr = "000" + String.valueOf(year);
        } else if (year < 100) {
            yearStr = "00" + String.valueOf(year);
        } else if (year < 1000) {
            yearStr = "0" + String.valueOf(year);
        } else {
            yearStr = String.valueOf(year);
        }
        return yearStr + "-" + monthStr + "-" + dayStr;
    }

    @NonNull
    public static String getRussianMonths(short month) {
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
            default:
                return "ОШИБКА";
        }
    }

    public static String getRussianMonthsGenitive(short month) {
        switch (month) {
            case 1:
                return "января";
            case 2:
                return "февраля";
            case 3:
                return "марта";
            case 4:
                return "апреля";
            case 5:
                return "мая";
            case 6:
                return "июня";
            case 7:
                return "июля";
            case 8:
                return "августа";
            case 9:
                return "сентября";
            case 10:
                return "октября";
            case 11:
                return "ноября";
            case 12:
                return "декабря";
        }
        return null;
    }

    public static short getDay() {
        return day;
    }

    public static short getMonth() {
        return month;
    }

    public static short getYear() {
        return year;
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

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Fragment session_fragment = FragmentHelper.getSelectedFragment();
        DayOfDate = session_fragment.requireView().findViewById(R.id.date_day);
        DayOfWeek = session_fragment.requireView().findViewById(R.id.day_of_the_week);
        MonthAndYear = session_fragment.requireView().findViewById(R.id.month_year);
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
            day = (short) jsonObject.getInt("day");
            year = (short) jsonObject.getInt("year");
            month = (short) jsonObject.getInt("month");
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
                isDataUpdated = true;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}