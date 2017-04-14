package com.bigmacdev.all_med.model;

import com.bigmacdev.all_med.controller.Main;
import net.maritimecloud.internal.core.javax.json.Json;
import net.maritimecloud.internal.core.javax.json.JsonObject;
import net.maritimecloud.internal.core.javax.json.JsonObjectBuilder;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.jasypt.util.password.PasswordEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Base64;
import java.util.SplittableRandom;

public class ServerThread extends Thread{
    private Socket socket = null;
    public int threadID;

    public ServerThread(Socket socket, int i){
        super("MultiServerThread");
        this.socket=socket;
        this.threadID=i;
    }

    public void run(){
        System.out.println("Created!");
        try (
                PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                ) {
            String inputLine, outputLine;
            inputLine = in.readLine();
            //System.out.println(inputLine);
            System.out.println("process start");
            outputLine = processInput(inputLine);
            //System.out.println("Output Line: "+outputLine);
            out.println(outputLine);
            socket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        Main.threads.remove(threadID);
        return;
    }

    public String processInput(String input){
        //System.out.println("Processing Started");
        String output="";
        String subInput;
        if (input.startsWith("patient:")){
            subInput=(input.substring(8,input.length()));
            //System.out.println("processInput: sub: "+subInput);
            output = processPatient(subInput);
        }else if (input.startsWith("web:")){
            output=processWeb(input.substring(4,input.length()));
        }else if (input.startsWith("pharm:")){
            output=processPharma(input.substring(6, input.length()));
        }else if (input.startsWith("clinic:")){
            output=processClinic(input.substring(7, input.length()));
        }
        return output;
    }
    //-----pharma------

    private String processPharma(String s){
        String output="";
        if(s.startsWith("login:")){
            output = processPharmaLogin(s.substring(6, s.length()));
        }
        return output;
    }



    private String processPharmaLogin(String request){
        request = decryptString(request);
        String out = "";
        if(new File("storage/login/staff/"+request).exists()){
            Staff staff = new Staff();
            staff.loadData("storage/login/staff/"+request);
            if(staff.hasClinics()){
                out=staff.jsonToString(staff.createJson());
                out=encryptString(out);
                return out;
            }else{
                return "false";
            }

        } else {
            return "false";
        }
    }


    //-----------Process Clinic-------------
    private String processClinic(String request){
        String output="";
        if(request.startsWith("login:")){
            output=processClinicLogin(request.substring(6, request.length()));
        }else if (request.startsWith("updateClinic:")){
            output=processClinicUpdate(request.substring(13,request.length()));
        } else if(request.startsWith("updateLogin:")){
            output=processUpdateStaff(request.substring(12,request.length()));
        } else if (request.startsWith("getClinic:")){
            output=processGetClinic(request.substring(10,request.length()));
        } else if(request.startsWith("scheduleGet:")){
            output=processGetSchedule(request.substring(12, request.length()));
        } else if (request.startsWith("makeApp:")){
            output=processClinicCreateApp(request.substring(8, request.length()));
        }
        return output;
    }

    private String processClinicCreateApp(String request){
        request = decryptString(request);
        Practice clinic = new Practice();
        boolean b = clinic.createAppointment(Json.createReader(new StringReader(request)).readObject());
        return String.valueOf(b);
    }

    private String processGetSchedule(String request){
        Practice clinic = new Practice();
        return clinic.getAppointments(Json.createReader(new StringReader(decryptString(request))).readObject());
    }

    private String processGetClinic(String request){
        request = decryptString(request);
        Practice clinic = new Practice();
        System.out.println(request);
        clinic.loadFile(request);
        return encryptString(clinic.jsonToString(clinic.createJson()));
    }

    private String processUpdateStaff(String request){
        request=decryptString(request);
        Staff staff = new Staff();
        staff.loadData(request);
        return String.valueOf(staff.createFile());
    }

    private String processClinicUpdate(String request){
        request=decryptString(request);
        Practice clinic = new Practice();
        clinic.loadData(request);
        return String.valueOf(clinic.createFile());
    }

    private String processClinicLogin(String request){
        request = decryptString(request);
        String out = "";
        if(new File("storage/login/staff/"+request).exists()){
            Staff staff = new Staff();
            staff.loadData("storage/login/staff/"+request);
            if(staff.hasClinics()){
                out=staff.jsonToString(staff.createJson());
                out=encryptString(out);
                return out;
            }else{
                return "false";
            }

        } else {
            return "false";
        }
    }

    //------Process Web--------------
    private String processWeb(String request){
        String output="";
        if(request.startsWith("pharmacy:")){
            output=processCreatePharma(request.substring(9,request.length()));
        } else if(request.startsWith("clinic:")){
            output=processCreateClinic(request.substring(7,request.length()));
        } //else if(request.startsWith("er:")){
            //output=processCreateEr(request.substring(3,request.length()));
       // }
        return output;
    }

    private String processCreateClinic(String request){
        JsonObject jo = Json.createReader(new StringReader(request)).readObject();
        JsonObject mJo = jo.getJsonObject("manager");
        JsonObject pJo = jo.getJsonObject("clinic");
        String name = pJo.getString("name");
        String phone = pJo.getString("phone");
        JsonObject lJo = pJo.getJsonObject("location");
        String street = lJo.getString("street");
        String city = lJo.getString("city");
        String state = lJo.getString("state");
        String zip = lJo.getString("zip");
        String rooms = pJo.getString("rooms");

        String fName = mJo.getString("fname");
        String lName = mJo.getString("lname");
        String email = mJo.getString("email");
        String user = mJo.getString("username");
        String pass = mJo.getString("password");

        pass = encryptPassword(pass);

        Practice practice = new Practice(name, street, city, state, zip, phone, user, Integer.parseInt(rooms));

        Staff manager = new Staff(fName, lName, user, pass, email);

        manager.addLocation(practice.getPath(), 2);

        boolean m = manager.createFile();
        boolean p = practice.createFile();

        return String.valueOf(m && p);

    }

    private String processCreatePharma(String request){
        JsonObject jo = Json.createReader(new StringReader(request)).readObject();
        JsonObject mJo = jo.getJsonObject("manager");
        JsonObject pJo = jo.getJsonObject("pharmacy");
        String name = pJo.getString("name");
        String phone = pJo.getString("phone");
        JsonObject lJo = pJo.getJsonObject("location");
        String street = lJo.getString("street");
        String city = lJo.getString("city");
        String state = lJo.getString("state");
        String zip = lJo.getString("zip");

        String fName = mJo.getString("fname");
        String lName = mJo.getString("lname");
        String email = mJo.getString("email");
        String user = mJo.getString("username");
        String pass = mJo.getString("password");

        pass = encryptPassword(pass);

        Pharmacy pharmacy = new Pharmacy(name, street, city, state, zip, phone, user);

        Staff manager = new Staff(fName,lName,user,pass,email);

        manager.addLocation(pharmacy.getPath(), 1);

        boolean m = manager.createFile();
        boolean p = pharmacy.createFile();

        return String.valueOf(m && p);
    }


    //----- Process Patient Requests---------------

    private String processPatient(String patientString){
        String output="";
        if (patientString.startsWith("new:")){
            patientString=patientString.substring(4,patientString.length());
            output=processPatientNewLogin(patientString);
        } else if (patientString.startsWith("login:")){
            patientString=patientString.substring(6, patientString.length());
            output=processPatientLogin(patientString);
        }else if (patientString.startsWith("check:")) {
            patientString = patientString.substring(6, patientString.length());
            output = processCheckUsername(patientString);
        } else if(patientString.startsWith("get:")){
            patientString=patientString.substring(4,patientString.length());
            output= processGetRecords(patientString);
        } else if(patientString.startsWith("update:")){
            output=processUpdatePatient(patientString.substring(7, patientString.length()));
        }else if(patientString.equals("clinics")){
            output=getclinicslist();
        } else if (patientString.startsWith("access:")){
            output=giveAccess(patientString.substring(7, patientString.length()));
        } else if (patientString.startsWith("web")){

        }
        return output;
    }

    private String giveAccess(String s){
        Patient patient = new Patient();
        MicroPatient mp = new MicroPatient();
        String loc = s.substring(s.indexOf(';')+1, s.length());
        s=s.substring(0, s.indexOf(';'));
        patient.loadData(patient.getPatientData(s));
        mp.setDay(patient.getDobD());
        mp.setFirst(patient.getfName());
        mp.setLast(patient.getlName());
        mp.setMonth(patient.getDobM());
        mp.setYear(patient.getDobY());
        mp.setPath(patient.getFilePath());
        File file = new File("storage/data/clinic/"+loc+"/patients/");
        file.mkdirs();
        File f = new File("storage/data/clinic/"+loc+"/patients/"+mp.getLast()+"_"+mp.getFirst()+"_"+mp.getYear()+"_"+mp.getMonth()+"_"+mp.getDay()+".txt");
        try{
            f.createNewFile();
            PrintStream out = new PrintStream(new FileOutputStream(f));
            out.print(mp.toJsonString());
            out.close();
            return "true";
        }catch (Exception e){
            e.printStackTrace();
            return "false";
        }

    }

    private String getclinicslist(){
        File f = new File("storage/data/clinic");
        File [] files = f.listFiles();
        JsonObjectBuilder job = Json.createObjectBuilder();
        for(int i=0; i<files.length; i++){
            job.add("file"+i,files[i].getName());
        }
        return job.build().toString();
    }

    private String processUpdatePatient(String request){
        String output="false";
        try{
            request=decryptString(request);
            Patient patient = new Patient();
            System.out.println(request);
            System.out.println("Loading Data");
            patient.loadData(Json.createReader(new StringReader(request)).readObject());
            System.out.println("Load Data Finished");
            patient.createAdditionalFile();
            output="true";
        }catch (Exception e){
            e.printStackTrace();
        }
        return output;
    }

    private String processGetRecords(String request){
        String output="";
        request = decryptString(request);
        Login login = new Login();
        try{
            login.loadLoginData(Json.createReader(new StringReader(request)).readObject());
            String location = login.getLocation();
            Patient patient = new Patient();
            output = encryptString(patient.getPatientData(location).toString());
        } catch(Exception e){
            System.out.println(e);
            output="false";
        }
        return output;
    }

    private String processCheckUsername(String input){
        input=decryptString(input);
        Login login = new Login();
        try{
            login.loadLoginData(Json.createReader(new StringReader(input)).readObject());
            String location = "storage/login/patient/"+login.getUsername();
            System.out.println(login.getUsername());
            if(new File(location).exists()){
                return "true";
            }else {
                new File(location).mkdirs();
                return "false";
            }
        }catch (Exception e){
            e.printStackTrace();
            return "false";
        }
    }

    private String processPatientLogin(String patientString){
        patientString=decryptString(patientString);
        System.out.println(patientString);
        try{
            Login login = new Login();
            login.loadLoginData(Json.createReader(new StringReader(patientString)).readObject());
            String location = "storage/login/patient/"+login.getUsername();
            System.out.println(login.getUsername());
            if(new File(location).exists()){
                JsonObject jo = login.readLoginData(location+"/"+getLatestRecord(location));
                return encryptString(jo.toString());
            }
        } catch (Exception e){
            System.out.println(e.toString());
        }
        return "false";
    }


    public String processPatientNewLogin(String patientString){
        String output="";
        try{
            patientString=decryptString(patientString);
            System.out.println(patientString);
            Patient patient = new Patient();
            patient.loadData(Json.createReader(new StringReader(patientString)).readObject());
            patient.createFile();
            Login log = new Login();
            log.setLogin(patient.getUsername(),patient.getPassword());
            log.setLocation(patient.getFilePath());
            log.writeJsonToFile();
            output="true";

        }catch (Exception e){
            System.out.println(e);
            output="false";
        }
        return output;
    }


   //--------Encryption Beyond this point--------------------


    private String encryptPassword(String pass){
        try {
            BasicTextEncryptor encryptor = new BasicTextEncryptor();
            encryptor.setPassword(pass);
            pass = encryptor.encrypt(pass);
        }catch (Exception e){
            e.printStackTrace();
        }
        return pass;
    }

    private String encryptString(String x){
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword(encryptionKey());
        return textEncryptor.encrypt(x);
    }

    private static String decryptString(String x) {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword(encryptionKey());
        return textEncryptor.decrypt(x);

    }

    private static String encryptionKey(){
        Long unixTime = System.currentTimeMillis()/10000000;
        System.out.println(""+unixTime);
        String keyGenSeed = unixTime+"";
        String output="";
        String keyGenSeedStart=keyGenSeed;
        while (keyGenSeed.length()>0) {
            char letter = keyGenSeed.charAt(0);
            keyGenSeed=keyGenSeed.substring(1, keyGenSeed.length());
            switch (letter){
                case '1':
                    output+="a";
                    break;
                case '2':
                    output+="b";
                    break;
                case '3':
                    output+="c";
                    break;
                case '4':
                    output+="d";
                    break;
                case '5':
                    output+="e";
                    break;
                case '6':
                    output+="f";
                    break;
                case '7':
                    output+="g";
                    break;
                case '8':
                    output+="h";
                    break;
                case '9':
                    output+="j";
                    break;
                case '0':
                    output+="k";
                    break;
            }
        }
        return output+keyGenSeedStart+output;
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
