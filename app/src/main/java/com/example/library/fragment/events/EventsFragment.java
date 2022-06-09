package com.example.library.fragment.events;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.library.R;
import com.example.library.entity.Book;
import com.example.library.entity.Event;
import com.example.library.helper.AsyncResponse;
import com.example.library.helper.BookHelper;
import com.example.library.helper.DateHelper;
import com.example.library.helper.EventHelper;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

public class EventsFragment extends Fragment implements AsyncResponse {

    private CalendarView calendarView;
    private ProgressBar loading;
    private RecyclerView recycler;
    private TextView alert, textViewShowCalendar;
    private static final String TAG = "EventsFragment";
    private int activeDay, activeMonth, activeYear;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_events, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActiveDate();
        setViews();
        setCalendarView();
        setOnClickListeners();
        getEvents();
    }

    private void setOnClickListeners() {
        textViewShowCalendar.setOnClickListener(textViewShowCalendarClickListener);
    }

    private View.OnClickListener textViewShowCalendarClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String text = textViewShowCalendar.getText().toString();
            if(text.equals(getResources().getString(R.string.calendar_shown))){
                calendarView.setVisibility(View.GONE);
                textViewShowCalendar.setText(getResources().getString(R.string.calendar_hidden));
            }
            else {
                calendarView.setVisibility(View.VISIBLE);
                textViewShowCalendar.setText(getResources().getString(R.string.calendar_shown));
            }
        }
    };

    private void setViews() {
        View v = requireView();
        calendarView = v.findViewById(R.id.calendarView);
        recycler = v.findViewById(R.id.recycler);
        loading = v.findViewById(R.id.loading);
        alert = v.findViewById(R.id.alert);
        textViewShowCalendar = v.findViewById(R.id.text_view_show_calendar);
    }

    private void getActiveDate(){
        activeYear = DateHelper.getYear();
        activeMonth = DateHelper.getMonth();
        activeDay = DateHelper.getDay();
    }

    private void setCalendarView(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, activeYear);
        calendar.set(Calendar.MONTH, activeMonth);
        calendar.set(Calendar.DAY_OF_MONTH, activeDay);
        long milliTime = calendar.getTimeInMillis();
        calendar.set(Calendar.YEAR, activeYear-1);
        long minTime = calendar.getTimeInMillis();
        calendar.set(Calendar.YEAR, activeYear+1);
        long maxTime = calendar.getTimeInMillis();
        calendarView.setDate(milliTime,true,true);
        calendarView.setMinDate(minTime);
        calendarView.setMaxDate(maxTime);
        calendarView.setOnDateChangeListener((calendarView, year, month, dayOfMonth) -> {
            // i = год i1 = месяц(5 - май, 0 - январь) i2 = день
            Log.d(TAG, String.format("onSelectedDayChange: %d/%d/%d",dayOfMonth,month,year));
        });
    }

    private void getEvents(){
        String link = "https://liaten.ru/db/events.php?page=1&recsPerPage=12";
        try {
            new EventHelper((AsyncResponse) this).execute(new URL(link));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processFinish(Boolean output) {

    }

    @Override
    public void returnBooks(ArrayList<Book> output) {

    }

    @Override
    public void returnEvents(ArrayList<Event> output) {
        Log.d(TAG, "returnEvents: " + output);
    }

    @Override
    public void returnBooks(ArrayList<Book> output, String table) {

    }

    @Override
    public void returnTable(String table) {

    }

    @Override
    public void processFinish(Bitmap output) {

    }

    @Override
    public void processFinish(Bitmap output, String table) {

    }

    @Override
    public void returnJSONObject(JSONObject jsonObject) {

    }
}
