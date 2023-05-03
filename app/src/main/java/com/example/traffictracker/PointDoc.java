package com.example.traffictracker;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

@RealmClass(embedded = true)
public class PointDoc extends RealmObject {
    private double longitude;
    private double latitude;

    public PointDoc() {}
    public PointDoc(Point p) {
        this.longitude = p._longitude;
        this.latitude = p._latitude;
    }

    public PointDoc(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
