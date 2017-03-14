package com.bigmacdev.all_med.controller;

import com.bigmacdev.all_med.model.*;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Main {

    public static ArrayList<ServerThread> threads = new ArrayList<ServerThread>();
    private static String port;
    private static int portNumber;

    public static void main(String[] args) {
        //Check For File Location
        if(!new File("data").exists()){
            //createFolders();
        }

        if (args.length==0){
            port = "8088";
        } else {
            port = args[0];
        }

        portNumber = Integer.parseInt(port);
        boolean listening = true;

        try (ServerSocket serverSocket = new ServerSocket(portNumber)){
            while (listening){
                threads.add(new ServerThread(serverSocket.accept(), threads.size()));
                threads.get(threads.size()-1).start();
            }
        } catch (IOException e){
            System.err.println("Could not listen on port: "+portNumber);
            System.exit(-1);
        }
    }

    private static void createFolders() {
        String[] folderNames = {
                "patient",
                "doctor",
                "practice"
        };
        new File("data").mkdir();
        for (int i=0; i<folderNames.length;i++){
            new File("data/"+folderNames[i]).mkdir();
        }
    }
}
