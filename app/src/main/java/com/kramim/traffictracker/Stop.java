package com.kramim.traffictracker;

import android.annotation.SuppressLint;

import java.time.Duration;
import java.util.Date;

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
