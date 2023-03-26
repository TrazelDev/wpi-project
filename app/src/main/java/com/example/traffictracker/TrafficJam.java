package com.example.traffictracker;

import android.location.Location;

import java.sql.Time;

public class TrafficJam extends Stop {
    private Location endLocation;
    private double distance;

    public TrafficJam(Location stopLocation) {
        super(stopLocation);
    }

    public void endTrafficJam(Location endLocation) {
        this.endStop(new Time(endLocation.getTime()));
        this.endLocation = endLocation;
        double dx = endLocation.getLongitude() - this.getStopLocation().getLongitude();
        double dy = endLocation.getLatitude() - this.getStopLocation().getLatitude();
        this.distance = Math.pow((dx*dx + dy*dy), 0.5);
    }

    public Location getEndLocation() {
        return endLocation;
    }

    public double getDistance() {
        return distance;
    }
}
