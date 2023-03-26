package com.example.traffictracker;

import android.location.Location;

import java.sql.Time;

public class Stop {
    private final Location stopLocation;
    private final Time stopStart;
    private Time stopEnd;
    private Time totalStopTime;

    public Stop(Location stopLocation){
        this.stopLocation = stopLocation;
        this.stopStart = new Time(stopLocation.getTime());
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

    public Time getStopEnd() {
        return stopEnd;
    }

    public Time getTotalStopTime() {
        return totalStopTime;
    }
}
