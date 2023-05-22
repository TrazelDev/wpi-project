package com.example.traffictracker;

import org.bson.types.ObjectId;

import java.time.Duration;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;
import io.realm.mongodb.sync.SyncConfiguration;

public class TripDoc extends RealmObject {

    @PrimaryKey
    private ObjectId _id;
    private double distance;
    private long duration;
    private PointDoc start_point;
    private PointDoc end_point;
    private RealmList<PointDoc> points;
    private String trip_type;
    private ObjectId user_id;

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public PointDoc getStart_point() {
        return start_point;
    }

    public void setStart_point(PointDoc start_point) {
        this.start_point = start_point;
    }

    public PointDoc getEnd_point() {
        return end_point;
    }

    public void setEnd_point(PointDoc end_point) {
        this.end_point = end_point;
    }

    public RealmList<PointDoc> getPoints() {
        return points;
    }

    public void setPoints(RealmList<PointDoc> points) {
        this.points = points;
    }

    public String getTrip_type() {
        return trip_type;
    }

    public void setTrip_type(String trip_type) {
        this.trip_type = trip_type;
    }

    public ObjectId getUser_id() {
        return user_id;
    }

    public void setUser_id(ObjectId user_id) {
        this.user_id = user_id;
    }

    public long getDuration() { return duration; }
    public void setDuration(long duration) {
        this.duration = duration;
    }

    public TripDoc(ObjectId _id, Point start_point, String trip_type, ObjectId user_id) {
        this._id = _id;
        this.start_point = new PointDoc(start_point);
        this.trip_type = trip_type;
        this.user_id = user_id;
        this.points = new RealmList<>();
        addPoint(start_point);

    }


    public void addPoint(Point p) {
        PointDoc pd = new PointDoc(p);
        points.add(pd);
    }

    public TripDoc() {}
}


