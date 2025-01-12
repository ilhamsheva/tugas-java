/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pis.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

/**
 *
 * @author sheva
 */
public class Database {
    public static Connection connectDb() {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/passenger", "root", "");
            return connect;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    } 
}
