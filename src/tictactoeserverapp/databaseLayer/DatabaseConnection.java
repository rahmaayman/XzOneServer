/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoeserverapp.databaseLayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.derby.jdbc.ClientDriver;
/**
 *
 * @author Rahma Ayman
 */
public class DatabaseConnection {
    private static DatabaseConnection instance;
    private static Connection connection;
    //DriverManager.registerDriver(new ClientDriver());
    
    private DatabaseConnection(){
    }
    static{
        try {
            DriverManager.deregisterDriver(new ClientDriver());
            connection=DriverManager.getConnection("jdbc:derby://localhost:1527/TicTacToBataBase", "root", "root");
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static DatabaseConnection getInstance() 
    { 
        if(instance==null){
            instance=new DatabaseConnection();
        }
        return instance; 
    } 
    public static Connection getConnection(){
        return connection;
    }
}
