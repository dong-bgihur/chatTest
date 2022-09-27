package com.example.myapp110_2_2;

import java.sql.Timestamp;
import java.util.Objects;

public class event {
    String title;
    Timestamp startTime;
    Timestamp endTime;
    String location;
    String description;




    public event(String title, Timestamp startTime, Timestamp endTime, String location, String description) {
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.description = description;
    }

    public event(String title, Timestamp startTime, Timestamp endTime, String location) {
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
    }

    public String getTitle(){
        return title;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        event event = (event) o;
        return Objects.equals(title, event.title) && Objects.equals(startTime, event.startTime) && Objects.equals(endTime, event.endTime) && Objects.equals(location, event.location) && Objects.equals(description, event.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, startTime, endTime, location, description);
    }

}
