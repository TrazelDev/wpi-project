package com.example.traffictracker;

import org.bson.types.ObjectId;

import io.realm.annotations.PrimaryKey;

public class UserDoc {
    @PrimaryKey
    private ObjectId _id;
    private String name;
    private PointDoc address;
    private String gender;

    public UserDoc(ObjectId _id, String name, Point address, String gender) {
        this._id = _id;
        this.name = name;
        this.address = new PointDoc(address);
        this.gender = gender;
    }

    public UserDoc() {}

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PointDoc getAddress() {
        return address;
    }

    public void setAddress(PointDoc address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
