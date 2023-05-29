package com.kramim.traffictracker;

import java.util.ArrayList;

import io.realm.RealmList;
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

    public static RealmList<PointDoc> toRealmList(ArrayList<Point> p) {
        RealmList<PointDoc> result = new RealmList<>();
        for(int i=0; i<p.size(); i++) {
            result.add(new PointDoc(p.get(i)));
        }
        return result;
    }
}
