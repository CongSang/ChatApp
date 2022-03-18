/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.messages;

import java.io.Serializable;

/**
 *
 * @author Admin
 */
public class SigninMessage implements Serializable{
    private String username;
    private String password;
    private MessageType type = MessageType.SIGNIN;

    public SigninMessage() {
    }

    public SigninMessage(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    
    
    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the type
     */
    public MessageType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(MessageType type) {
        this.type = type;
    }       
}
