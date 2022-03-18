/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.user;

import com.utils.Jdbc;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author CÔNG SANG
 */
public class UserService {
    public static User getUser(String username, String password) throws SQLException {
        try (Connection conn = Jdbc.getConn()) {
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM users where username = ? AND password = ?");
            stm.setString(1, username);
            stm.setString(2, password);
            
            ResultSet rs = stm.executeQuery();
            
            User u = new  User();
            while(rs.next()) {
                u.setUsername(username);
                u.setPassword(password);
            }      
            return u;
        }     
    }
    
    public List<User> getUsers() throws SQLException {
        try (Connection conn = Jdbc.getConn()) {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM users");
            
            List<User> users = new ArrayList<>();
            
            while (rs.next()) {
                int id = rs.getInt("id");
                String first_name = rs.getNString("first_name");
                String last_name = rs.getNString("last_name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String picture = rs.getString("picture");
                String username = rs.getString("username");
                String password = rs.getString("password");
                
                users.add(new User(id, picture, first_name, last_name, email, phone, username, password));
            }
            
            return users;
        }
    }
    
    public static int addUser(String firstName, String lastName, String email
            , String phone, String picture, String username, String password) throws SQLException {
        try (Connection conn = Jdbc.getConn()) {
            PreparedStatement stm = conn.prepareStatement("INSERT INTO users (first_name, last_name, email, phone, picture, username, password)"
                        + "VALUES (?,?,?,?,?,?,?)");
            stm.setString(1, firstName);
            stm.setString(2, lastName);
            stm.setString(3, email);
            stm.setString(4, phone);
            stm.setString(5, picture);
            stm.setString(6, username);
            stm.setString(7, password);
            
            
            return stm.executeUpdate();
        }
        
    }
}
