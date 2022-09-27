package com.example.myproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.myproject.databinding.ActivityMainBinding;
import com.github.tibolte.agendacalendarview.CalendarPickerController;
import com.github.tibolte.agendacalendarview.models.BaseCalendarEvent;
import com.github.tibolte.agendacalendarview.models.CalendarEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    ArrayList<CalendarEvent> eventList = new ArrayList<>();  // for event

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Calendar minDate = Calendar.getInstance();
        Calendar maxDate = Calendar.getInstance();

        minDate.add(Calendar.MONTH, -2);
        minDate.set(Calendar.DAY_OF_MONTH, 1);
        maxDate.add(Calendar.YEAR, 1);

        mockList(eventList);

        binding.magendaCalendarView.init(eventList, minDate, maxDate, Locale.getDefault(), (CalendarPickerController) this);
        binding.magendaCalendarView.addEventRenderer(new DrawableEventRenderer());


    }

    private void mockList(List<CalendarEvent> eventList) {
        Calendar startTime1 = Calendar.getInstance();
        Calendar endTime1 = Calendar.getInstance();
        endTime1.add(Calendar.DAY_OF_YEAR, 5);
        BaseCalendarEvent event1 = new BaseCalendarEvent("出遊", "nice!", "花蓮",
                ContextCompat.getColor(this, R.color.teal_200), startTime1, endTime1, true);
        eventList.add(event1);
    }
}