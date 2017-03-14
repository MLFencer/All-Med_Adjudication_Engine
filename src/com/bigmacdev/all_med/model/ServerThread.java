package com.bigmacdev.all_med.model;

import com.bigmacdev.all_med.controller.Main;

import java.io.*;
import java.net.Socket;
import java.util.Base64;

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
            System.out.println(in.readLine());
            while ((inputLine= in.readLine())!=null){
                System.out.println(inputLine);
                //outputLine = processInput(inputLine);
                //out.println(outputLine);
                break;
            }
            socket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        Main.threads.remove(threadID);
        return;
    }

    public String processInput(String input){
        String output="";
        String subInput;
        if (input.startsWith("patient:")){
            subInput=(input.substring(8,input.length()));
            output = processPatient(subInput);
        }else if (input.startsWith("unknown:")){

        }else if (input.startsWith("pharm:")){

        }else if (input.startsWith("practice")){

        }
        return output;
    }

    public String processPatient(String patientString){
        String output="";
        if (patientString.startsWith("checkLogin:")){
            patientString=patientString.substring(11,patientString.length());
            output=processPatientCheckLogin(patientString);
        } else if (patientString.startsWith("new")){
            patientString=patientString.substring(3,patientString.length());
            output=processPatientNewLogin(patientString);
        }
        return output;
    }

    public String processPatientNewLogin(String patientString){
        String output="";
        try{
            Person person = (Person) fromStringDecoder(patientString);
            Patient patient = new Patient(person);

        }catch (Exception e){
            System.out.println(e);
        }
        return output;
    }

    public String processPatientCheckLogin(String patientString){
        String output="";
        try {
            Person person = (Person) fromStringDecoder(patientString);
            if(person.fileExists()){
                Patient patient = new Patient(person);
                //String data = patient.getData();
                //String password = patient.getPassword();
                //Encrypt "true"+data with password as key
            } else {
                output="false";
            }

        }catch(Exception e){
            System.out.println(e);
        }
        return output;
    }

    public Object fromStringDecoder(String s) throws IOException, ClassNotFoundException{
        byte [] data = Base64.getDecoder().decode(s);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
        Object o = ois.readObject();
        ois.close();
        return o;
    }

    //Takes Data and key to encrypt data with. key is patients password or practice users password
    public String toStringEncoder(String data, String key){
        return "";
    }
}
