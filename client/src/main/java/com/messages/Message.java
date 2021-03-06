package com.messages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import com.user.User;

public class Message implements Serializable {

    private String name;
    private MessageType type;
    private String msg;
    private int count;
    private ArrayList<User> list;
    private ArrayList<User> users;
    private String picture;

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Message() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public ArrayList<User> getUserList() {
        return list;
    }

    public void setUserList(HashMap<String, User> userList) {
        this.list = new ArrayList<>(userList.values());
    }

    public void setOnlineCount(int count) {
        this.count = count;
    }

    public int getOnlineCount() {
        return this.count;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

}