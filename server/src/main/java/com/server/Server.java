package com.server;

import com.exception.DuplicateUsernameException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.messages.Message;
import com.messages.MessageType;
import com.messages.SigninMessage;
import com.user.User;
import com.user.UserService;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.sql.SQLException;
import java.util.logging.Level;
import jdk.jshell.execution.Util;

public class Server {
    private static final int PORT = 9001;
    private static final HashMap<String, User> names = new HashMap<>();
    private static final HashSet<ObjectOutputStream> writers = new HashSet<>();
    private static final ArrayList<User> users = new ArrayList<>();
    static Logger logger = LoggerFactory.getLogger(Server.class);
    private static InputStream is;
    private static ObjectInputStream input;

    public static void main(String[] args) throws Exception {
        logger.info("The chat server is running.");
        ServerSocket listener;
        Socket socket;

        try {
            listener = new ServerSocket(PORT);
            while (true) {
                
                socket = listener.accept();
                is = socket.getInputStream();
                input = new ObjectInputStream(is);
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                SigninMessage s = (SigninMessage) input.readObject();
                    if(checkLogin(s)) {
                        output.writeInt(1);
                        while(true) {
                            socket = listener.accept();
                            new Handler(socket).start();
                            break;
                        }
                        
                    }
                    else 
                        output.writeInt(-1);
                System.out.print(socket.getRemoteSocketAddress().toString());
                System.out.println(" Connected");
                System.out.println("Waiting for clients...");        

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static boolean checkLogin(SigninMessage msg) throws SQLException {
        User u = UserService.getUser(msg.getUsername(), msg.getPassword());
        if (u != null) {
            return true;
        }
        return false;
    }

    private static class Handler extends Thread {
        private String name;
        private Socket socket;
        private Logger logger = LoggerFactory.getLogger(Handler.class);
        private User user;
        private ObjectInputStream input;
        private OutputStream os;
        private ObjectOutputStream output;
        private InputStream is;

        public Handler(Socket socket) throws IOException {
            this.socket = socket;
        }

        @Override
        public void run() {
            logger.info("Attempting to connect a user...");
            try {
                is = socket.getInputStream();
                input = new ObjectInputStream(is);
                os = socket.getOutputStream();
                output = new ObjectOutputStream(os);

                Message firstMessage = (Message) input.readObject();
                checkDuplicateUsername(firstMessage);
                writers.add(output);
                sendNotification(firstMessage);
                addToList();

                while (socket.isConnected()) {
                    Message inputmsg = (Message) input.readObject();
                    if (inputmsg != null) {
                        logger.info(inputmsg.getType() + " - " + inputmsg.getName() + ": " + inputmsg.getMsg());
                        switch (inputmsg.getType()) {
                            case USER:
                                write(inputmsg);
                                break;
                            case CONNECTED:
                                addToList();
                                break;                       
                        }
                    }
                }
            } catch (SocketException socketException) {
                logger.error("Socket Exception for user " + name);
            } catch (DuplicateUsernameException | IOException | ClassNotFoundException e) {
                logger.error("Exception in run() method for user: " + name, e);
            } finally {
                closeConnections();
            }
            System.out.println("Sever stopped!");
        }

        private synchronized void checkDuplicateUsername(Message firstMessage) throws DuplicateUsernameException {
            logger.info(firstMessage.getName() + " is trying to connect");
            if (!names.containsKey(firstMessage.getName())) {
                this.name = firstMessage.getName();
                user = new User();
                user.setFirstName(firstMessage.getName());
                user.setPicture(firstMessage.getPicture());

                users.add(user);
                names.put(name, user);

                logger.info(name + " has been added to the list");
            } else {
                logger.error(firstMessage.getName() + " is already connected");
                throw new DuplicateUsernameException(firstMessage.getName() + " is already connected");
            }
        }

        private Message sendNotification(Message firstMessage) throws IOException {
            Message msg = new Message();
            msg.setMsg("has joined the chat.");
            msg.setType(MessageType.NOTIFICATION);
            msg.setName(firstMessage.getName());
            msg.setPicture(firstMessage.getPicture());
            write(msg);
            return msg;
        }

        private Message removeFromList() throws IOException {
            logger.debug("removeFromList() method Enter");
            Message msg = new Message();
            msg.setMsg("has left the chat.");
            msg.setType(MessageType.DISCONNECTED);
            msg.setName(name);
            msg.setUserList(names);
            write(msg);
            logger.debug("removeFromList() method Exit");
            return msg;
        }
        
        /*
         * For displaying that a user has joined the server
         */
        private Message addToList() throws IOException {
            Message msg = new Message();
            msg.setMsg("Welcome, You have now joined the server!");
            msg.setType(MessageType.CONNECTED);
            msg.setName("SERVER");
            write(msg);
            return msg;
        }

        /*
         * Creates and sends a Message type to the listeners.
         */
        private void write(Message msg) throws IOException {
            for (ObjectOutputStream writer : writers) {
                msg.setUserList(names);
                msg.setUsers(users);
                msg.setOnlineCount(names.size());
                writer.writeObject(msg);
                writer.reset();
            }
        }

        /*
         * Once a user has been disconnected, we close the open connections and remove
         * the writers
         */
        private synchronized void closeConnections() {
            logger.debug("closeConnections() method Enter");
            logger.info(
                    "HashMap names:" + names.size() + " writers:" + writers.size() + " usersList size:" + users.size());
            if (name != null) {
                names.remove(name);
                logger.info("User: " + name + " has been removed!");
            }
            if (user != null) {
                users.remove(user);
                logger.info("User object: " + user + " has been removed!");
            }
            if (output != null) {
                writers.remove(output);
                logger.info("Writer object: " + user + " has been removed!");
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                removeFromList();
            } catch (IOException e) {
                e.printStackTrace();
            }
            logger.info(
                    "HashMap names:" + names.size() + " writers:" + writers.size() + " usersList size:" + users.size());
            logger.debug("closeConnections() method Exit");
        }
        
    }
}
