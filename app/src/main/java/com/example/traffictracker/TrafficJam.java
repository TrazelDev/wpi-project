package com.example.traffictracker;

import android.location.Location;

import java.sql.Time;

public class TrafficJam extends Stop
{
    private double distance;
    // private
    public TrafficJam(Point startPoint)
    {
        super(startPoint);
    }


    public double getDistance() {
        return distance;
    }
}
