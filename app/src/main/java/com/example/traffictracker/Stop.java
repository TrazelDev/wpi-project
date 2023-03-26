package com.example.traffictracker;

import android.location.Location;

import java.sql.Time;

public class Stop {
    private final Location stopLocation;
    private Time stopStart;
    private Time stopEnd;
    private Time totalStopTime;

    public Stop(Location stopLocation, Time stopStart){
        this.stopLocation = stopLocation;
        this.stopStart = stopStart;
    }

    public void endStop(Time stopEnd) {
        this.stopEnd = stopEnd;
        this.totalStopTime = new Time(this.stopEnd.getTime() - this.stopStart.getTime());
    }

    public Location getStopLocation() {
        return stopLocation;
    }

    public Time getStopStart() {
        return stopStart;
    }

    public void setStopStart(Time stopStart) {
        this.stopStart = stopStart;
    }

    public Time getStopEnd() {
        return stopEnd;
    }

    public void setStopEnd(Time stopEnd) {
        this.stopEnd = stopEnd;
    }

    public Time getTotalStopTime() {
        return totalStopTime;
    }
}
