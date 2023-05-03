package com.example.traffictracker;


import io.realm.RealmList;
import io.realm.annotations.PrimaryKey;

public class TrafficJamDoc {
    @PrimaryKey
    private String _id;
    private double length;
    private RealmList<String> trip;
    private int count;
    private boolean is_valid;

    public TrafficJamDoc(String _id, double length, RealmList<String> trip, int count, boolean is_valid) {
        this._id = _id;
        this.length = length;
        this.trip = trip;
        this.count = count;
        this.is_valid = is_valid;
    }

    public TrafficJamDoc() {}

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public RealmList<String> getTrip() {
        return trip;
    }

    public void setTrip(RealmList<String> trip) {
        this.trip = trip;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isIs_valid() {
        return is_valid;
    }

    public void setIs_valid(boolean is_valid) {
        this.is_valid = is_valid;
    }
}
