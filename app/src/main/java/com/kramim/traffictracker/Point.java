package com.kramim.traffictracker;

public class Point {
    public double _latitude; // קו רוחב
    public double _longitude; // קו אורך

    public Point(double latitude, double longitude)
    {
        this._latitude = latitude;
        this._longitude = longitude;
    }

    public boolean checkEmpty()
    {
        return _latitude == 0 && _longitude == 0;
    }

    // the function returns a value of meters
    public double distanceTo(Point other) {
        final int R = 6371; // Radius of the earth in kilometers
        double lat1 = Math.toRadians(_latitude);
        double lon1 = Math.toRadians(_longitude);
        double lat2 = Math.toRadians(other._latitude);
        double lon2 = Math.toRadians(other._longitude);
        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // Distance in meters
        return distance;
    }
}
