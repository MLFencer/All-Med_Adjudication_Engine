package com.bigmacdev.all_med.model;

import net.maritimecloud.internal.core.javax.json.Json;
import net.maritimecloud.internal.core.javax.json.JsonObject;
import net.maritimecloud.internal.core.javax.json.JsonObjectBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Pharmacy {

    private String name, street, city, state, zip, phone, path, manager;
    private ArrayList<String> staff = new ArrayList<String>();

    public Pharmacy(String name, String street, String city, String state, String zip, String phone, String manager){
        this.name=name;
        this.street=street;
        this.city=city;
        this.state=state;
        this.zip=zip;
        this.phone=phone;
        this.manager=manager;
        DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HHmm");
        Date date = new Date();
        this.path = "storage/data/pharmacy/"+name+"_"+manager+"_"+dateFormat.format(date);
        new File(path+"/records").mkdirs();
        new File(path+"/scripts/archive").mkdirs();
    }

    public Pharmacy(){}

    public void addStaff(String username){
        staff.add(username);
    }

    public String getPath(){
        return path;
    }

    public JsonObject createJson(){
        JsonObjectBuilder job = Json.createObjectBuilder();
        JsonObjectBuilder job2 = Json.createObjectBuilder();
        JsonObjectBuilder job3 = Json.createObjectBuilder();
        job.add("name", name);
        job.add("phone", phone);
        job.add("manager", manager);
        job3.add("street", street);
        job3.add("city", city);
        job3.add("state", state);
        job3.add("zip", zip);
        job.add("location", job3);
        if(staff.size()>0){
            for (int i=0; i<staff.size(); i++){
                job2.add("staff"+i, staff.get(i));
            }
            job.add("staff", job2);
        }
        JsonObject jo = job.build();
        return jo;
    }

    public String jsonToString(JsonObject jo){
        return jo.toString();
    }

    public boolean createFile(){
        DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HHmmss");
        Date date = new Date();
        try{
            new File(getPath()+"/"+dateFormat.format(date)+".txt").createNewFile();
            PrintStream out = new PrintStream(new FileOutputStream(getPath()+"/"+dateFormat.format(date)+".txt"));
            out.print(jsonToString(createJson()));
            out.close();
            return true;
        } catch (Exception e){
            System.out.println(e);
            return false;
        }
    }
}
