package com.bigmacdev.all_med.model;

import net.maritimecloud.internal.core.javax.json.Json;
import net.maritimecloud.internal.core.javax.json.JsonObject;
import net.maritimecloud.internal.core.javax.json.JsonObjectBuilder;
import net.maritimecloud.internal.core.javax.json.JsonReader;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Patient extends Person implements Serializable{

	private static final long serialVersionUID = 2L;

	private ArrayList<Diagnosis> diagnosis = new ArrayList<Diagnosis>();
	private ArrayList<Practice> approvedPractices = new ArrayList<Practice>();
	private ArrayList<String> changes = new ArrayList<String>();
	private String path;
	private String password="";
	private String gender="";
	private String username="";
	private String homeNumber="";
	private String cellNumber="";
	private String address="";
	private String city="";
	private String state="";
	private String zip="";
	private String ssn="";
	private String email="";
	private String emergencyContact="";
	private String emergencyContactRelationship="";
	private String eCHomeNumber="";
	private String eCCellNumber="";
	private String changedBy="";


	//-----Constructors---------
	public Patient(Person p){
		super(p.getfName(),p.getlName(),p.getDobY(),p.getDobM(),p.getDobD());
	}

	public Patient(String f, String l, int y, int m, int d){
		super(f,l,y,m,d);
	}

	public Patient(){}

	//--------------------------


	//------Getters-----------
	public String getUsername(){return username;}
	public String getGender(){return gender;}
	public String getPassword(){return password;}
	public String getHomeNumber(){return homeNumber;}
	public String getCellNumber(){return cellNumber;}
	public String getAddress() {return address;}
	public String getCity(){return city.substring(0,0).toLowerCase()+city.substring(1,city.length());}
	public String getState(){return state;}
	public String getZip() {return zip;}
	public String getSsn() {return ssn.substring(ssn.length()-4,ssn.length());}
	public String getEmail(){return email;}
	public String getEmergencyContact(){return emergencyContact;}
	public String getEmergencyContactRelationship(){return emergencyContactRelationship;}
	public String geteCHomeNumber(){return eCHomeNumber;}
	public String geteCCellNumber(){return eCHomeNumber;}
	//------------------------------

	//------Setters--------------
	public void setUsername(String s){username=s;}
	public void setGender(String s){gender=s;}
	public void setPassword(String password){this.password=password;}
	public void setHomeNumber(String s){homeNumber=s;}
	public void setCellNumber(String s){cellNumber=s;}
	public void setAddress(String s){address=s;}
	public void setCity(String s){city=s;}
	public void setState(String s){state=s;}
	public void setZip(String s){zip=s;}
	public void setSsn(String s){ssn=s;}
	public void setEmail(String s){email=s;}
	public void setEmergencyContact(String s){emergencyContact=s;}
	public void setEmergencyContactRelationship(String s){emergencyContactRelationship=s;}
	public void seteCHomeNumber(String s){eCHomeNumber=s;}
	public void seteCCellNumber(String s){eCCellNumber=s;}
	//----------------------------------



	//----Create Patient Record----------------
	public boolean createFile(){
		DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HHmmss");
		Date date = new Date();
		path=getFilePath();
		String dateString=dateFormat.format(date);
		try{
			if(!new File(getFilePath()).exists()){
				new File(getFilePath()).mkdirs();
			}
			new File(getFilePath()+"/"+dateString+".txt").createNewFile();
			PrintStream out = new PrintStream(new FileOutputStream(getFilePath()+"/"+dateString+".txt"));
			out.print(toJsonString());
			out.close();
		}catch(Exception e){
			System.out.println(e);
			e.printStackTrace();
			return false;
		}
		return true;
	}
	//-------------------------------

	//----Make another patient record--------
	public boolean createAdditionalFile(){
		DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HHmmss");
		Date date = new Date();
		path=getFilePath();
		String dateString=dateFormat.format(date);
		try{
			if(!new File(getFilePath()).exists()){
				new File(getFilePath()).mkdirs();
			}
			new File(getFilePath()+"/"+dateString+".txt").createNewFile();
			PrintStream out = new PrintStream(new FileOutputStream(getFilePath()+"/"+dateString+".txt"));
			out.print(toJsonString());
			out.close();
		}catch(Exception e){
			System.out.println(e);
			e.printStackTrace();
			return false;
		}
		return true;
	}
	//-----------------------------------------

	//Get File Path
	public String getFilePath(){
		return "storage/data/patient/"+dobY+"/"+dobM+"/"+dobD+"/"+ssn.substring(ssn.length()-4,ssn.length())+"/"+getName();
	}

	//--------Write Json Data to file---------
	public boolean writeJsonToFile(JsonObject jo){
		DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HHmmss");
		Date date = new Date();
		String dateString=dateFormat.format(date);
		try {
			new File(path + "/" +dateString+".txt").createNewFile();
			PrintStream out = new PrintStream(new FileOutputStream(getFilePath()+"/"+dateString+".txt"));
			out.print(jo);
			out.close();
			return true;
		}catch (Exception e){
			return false;
		}
	}
	//----------------------------------------

	//----Get Data As Json------------------
	public JsonObject getPatientData(String location){
		location=location+"/"+getLatestRecord(location);
		File inputFile = new File(location);
		InputStream inputStream;
		JsonObject jo=null;
		try{
			inputStream = new FileInputStream(inputFile);
			JsonReader reader = Json.createReader(inputStream);
			jo = reader.readObject();
			reader.close();
			inputStream.close();
			return jo;

		}catch (Exception e){
			System.out.println(e);
			return jo;

		}
	}
	public String toJsonString(){
		return toJson().toString();
	}


	//-------Create Json------
	public JsonObject toJson(){
		JsonObjectBuilder job = Json.createObjectBuilder();
		JsonObjectBuilder job2 = Json.createObjectBuilder();
		JsonObjectBuilder job3 = Json.createObjectBuilder();
		job.add("username",username)
				.add("password",password)
				.add("path", path)
				.add("personal_info", Json.createObjectBuilder()
						.add("name", Json.createObjectBuilder()
								.add("first", fName)
								.add("middle", mName)
								.add("last", lName)
						)
						.add("dob", Json.createObjectBuilder()
								.add("day",dobD)
								.add("month",dobM)
								.add("year", dobY)
						)

				)
				.add("ssn",ssn)
				.add("contact", Json.createObjectBuilder()
						.add("home",homeNumber)
						.add("cell",cellNumber)
						.add("email",email)
						.add("address",address)
						.add("city",city)
						.add("state",state)
						.add("zip",zip)
				)
				.add("emergency", Json.createObjectBuilder()
						.add("contact",emergencyContact)
						.add("relationship",emergencyContactRelationship)
						.add("home",eCHomeNumber)
						.add("cell",eCCellNumber)
				);
		try {
			if (!changedBy.equals("")) {
				job2.add("updatedBy", changedBy);
				for (int i = 0; i < changes.size(); i++) {
					job3.add("field" + i, changes.get(i));
				}
				job2.add("fieldsChanged", job3);
				job.add("changes", job2);
			}
		}catch(Exception e){}
		JsonObject jo = job.build();
		return jo;
	}

	//------------------------------

	//----Get Convert Data from Json to Object----
	public void loadData(JsonObject jo){
		this.password=jo.getString("password");
		this.username=jo.getString("username");
		this.path=jo.getString("path");
		JsonObject personalInfo = jo.getJsonObject("personal_info");
		JsonObject name = personalInfo.getJsonObject("name");
		this.fName=name.getString("first");
		this.lName=name.getString("last");
		if(name.containsKey("middle")){
			this.mName=name.getString("middle");
		}
		JsonObject dob = personalInfo.getJsonObject("dob");
		this.dobD=dob.getInt("day");
		this.dobM=dob.getInt("month");
		this.dobY=dob.getInt("year");
		this.ssn=jo.getString("ssn");
		JsonObject con = jo.getJsonObject("contact");
		this.homeNumber=con.getString("home");
		this.cellNumber=con.getString("cell");
		this.email=con.getString("email");
		this.address=con.getString("address");
		this.city=con.getString("city");
		this.state=con.getString("state");
		this.zip=con.getString("zip");
		JsonObject contact = jo.getJsonObject("emergency");
		this.emergencyContact=contact.getString("contact");
		this.emergencyContactRelationship=contact.getString("relationship");
		this.eCCellNumber=contact.getString("cell");
		this.eCHomeNumber=contact.getString("home");
		if(jo.containsKey("changes")){
			JsonObject changesObject = jo.getJsonObject("changes");
			this.changedBy=changesObject.getString("updatedBy");
			changesObject = changesObject.getJsonObject("fieldsChanged");
			int i = 0;
			while (true){
				System.out.println(changesObject.containsKey("field"+i));
				if(changesObject.containsKey("field"+i)){
					changes.add(changesObject.getString("field"+i));
					System.out.println(changesObject.getString("field"+i));
				} else {
					break;
				}
				i++;
			}
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
