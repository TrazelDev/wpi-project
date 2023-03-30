package com.example.traffictracker;

import android.annotation.SuppressLint;
import android.location.Location;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Stop
{
    private Point _startPoint;
    private Date _startTime;
    private Point _endPoint;
    private Date _endTime;

    public Stop(Point startPoint){
        _startTime = TimeMan.getDate();
        _startPoint = startPoint;
    }

    @SuppressLint("NewApi")
    public Duration endStop(Point endPoint) {
        _endPoint = endPoint;
        _endTime = TimeMan.getDate();

        return TimeMan.DateDiff(_startTime, _endTime);
    }


    //public Location getStopLocation() {
    //    return stopLocation;
    //}
//
    //public Time getStopStart() {
    //    return stopStart;
    //}
//
    //public Time getStopEnd() {
    //    return stopEnd;
    //}
//
    //public Time getTotalStopTime() {
    //    return totalStopTime;
    //}
}
