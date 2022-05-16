/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jsws.collegeworkserver_3000;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author andrew
 */
public class ServerApp {
    
    private static int login_port = 3001;
    private static int reg_port = 3002;
    
    public static void main(String[] args) {
        DatabaseManager dm = DatabaseManager.getInstance();
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(()->{ //listening for login attempts
            try {
                //listening for registrations
                ServerSocket servSocket = new ServerSocket(login_port);
                while(true){
                    System.out.println(Thread.currentThread() + ": listening for registrations...");
                    try {
                        Socket conn = servSocket.accept();
                        System.out.println(Thread.currentThread() + ": connection from " + conn.getInetAddress().toString());
                        Thread connected_response = new Thread(()->{ 
                            try {
                                InputStream input = conn.getInputStream();
                                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                                String[] data = reader.readLine().replaceAll("\\s+", "").split(",");
                                Boolean response = dm.queryLoginData(data[0], data[1]);
                                OutputStream output = conn.getOutputStream();
                                PrintWriter writer = new PrintWriter(output, true);
                                writer.println(response.toString());
                            } catch (IOException ex) {
                                System.out.println("Severe Error in Registration Thread: " + ex.getClass().getTypeName());
                            }
                            System.out.println(Thread.currentThread() + ": sent resonse to " + conn.getInetAddress().toString());
                        });
                        connected_response.start(); //execute a response
                    } catch (IOException ex) {
                        System.out.println("Severe Error Accepting REGISTER Connection: " + ex.getClass().getTypeName());
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(ServerApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        executor.execute(()->{ 
            try {
                //listening for registrations
                ServerSocket servSocket = new ServerSocket(reg_port);
                while(true){
                    System.out.println(Thread.currentThread() + ": listening for registrations...");
                    try {
                        Socket conn = servSocket.accept();
                        System.out.println(Thread.currentThread() + ": connection from " + conn.getInetAddress().toString());
                        Thread connected_response = new Thread(()->{ 
                            try {
                                InputStream input = conn.getInputStream();
                                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                                String[] data = reader.readLine().replaceAll("\\s+", "").split(",");
                                Boolean response = dm.insertLoginData(data[0], data[1], Integer.parseInt(data[2]));
                                OutputStream output = conn.getOutputStream();
                                PrintWriter writer = new PrintWriter(output, true);
                                writer.println(response.toString());
                            } catch (IOException ex) {
                                System.out.println("Severe Error in Registration Thread: " + ex.getClass().getTypeName());
                            }
                            System.out.println(Thread.currentThread() + ": sent resonse to " + conn.getInetAddress().toString());
                        });
                        connected_response.start(); //execute a response
                    } catch (IOException ex) {
                        System.out.println("Severe Error Accepting REGISTER Connection: " + ex.getClass().getTypeName());
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(ServerApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        System.out.println("Type 'quit' to exit application: ");
        while(true){
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                if(reader.readLine().toLowerCase().equals("quit")) System.exit(0);
            } catch (IOException ex) {
                //ignore
            }
        }
    }
}
