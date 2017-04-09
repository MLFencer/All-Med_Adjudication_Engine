package com.bigmacdev.all_med.model;

import net.maritimecloud.internal.core.javax.json.Json;
import net.maritimecloud.internal.core.javax.json.JsonObject;
import net.maritimecloud.internal.core.javax.json.JsonObjectBuilder;
import org.jasypt.util.text.BasicTextEncryptor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Staff {

    String last, first, username, password, title, email, path;
    ArrayList<String> locations = new ArrayList<String>();

    public Staff(){}

    public Staff(String first, String last, String username, String password, String email){
        this.last=last;
        this.first=first;
        this.username=username;
        this.password=password;
        this.email=email;
        path = "storage/login/staff/"+username;
        new File(path).mkdirs();
    }

    public void addLocation(String path){
        locations.add(path);
    }

    public JsonObject createJson(){
        JsonObjectBuilder job = Json.createObjectBuilder();
        JsonObjectBuilder job2 = Json.createObjectBuilder();
        job.add("username", username);
        job.add("password", password);
        job.add("first", first);
        job.add("last", last);
        job.add("email", email);
        if(locations.size()>0){
            for (int i=0; i<locations.size(); i++){
                job2.add("location"+i, locations.get(i));
            }
            job.add("locations", job2);
        }
        JsonObject jo = job.build();
        return jo;
    }

    public String jsonToString(JsonObject jo){
        return jo.toString();
    }

    public String getPath(){
        return "storage/login/staff/"+username;
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
