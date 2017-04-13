package com.bigmacdev.all_med.model;

import net.maritimecloud.internal.core.javax.json.Json;
import net.maritimecloud.internal.core.javax.json.JsonObject;
import net.maritimecloud.internal.core.javax.json.JsonObjectBuilder;
import net.maritimecloud.internal.core.javax.json.JsonReader;
import org.jasypt.util.text.BasicTextEncryptor;
import org.jasypt.util.text.TextEncryptor;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Staff {

    private String last, first, username, password, title, email, path;
    private ArrayList<String> pharmacies = new ArrayList<String>();
    private ArrayList<String> clinics = new ArrayList<String>();

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

    public void addLocation(String path, int type){
        if(type==1){
            pharmacies.add(path);
        } else if (type==2){
            clinics.add(path);
        }
    }

    public boolean hasPharm(){
        return pharmacies.size()>0;
    }

    public boolean hasClinics(){
        return clinics.size()>0;
    }

    public void loadData(String loc){
        JsonObject jo = getData(loc);
        this.username=jo.getString("username");
        this.password=jo.getString("password");
        this.email=jo.getString("email");
        this.first=jo.getString("first");
        this.last=jo.getString("last");
        if (jo.containsKey("clinics")){
            JsonObject cl = jo.getJsonObject("clinics");
            int i = 0;
            while(true){
                if(cl.containsKey("location"+i)){
                    clinics.add(cl.getString("location"+i));
                }else{
                    break;
                }
                i++;
            }
        }
        if (jo.containsKey("pharmacies")){
            JsonObject pharm = jo.getJsonObject("pharmacies");
            int i = 0;
            while(true){
                if(pharm.containsKey("location"+i)){
                    pharmacies.add(pharm.getString("location"+i));
                }else{
                    break;
                }
                i++;
            }
        }
    }

    public JsonObject getData(String loc){
        loc=loc+"/"+getLatestRecord(loc);
        File inputFile = new File(loc);
        InputStream inputStream;
        JsonObject out=null;
        try{
            inputStream = new FileInputStream(inputFile);
            JsonReader reader = Json.createReader(inputStream);
            out = reader.readObject();
            reader.close();
            inputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return out;
    }

    public String getLatestRecord(String location){
        File folder = new File(location);
        File [] listOfFiles = folder.listFiles();
        String latest=listOfFiles[0].getName();
        for (int i=1; i<listOfFiles.length; i++){
            String nameCurrent = listOfFiles[i].getName();
            int latestDay = Integer.parseInt(latest.substring(0,8));
            int currentDay = Integer.parseInt(nameCurrent.substring(0,8));
            int latestTime = Integer.parseInt(latest.substring(10,13));
            int currentTime = Integer.parseInt(nameCurrent.substring(10,13));
            if(latestDay>currentDay){
            } else if(latestDay<currentDay){
                latest=nameCurrent;
            }else if (latestDay==currentDay){
                if(latestTime>currentTime){
                }else{
                    latest=nameCurrent;
                }
            }
        }
        return latest;
    }

    public JsonObject createJson(){
        JsonObjectBuilder job = Json.createObjectBuilder();
        JsonObjectBuilder job2 = Json.createObjectBuilder();
        JsonObjectBuilder job3 = Json.createObjectBuilder();
        job.add("username", username);
        job.add("password", password);
        job.add("first", first);
        job.add("last", last);
        job.add("email", email);
        if(pharmacies.size()>0){
            for (int i=0; i<pharmacies.size(); i++){
                job2.add("location"+i, pharmacies.get(i));
            }
            job.add("pharmacies", job2);
        }
        if(clinics.size()>0){
            for (int i=0; i<clinics.size(); i++){
                job3.add("location"+i, clinics.get(i));
            }
            job.add("clinics", job3);
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
