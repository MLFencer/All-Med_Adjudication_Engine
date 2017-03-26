package com.bigmacdev.all_med.model;

public class Relationship {

    private String location, fname, lname, relatrionship;

    public Relationship(String loc, String f, String l, String r){
        this.location=loc;
        this.fname=f;
        this.lname=l;
        this.relatrionship=r;
    }

    public void setRelationship(String relatrionship) {
        this.relatrionship = relatrionship;
    }

    public String getRelationship() {
        return relatrionship;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getLocation() {
        return location;
    }
}
