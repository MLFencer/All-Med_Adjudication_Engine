package com.bigmacdev.all_med.model;
import net.maritimecloud.internal.core.javax.json.Json;
import net.maritimecloud.internal.core.javax.json.JsonObject;
import net.maritimecloud.internal.core.javax.json.JsonObjectBuilder;
import net.maritimecloud.internal.core.javax.json.JsonReader;

import javax.print.DocFlavor;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Practice {
	String name, address, city, state, manager, path, zip, phone;
	int rooms;
	ArrayList<String> staff = new ArrayList<String>();

	public Practice(String name, String street, String city, String state, String zip, String phone, String manager, int rooms){
		this.address=street;
		this.city=city;
		this.name=name;
		this.manager=manager;
		this.state=state;
		this.zip=zip;
		this.phone=phone;
		this.rooms=rooms;
		DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HHmm");
		Date date = new Date();
		this.path="storage/data/clinic/"+name+"_"+manager+"_"+dateFormat.format(date);
		new File(path+"/records").mkdirs();
	}

	public Practice(){}

	public void addStaff(String username){staff.add(username);}

	public String getPath(){return path;}

	public void loadFile(String loc){
		JsonObject jo = getData(loc);
		loadData(jo.toString());
	}

	public boolean createAppointment(JsonObject jo){
		String first=jo.getString("first");
		String last=jo.getString("last");
		int day=jo.getInt("day");
		int month=jo.getInt("month");
		int year=jo.getInt("year");
		int minute=jo.getInt("minute");
		int hour=jo.getInt("hour");
		String cPath = jo.getString("clinicPath");
		File f = new File(cPath+"/schedule/"+year+"/"+month+"/"+day);
		System.out.println(f.mkdirs());
		try {
			File file = new File(cPath + "/schedule/" + year + "/" + month + "/" + day + "/" + hour + "_" + minute + "_" + last + "_" + first + ".txt");
			System.out.println(file.createNewFile());
			PrintStream out = new PrintStream(new FileOutputStream(file));
			out.print(jo.toString());
			out.close();
			System.out.println("done");
			return true;
		}catch (Exception e){
			System.out.println("Failed");
			e.printStackTrace();
			return false;
		}
	}


	public boolean arrived(JsonObject jo){
		int day = jo.getInt("day");
		int month = jo.getInt("month");
		int year = jo.getInt("year");
		String loc = jo.getString("path");
		return true;
	}

	public boolean completed(JsonObject jo){
		int day = jo.getInt("day");
		int month = jo.getInt("month");
		int year = jo.getInt("year");
		String loc = jo.getString("path");
		return true;
	}

	public String getAppointments(JsonObject jo){
		int day = jo.getInt("day");
		int month = jo.getInt("month");
		int year = jo.getInt("year");
		String loc = jo.getString("path");
		System.out.println("Loc: "+loc);
		if(new File(loc+"/schedule/"+year+"/"+month+"/"+day).exists()){
			File[] f = new File(loc+"/schedule/"+year+"/"+month+"/"+day).listFiles();
			JsonObjectBuilder job = Json.createObjectBuilder();
			for(int i=0; i<f.length; i++){
				job.add("file"+i,f[i].getName());
			}
			return job.build().toString();
		}else{
			return "false";
		}
	}

	public void loadData(String s){
		JsonObject jo = Json.createReader(new StringReader(s)).readObject();
		this.name = jo.getString("name");
		this.phone=jo.getString("phone");
		this.manager=jo.getString("manager");
		this.rooms = jo.getInt("rooms");
		this.path = jo.getString("path");
		JsonObject loc = jo.getJsonObject("location");
		this.address=loc.getString("street");
		this.city=loc.getString("city");
		this.state=loc.getString("state");
		this.zip=loc.getString("zip");
		if(jo.containsKey("staff")){
			int i=0;
			JsonObject sta = jo.getJsonObject("staff");
			while(true){
				if(sta.containsKey("staff"+i)){
					staff.add(sta.getString("staff"+i));
				}else{
					break;
				}
			}
		}
	}

	public JsonObject createJson(){
		JsonObjectBuilder job = Json.createObjectBuilder();
		JsonObjectBuilder job2 = Json.createObjectBuilder();
		JsonObjectBuilder job3 = Json.createObjectBuilder();
		job.add("name", name);
		job.add("phone", phone);
		job.add("path", path);
		job.add("manager", manager);
		job.add("path", path);
		job.add("rooms", rooms);
		job3.add("street", address);
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

	public String jsonToString(JsonObject jo){return jo.toString();}

	public boolean createFile(){

		try{
			new File(getPath()+"/data.txt").createNewFile();
			PrintStream out = new PrintStream(new FileOutputStream(getPath()+"/data.txt"));
			out.print(jsonToString(createJson()));
			out.close();
			return true;
		} catch (Exception e){
			System.out.println(e);
			return false;
		}
	}

	public String getPatientList(String loc){
		if(new File(loc+"/patients/").exists()){
			String out = "";
			File [] pats = new File(loc+"/patients/").listFiles();
			for(int i =0; i<pats.length; i++){
				out=out+pats[i]+";";
			}
			return out;
		}else {
			return "false";
		}
	}

	public JsonObject getData(String loc){
		loc=loc+"/data.txt";
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

}
