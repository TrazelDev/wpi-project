package com.kramim.traffictracker;

import org.bson.types.ObjectId;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class UserDoc extends RealmObject {
    @PrimaryKey
    private ObjectId _id;
    private String school;
    private String neighborhood;
    private String gender;

    public UserDoc(ObjectId _id, String school, String neighborhood, String gender) {
        this._id = _id;
        this.school = school;
        this.neighborhood = neighborhood;
        this.gender = gender;
    }

    public UserDoc() {}

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
