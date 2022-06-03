package com.example.library.fragment.events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.library.R;
import com.example.library.helper.DateHelper;

import java.util.Calendar;

public class EventsFragment extends Fragment {

    private CalendarView calendarView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_events, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViews();
        setOnClickListeners();
        setCalendarView();
    }

    private void setOnClickListeners() {
    }

    private void setViews() {
        View v = requireView();
        calendarView = v.findViewById(R.id.calendarView);
    }

    private void setCalendarView(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, DateHelper.getYear());
        calendar.set(Calendar.MONTH, DateHelper.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, DateHelper.getDay());
        long milliTime = calendar.getTimeInMillis();
        calendar.set(Calendar.YEAR, DateHelper.getYear()-1);
        long minTime = calendar.getTimeInMillis();
        calendar.set(Calendar.YEAR, DateHelper.getYear()+1);
        long maxTime = calendar.getTimeInMillis();
        calendarView.setDate(milliTime,true,true);
        calendarView.setMinDate(minTime);
        calendarView.setMaxDate(maxTime);
    }
}
