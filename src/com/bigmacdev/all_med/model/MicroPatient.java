package com.bigmacdev.all_med.model;

import net.maritimecloud.internal.core.javax.json.Json;
import net.maritimecloud.internal.core.javax.json.JsonObject;
import net.maritimecloud.internal.core.javax.json.JsonObjectBuilder;

import java.io.StringReader;

public class MicroPatient {
    private String first, last, path;
    private int day, month, year;

    public MicroPatient(){}

    public int getDay() {
        return day;
    }

    public String getLocation(){
        return first+"_"+last+"_"+year+"_"+month+"_"+day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String toJsonString(){
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("first", first);
        job.add("last", last);
        job.add("day", day);
        job.add("month", month);
        job.add("year", year);
        job.add("path", path);
        JsonObject jo = job.build();
        return jo.toString();
    }

    public void loadData(String s){
        JsonObject jo = Json.createReader(new StringReader(s)).readObject();
        this.first=jo.getString("first");
        this.last=jo.getString("last");
        this.path=jo.getString("path");
        this.day=jo.getInt("day");
        this.month=jo.getInt("month");
        this.year=jo.getInt("year");
    }
}
