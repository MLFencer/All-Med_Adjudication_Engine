package com.bigmacdev.all_med.model;

import com.bigmacdev.all_med.controller.Main;
import org.jasypt.util.text.BasicTextEncryptor;

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
            inputLine = in.readLine();
            System.out.println(inputLine);
            System.out.println("process start");
            outputLine = processInput(inputLine);
            System.out.println(outputLine);
            out.println(outputLine);
            socket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        Main.threads.remove(threadID);
        return;
    }

    public String processInput(String input){
        System.out.println("Processing Started");
        String output="";
        String subInput;
        if (input.startsWith("patient:")){
            subInput=(input.substring(8,input.length()));
            System.out.println("processInput: sub: "+subInput);
            output = processPatient(subInput);
        }else if (input.startsWith("unknown:")){

        }else if (input.startsWith("pharm:")){

        }else if (input.startsWith("practice")){

        }
        return output;
    }

    public String processPatient(String patientString){
        String output="";
        if (patientString.startsWith("check:")){
            patientString=patientString.substring(6,patientString.length());
            System.out.println("processPatient: sub: "+patientString);
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
            patientString = decryptString(patientString);
            System.out.println("decrypted: "+patientString);
            Person person = (Person) fromStringDecoder(patientString);
            System.out.println("Object deserialized: "+person.getName());
            if(person.fileExists()){
                output="true";
            } else {
                output="false";
            }

        }catch(Exception e){
            System.out.println(e);
        }

        System.out.println("Exists: "+output);

        return output;
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

    public Object fromStringDecoder(String s) throws IOException, ClassNotFoundException{
        String rectifiedString = s.replace("\\","");
        byte [] data = Base64.getUrlDecoder().decode(rectifiedString);
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
