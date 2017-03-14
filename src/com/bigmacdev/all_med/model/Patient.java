package com.bigmacdev.all_med.model;

import net.maritimecloud.internal.core.javax.json.Json;
import net.maritimecloud.internal.core.javax.json.JsonObject;

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


	public String getPassword(){return password;}
	public void setPassword(String password){this.password=password;}
	public Patient(Person p){
		super(p.getfName(),p.getlName(),p.getDobY(),p.getDobM(),p.getDobD());
	}

	public Patient(String f, String l, int y, int m, int d){
		super(f,l,y,m,d);
	}

	//Try to create patient, if fail return false, if succeed then return true;
	public boolean createFile(){
		DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HHmmss");
		Date date = new Date();
		String dateString=dateFormat.format(date);
		//createFileStructure();
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
}
