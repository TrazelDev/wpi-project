package com.example.traffictracker;

import org.bson.types.ObjectId;

import io.realm.RealmList;
import io.realm.annotations.PrimaryKey;

public class TrafficJamDoc {
    @PrimaryKey
    private ObjectId _id;
    private double length;
    private RealmList<ObjectId> trip;
    private int count;
    private boolean is_valid;

    public TrafficJamDoc(ObjectId _id, double length, RealmList<ObjectId> trip, int count, boolean is_valid) {
        this._id = _id;
        this.length = length;
        this.trip = trip;
        this.count = count;
        this.is_valid = is_valid;
    }

    public TrafficJamDoc() {}

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public RealmList<ObjectId> getTrip() {
        return trip;
    }

    public void setTrip(RealmList<ObjectId> trip) {
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
