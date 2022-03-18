package com.client.chatwindow;

import com.cipher.Alphabet;
import com.cipher.Vigenere;
import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.messages.Message;
import com.messages.MessageType;

import static com.messages.MessageType.CONNECTED;

public class Listener implements Runnable {
    private static final String HASCONNECTED = "has connected";

    private static String picture;
    private Socket socket;
    public String hostname;
    public int port;
    public static String username;
    public RoomController controller;
    private static ObjectOutputStream oos;
    private InputStream is;
    private ObjectInputStream input;
    private OutputStream outputStream;
    Logger logger = LoggerFactory.getLogger(Listener.class);

    public Listener(String hostname, int port, String username, String picture, RoomController controller) {
        this.hostname = hostname;
        this.port = port;
        Listener.username = username;
        Listener.picture = picture;
        this.controller = controller;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(hostname, port);

            System.out.println("Socket is connected with server!");

            LoginController.getInstance().showScene();
            outputStream = socket.getOutputStream();
            oos = new ObjectOutputStream(outputStream);
            is = socket.getInputStream();
            input = new ObjectInputStream(is);
        } catch (IOException e) {
            LoginController.getInstance().showErrorDialog("Could not connect to server");
            logger.error("Could not Connect");
        }
        logger.info("Connection accepted " + socket.getInetAddress() + ":" + socket.getPort());

        try {
            
            connect();
            logger.info("Sockets in and out ready!");
            while (socket.isConnected()) {
                Message message = (Message) input.readObject();

                if (message != null) {
                    logger.debug("Message received:" + message.getMsg() + " MessageType:" + message.getType() + "Name:"
                            + message.getName());
                    switch (message.getType()) {
                        case USER:
                            controller.addToChat(message);
                            break;
                        case NOTIFICATION:
                            controller.addAsServer(message);
                            controller.notificationSounds(message);
                            break;
                        case SERVER:
                            controller.addAsServer(message);
                            break;
                        case CONNECTED:
                            controller.setUserList(message);
                            break;
                        case DISCONNECTED:
                            controller.setUserList(message);
                            controller.addAsServer(message);
                            break;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            controller.logout();
        }
    }

    public static void send(String msg) throws IOException {
        Message createMessage = new Message();
        Vigenere cipher = new Vigenere();
        createMessage.setName(username);
        createMessage.setType(MessageType.USER);
        createMessage.setMsg(Vigenere.Encode(msg));
        createMessage.setPicture(getPicture());
        oos.writeObject(createMessage);
        oos.flush();
    }

    private void connect() throws IOException {
        Message createMessage = new Message();
        createMessage.setName(username);
        createMessage.setType(MessageType.CONNECTED);
        createMessage.setMsg(HASCONNECTED);
        createMessage.setPicture("user");
        oos.writeObject(createMessage);
        oos.flush();
    }
    
    public static void disconnect() throws IOException {
        Message createMessage = new Message();
        createMessage.setName(username);
        createMessage.setType(MessageType.DISCONNECTED);
        createMessage.setMsg("has disconnected");
        createMessage.setPicture("user");
        oos.writeObject(createMessage);
        oos.flush();
    }
    
    //Ma hoa bang vigenere
        public static String Encode(String text) {
        
        return null;
    }
    
    //Giai ma bang Vigenere
    public static String Decode(String text) {
        
        return null;
    }

    /**
     * @return the picture
     */
    public static String getPicture() {
        return picture;
    }

    /**
     * @param aPicture the picture to set
     */
    public static void setPicture(String aPicture) {
        picture = aPicture;
    }
}
