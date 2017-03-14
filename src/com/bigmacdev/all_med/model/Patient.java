package com.bigmacdev.all_med.model;

import net.maritimecloud.internal.core.javax.json.Json;
import net.maritimecloud.internal.core.javax.json.JsonObject;
import net.maritimecloud.internal.core.javax.json.JsonReader;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Patient extends Person implements Serializable{

	private static final long serialVersionUID = 2L;

	ArrayList<Diagnosis> diagnosis = new ArrayList<Diagnosis>();
	ArrayList<Practice> approvedPractices = new ArrayList<Practice>();
	private String jsonString;
	private String password="";


	//-----Constructors---------
	public Patient(Person p){
		super(p.getfName(),p.getlName(),p.getDobY(),p.getDobM(),p.getDobD());
	}

	public Patient(String f, String l, int y, int m, int d){
		super(f,l,y,m,d);
	}

	public Patient(){}

	//--------------------------


	//Password Operations----------
	public String getPassword(){return password;}
	public void setPassword(String password){this.password=password;}
	//-----------------------------



	//----Create Patient Record----------------
	public boolean createFile(){
		DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HHmmss");
		Date date = new Date();
		String dateString=dateFormat.format(date);
		try{
			if(!new File(getFilePath()).exists()){
				new File(getFilePath()).mkdirs();
			}
			new File(getFilePath()+"/"+dateString+".txt").createNewFile();
			JsonObject jObject = Json.createObjectBuilder()
					.add("password",password)
					.add("personal_info", Json.createObjectBuilder()
						.add("name", Json.createObjectBuilder()
							.add("first", fName)
							//.add("middle", mName)
							.add("last", lName)
						)
						.add("dob", Json.createObjectBuilder()
							.add("day",dobD)
							.add("month",dobM)
							.add("year", dobY)
						)
					).build();
			PrintStream out = new PrintStream(new FileOutputStream(getFilePath()+"/"+dateString+".txt"));
			out.print(jObject);
		}catch(Exception e){
			System.out.println(e);
			e.printStackTrace();
			return false;
		}
		return true;
	}
	//-------------------------------

	//----Get Data------------------
	public void getPatientData(String location){
		location=location+"/"+getLatestRecord(location);
		File inputFile = new File(location);
		InputStream inputStream;
		try{
			inputStream = new FileInputStream(inputFile);
			JsonReader reader = Json.createReader(inputStream);
			JsonObject jo = reader.readObject();
			reader.close();
			password=jo.getString("password");
			JsonObject personalInfo = jo.getJsonObject("personal_info");
			JsonObject name = personalInfo.getJsonObject("name");
			fName=name.getString("first");
			lName=name.getString("last");
			JsonObject dob = personalInfo.getJsonObject("dob");
			dobD=dob.getInt("day");
			dobM=dob.getInt("month");
			dobY=dob.getInt("year");
		}catch (Exception e){
			System.out.println(e);
		}
	}

	//----Get Most Recent Patient Record------------------
	public String getLatestRecord(String location){
		File folder = new File(location);
		File [] listOfFiles = folder.listFiles();
		String latest=listOfFiles[0].getName();
		for (int i=1; i<listOfFiles.length; i++){
			String nameCurrent = listOfFiles[i].getName();
			int latestDay = Integer.parseInt(latest.substring(0,8));
			int currentDay = Integer.parseInt(nameCurrent.substring(0,8));
			int latestTime = Integer.parseInt(latest.substring(10,15));
			int currentTime = Integer.parseInt(nameCurrent.substring(10,15));
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
}
