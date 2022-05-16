/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jsws.collegeworkserver_3000;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author andrew
 */
public class DatabaseManager {
    
    private final static DatabaseManager INSTANCE = new DatabaseManager();
    private final static String DATABASE_URL = "jdbc:ucanaccess://.//login_info.accdb";
    
    private DatabaseManager() {
        //nothing here
    }
    
    public static DatabaseManager getInstance() {
        return INSTANCE;
    }
    
    public boolean insertLoginData(String email, String password, int permission) {
        try {
            Connection conn = DriverManager.getConnection(DATABASE_URL);
            String sql = "INSERT INTO logins (email, password, permission_level) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            preparedStatement.setInt(3, permission);
            int row = preparedStatement.executeUpdate();
            conn.close();
            return (row > 0);
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }
    
    public boolean queryLoginData(String email, String password) {
        try {
            Connection conn = DriverManager.getConnection(DATABASE_URL);
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("SELECT * FROM logins WHERE email = '" + email +"'");
            conn.close();
            return (result.next()) ? result.getString("password").equals(password) : false;
        } catch (SQLException except) {
            return false;
        }
    }
    
    public int getPermissionLevel(String email, String password){
        try {
            Connection conn = DriverManager.getConnection(DATABASE_URL);
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("SELECT * FROM logins WHERE email = '" + email +"' AND password = '" + password + "'");
            conn.close();
            return result.getInt("permission_level");
        } catch (SQLException except) {
            return 0;
        }
    }
}
