package com.example.myproject;


import com.github.tibolte.agendacalendarview.models.CalendarEvent;
import com.github.tibolte.agendacalendarview.models.DayItem;
import com.github.tibolte.agendacalendarview.models.WeekItem;

import java.util.Calendar;

public class DrawableCalendarEvent extends BasseCalendarEvent implements CalendarEvent {

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public void setId(long mId) {

    }

    @Override
    public Calendar getStartTime() {
        return null;
    }

    @Override
    public void setStartTime(Calendar mStartTime) {

    }

    @Override
    public Calendar getEndTime() {
        return null;
    }

    @Override
    public void setEndTime(Calendar mEndTime) {

    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public void setTitle(String mTitle) {

    }

    @Override
    public Calendar getInstanceDay() {
        return null;
    }

    @Override
    public void setInstanceDay(Calendar mInstanceDay) {

    }

    @Override
    public DayItem getDayReference() {
        return null;
    }

    @Override
    public void setDayReference(DayItem mDayReference) {

    }

    @Override
    public WeekItem getWeekReference() {
        return null;
    }

    @Override
    public void setWeekReference(WeekItem mWeekReference) {

    }

    @Override
    public CalendarEvent copy() {
        return null;
    }
}
