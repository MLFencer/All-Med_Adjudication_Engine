package com.bigmacdev.all_med.model;

import net.maritimecloud.internal.core.javax.json.Json;
import net.maritimecloud.internal.core.javax.json.JsonObject;
import net.maritimecloud.internal.core.javax.json.JsonObjectBuilder;
import net.maritimecloud.internal.core.javax.json.JsonReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by dev on 3/25/2017.
 */
public class Login {

    private String username, password, location;
    private ArrayList<Relationship> familyAccess = new ArrayList<>();

    public Login(){
        username="";
        password="";
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setLogin(String username1, String password1){
        username=username1;
        password=password1;
    }

    public void addRelationship(Relationship r){
        familyAccess.add(r);
    }

    public ArrayList<Relationship> getRelationships(){
        return familyAccess;
    }

    public void setUsername(String username1){
        this.username = username1;
    }

    public void setPassword(String password1){
        this.password = password1;
    }

    public String getPassword(){
        return password;
    }

    public String getUsername(){
        return username;
    }

    public String toJson(){
        JsonObjectBuilder job = Json.createObjectBuilder();
        JsonObjectBuilder job2 = Json.createObjectBuilder();
        try {
            if (familyAccess.size() >= 1) {
                for (int i = 0; i < familyAccess.size(); i++) {
                    job2.add(String.valueOf(i), Json.createObjectBuilder()
                            .add("first", familyAccess.get(i).getFname())
                            .add("last", familyAccess.get(i).getLname())
                            .add("location", familyAccess.get(i).getLocation())
                            .add("relationship", familyAccess.get(i).getRelationship()));
                }
            }
        }catch (Exception e){
            System.out.println("No Relationships.");
        }
        job.add("username", username)
                .add("password", password);
        try{
            job.add("location", location);
        }catch(Exception e){}
        try {
            if (familyAccess.size() >= 1) {
                job.add("relationships", job2);
            }
        }catch (Exception e){}

        JsonObject jo = job.build();
        return jo.toString();
    }

    public void loadLoginData(JsonObject jo){
        this.password=jo.getString("password");
        this.username=jo.getString("username");
        try {
            this.location = jo.getString("location");
        } catch (Exception e){}
        try{
            JsonObject jo2 = jo.getJsonObject("relationships");
            for (int i=0; i<jo2.size(); i++){
                this.familyAccess.add(new Relationship(jo2.getString("location"), jo2.getString("first"), jo2.getString("last"), jo2.getString("relationship")));
            }
        }catch (Exception e){
            System.out.println("No Relationships.");
        }
    }

    public JsonObject readLoginData(String address){
        File inputFile = new File(address);
        InputStream inputStream;
        JsonObject jo=null;
        try{
            inputStream = new FileInputStream(inputFile);
            JsonReader reader = Json.createReader(inputStream);
            jo = reader.readObject();
            reader.close();
            inputStream.close();
            return jo;

        }catch (Exception e) {
            System.out.println(e);
            return jo;
        }
    }
}
