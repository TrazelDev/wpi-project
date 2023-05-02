package com.example.traffictracker;

import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;

import io.realm.annotations.RealmClass;


public class Trip {
    private ArrayList<Stop> _stopsList;
    private Duration _stopDuration;

    private ArrayList<TrafficJam> _jamList;
    private Duration _jamDuration;

    private ArrayList<Point> routePoints;
    private Date _startTime;
    private Date _endTime;
    private Duration _tripDuration;

    private Point _startPoint;
    private Point _endPoint;

    private double _totalDistance; // meters right now

    public enum MovingStates
    {
        NO_VEHICLE_WAY,
        VEHICLE_WAY,
        STOP,
        JAM
    }
    MovingStates _movingState;
    boolean _mainSate; // true - vehicle, false - not a vehicle

    @SuppressLint("NewApi")
    public Trip(Point startPoint, boolean state)
    {
        _stopsList = new ArrayList<Stop>();
        _stopDuration = Duration.ZERO;

        _jamList = new ArrayList<TrafficJam>();
        _jamDuration = Duration.ZERO;

        _startTime = TimeMan.getDate();
        _startPoint = startPoint;

        _mainSate = state;
        backMainState();

        _totalDistance = 0;
        routePoints = new ArrayList<Point>();
    }

    // trip stops:
    public void addStop(Point startPoint) {_stopsList.add(new Stop(startPoint));}
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateStop(Point endPoint) {
        _stopDuration = _stopDuration.plus(_stopsList.get(_stopsList.size() - 1).endStop(endPoint));
    }

    // trip traffic jams:
    public void addJam(Point startPoint) {_jamList.add(new TrafficJam(startPoint));}
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateJam(Point endPoint) {
        _jamDuration = _jamDuration.plus(_jamList.get(_jamList.size() - 1).endStop(endPoint));
    }

    @SuppressLint("NewApi")
    void endTrip(Point finalPoint)
    {
        _endTime = TimeMan.getDate();
        _tripDuration = TimeMan.DateDiff(_startTime, _endTime);
        _tripDuration = _tripDuration.minus(_stopDuration);
        _endPoint = finalPoint;
    }



    public void backMainState()
    {
        if(_mainSate)
        {
            _movingState = MovingStates.VEHICLE_WAY;
        }
        else
        {
            _movingState = MovingStates.NO_VEHICLE_WAY;
        }
    }

    public void setState(MovingStates state)
    {
        _movingState = state;
    }

    // function that returns after how many seconds does the location needs to be tracked
    // 0 - no need to track location right now
    public int getTrackerState()
    {
        switch(_movingState)
        {
            case JAM:
                return 12;
            case VEHICLE_WAY:
                return 2;
            case NO_VEHICLE_WAY:
                return 15;
            case STOP:
                return 0;
        }

        return 0;
    }

    public String addPoint(Point newPoint)
    {
        // if it is not the first point
        if(routePoints.size() != 0)
        {
            _totalDistance += routePoints.get(routePoints.size() - 1).distanceTo(newPoint);
        }
        routePoints.add(newPoint);

        String a = "point1: ";
        a += "" + routePoints.get(routePoints.size() - 1)._latitude + ", " + routePoints.get(routePoints.size() - 1)._longitude;
        a += "\npoint2: ";
        a += "" + newPoint._latitude + ", " + newPoint._longitude;
        a += "\ntotal dis: ";
        a += "" + _totalDistance;
        return a;
    }

    @SuppressLint("NewApi")
    public String returnResults()
    {
        String r = new String("total time: ");
        r += getTripDuration();
        r += "\ntraffic jam time";
        r += getDurationString(_jamDuration);
        r += "\nstop time";
        r += getDurationString(_stopDuration);
        r += "\ndistance in meters";
        r += "" + _totalDistance;
        return r;
    }

    @SuppressLint("NewApi")
    public String getTripDuration()
    {
        return getDurationString(_tripDuration);
    }

    @SuppressLint("NewApi")
    public static String getDurationString(Duration duration)
    {
        long hours = duration.toHours();
        long minutes = duration.toMinutes();
        long seconds = duration.minusMinutes(minutes).getSeconds();
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public Duration get_tripDuration() {
        return _tripDuration;
    }

    public Point get_startPoint() {
        return _startPoint;
    }

    public Point get_endPoint() {
        return _endPoint;
    }

    public double get_totalDistance() {
        return _totalDistance;
    }

    public ArrayList<TrafficJam> get_jamList() {
        return _jamList;
    }

    public Duration get_jamDuration() {
        return _jamDuration;
    }

    public ArrayList<Point> getRoutePoints() {
        return routePoints;
    }
}
