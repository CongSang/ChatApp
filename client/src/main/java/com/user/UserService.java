/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.user;

import com.utils.Jdbc;
import java.sql.Connection;
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
}
