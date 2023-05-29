package com.kramim.traffictracker;

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
