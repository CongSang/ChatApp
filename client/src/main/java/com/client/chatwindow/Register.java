/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.client.chatwindow;

import com.messages.MessageType;
import com.messages.UserInfoMessage;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author CÔNG SANG
 */
public class Register {
    public static boolean signUp(String firstName, String lastName, String email
            , String phone, String picture, String username, String password) {
        String hostname = "localhost";
        int port = 9001;
        Socket socket;
        
        try {
            socket = new Socket(hostname, port);
            System.out.println("Connecting...");
            UserInfoMessage msg = new UserInfoMessage(picture, firstName, lastName, email, phone, username, password, MessageType.SIGNUP);
            OutputStream os = socket.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(msg);
            DataInputStream input = new DataInputStream(socket.getInputStream());
            int correct = (int) input.readInt();
            if (socket.isConnected()){
                System.out.println("Success.");
                if(correct == 1) {
                    socket.close();
                    return true;
                }
                else {
                    socket.close();
                    return false;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
