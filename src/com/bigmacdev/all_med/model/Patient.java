package com.bigmacdev.all_med.model;

import net.maritimecloud.internal.core.javax.json.Json;
import net.maritimecloud.internal.core.javax.json.JsonObject;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Patient extends Person{

	ArrayList<Diagnosis> diagnosis = new ArrayList<Diagnosis>();
	ArrayList<Practice> approvedPractices = new ArrayList<Practice>();
	
	String jsonString;

	public Patient(Person p){
		super(p.getfName(),p.getlName(),p.getDobY(),p.getDobM(),p.getDobD());
	}

	public Patient(String f, String l, int y, int m, int d){
		super(f,l,y,m,d);
	}

	//Get patients password
	public String getPassword(){
		return "";
	}

	//Get patients data
	public String getData(){
		return "";
	}
	
	//Try to create patient, if fail return false, if succeed then return true;
	public boolean createPatient(){
		DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HHmmss");
		Date date = new Date();
		String dateString=dateFormat.format(date);
		createFile();
		try{
			if(!new File("data/patient/"+dobY+"/"+dobM+"/"+dobD).exists()){
				new File("data/patient/"+dobY+"/"+dobM+"/"+dobD).mkdirs();
			}
			File file= new File("data/patient/"+dobY+"/"+dobM+"/"+dobD+"/"+getName());
			file.mkdirs();
			new File("data/patient/"+dobY+"/"+dobM+"/"+dobD+"/"+getName()+"/"+dateString+".txt").createNewFile();
			JsonObject jObject = Json.createObjectBuilder()
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
			PrintStream out = new PrintStream(new FileOutputStream("data/patient/"+dobY+"/"+dobM+"/"+dobD+"/"+getName()+"/"+dateString+".txt"));
			out.print(jObject);
		}catch(Exception e){
			System.out.println(e);
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
