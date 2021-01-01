/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoeserverapp.databaseLayer;

import tictactoeserverapp.databaseLayer.Player;
import tictactoeserverapp.databaseLayer.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rahma Ayman
 */
public class PlayerDao {

    DatabaseConnection dbInstance = DatabaseConnection.getInstance();
    Connection con = DatabaseConnection.getConnection();

    public PlayerDao() {
        this.con = con;
    }

    private PreparedStatement pst = null;
    private Statement stmt;
    private ResultSet rs;
    private ArrayList<Player> pList = new ArrayList<>();

    //insert player in database
    public void insertPlayer(Player p) {
        try {
            pst = con.prepareStatement("INSERT INTO PLAYER VALUES (?,?,?,?)");
            pst.setString(1, p.getName());
            pst.setString(2, p.getPassword());
            pst.setInt(3, p.getScore());
            pst.setInt(4, p.getStatus());
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PlayerDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //update player score
    public void UpdatePlayerScore(Player p, int score) {
        try {
            pst = con.prepareStatement("UPDATE PLAYER SET SCORE=? WHERE NAME=?");
            p.setScore(score);
            pst.setInt(1, p.getScore());
            pst.setString(2, p.getName());
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PlayerDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //update player status 
    public void UpdatePlayerStatus(String userName, int status) {
        try {
            pst = con.prepareStatement("UPDATE PLAYER SET STATUS=? WHERE NAME=?");
            //p.setStatus(status);
            pst.setInt(1, status);
            pst.setString(2, userName);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PlayerDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<Player> selectallPlayers() {
        try {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String queryString = new String("SELECT * FROM PLAYER");
            rs = stmt.executeQuery(queryString);
            while (rs.next()) {
                String name = rs.getString(1);
                String password = rs.getString(2);
                int score = rs.getInt(3);
                int status = rs.getInt(4);

                pList.add(new Player(name, password, score, status));
            }

        } catch (SQLException ex) {
            Logger.getLogger(PlayerDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return pList;
    }

}
