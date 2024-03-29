package com.example.library.helper;

import static com.example.library.helper.NetworkHelper.getJSONFromURL;

import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.library.R;

import org.json.JSONObject;

import java.net.URL;
import java.util.Calendar;

public class DateHelper extends AsyncTask<URL, Void, String> {

    private static final String TAG = "DateHelper";
    private static int day;
    private static int month;
    private static int year;
    private static int dayOfWeek;
    @NonNull
    protected String result = "";
    private TextView DayOfDate, DayOfWeek, MonthAndYear;
    private boolean isDataUpdated = false;

    @NonNull
    public static String getMonths(int month) {
        switch (month) {
            case 0:
                return "Январь";
            case 1:
                return "Февраль";
            case 2:
                return "Март";
            case 3:
                return "Апрель";
            case 4:
                return "Май";
            case 5:
                return "Июнь";
            case 6:
                return "Июль";
            case 7:
                return "Август";
            case 8:
                return "Сентябрь";
            case 9:
                return "Октябрь";
            case 10:
                return "Ноябрь";
            case 11:
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
        return "";
    }

    public static int getDay() {
        return day;
    }

    public static int getMonth() {
        return month;
    }

    public static int getYear() {
        return year;
    }

    public String getDaysOfWeek(int dayOfWeek) {
        switch (dayOfWeek) {
            case 1:
                return "Воскресенье";
            case 2:
                return "Понедельник";
            case 3:
                return "Вторник";
            case 4:
                return "Среда";
            case 5:
                return "Четверг";
            case 6:
                return "Пятница";
            case 7:
                return "Суббота";
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Fragment session_fragment = FragmentHelper.getSelectedTopFragment();
        try{
            View view = session_fragment.requireView();
            DayOfDate = view.findViewById(R.id.date_day);
            DayOfWeek = view.findViewById(R.id.day_of_the_week);
            MonthAndYear = view.findViewById(R.id.month_year);
            if (!isDataUpdated) {
                DayOfWeek.setVisibility(View.INVISIBLE);
                DayOfDate.setVisibility(View.INVISIBLE);
                MonthAndYear.setVisibility(View.INVISIBLE);
            }
        }
        catch (Exception ex){
//            ex.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(URL... urls) {
        result = getJSONFromURL(urls[0]);
        return result;
    }

    public static String getSQLDate(short day, short month, short year) {
        String dayStr, monthStr, yearStr;
        if (day < 10) {
            dayStr = "0" + day;
        } else {
            dayStr = String.valueOf(day);
        }
        if (month < 10) {
            monthStr = "0" + month;
        } else {
            monthStr = String.valueOf(month);
        }
        if (year < 10) {
            yearStr = "000" + year;
        } else if (year < 100) {
            yearStr = "00" + year;
        } else if (year < 1000) {
            yearStr = "0" + year;
        } else {
            yearStr = String.valueOf(year);
        }
//        Log.d(TAG, "SQLDATE: " + yearStr + "-" + monthStr + "-" + dayStr);
        return yearStr + "-" + monthStr + "-" + dayStr;
    }


    @Override
    protected void onPostExecute(@Nullable String _result) {
        super.onPostExecute(_result);
        try {
            JSONObject jsonObject = new JSONObject(result);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(jsonObject.getLong("time"));

            day = calendar.get(Calendar.DAY_OF_MONTH);
            month = calendar.get(Calendar.MONTH);
            year = calendar.get(Calendar.YEAR);
            dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            String russianDayOfWeek = getDaysOfWeek(dayOfWeek);
            String monthAndYear = getMonths(month) + " " + year;

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