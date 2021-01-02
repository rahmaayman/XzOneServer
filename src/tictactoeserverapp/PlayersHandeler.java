/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoeserverapp;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.derby.iapi.sql.compile.CostEstimate;
import tictactoeserverapp.databaseLayer.DatabaseConnection;
import tictactoeserverapp.databaseLayer.Player;
import tictactoeserverapp.databaseLayer.PlayerDao;

/**
 *
 * @author Rahma Ayman
 */
public class PlayersHandeler implements Runnable {

    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private String receivingPacket;
    private int status;
    ArrayList<String> message;
    ArrayList<String> information;
    String name;
    Socket clientSocket;
    public static Thread th;
    boolean flag = true;
    PlayersHandeler opponent;
    static Set<PlayersHandeler> playersList = new HashSet<PlayersHandeler>();
    PlayerDao playerDao;
    ArrayList<Player> pList;

    public PlayersHandeler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        playerDao = new PlayerDao();
        pList = playerDao.selectAllPlayers(0);
        opponent = null;
        name="guest";
        try {
            objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
            playersList.add(this);
            th = new Thread(this);
            th.start();

        } catch (IOException ex) {
            Logger.getLogger(PlayersHandeler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
//                System.out.println("bbbb");
//                System.out.println("vvvvv");
                information = (ArrayList<String>) objectInputStream.readObject();
                if (information.get(0).equals(Constants.WANT_TO_REGISTER)) {
                    System.out.println(information.get(0) + "  " + information.get(1) + "   " + information.get(2));
                    registration();
                    name = information.get(1);
                    sendMessageToPlayer(message);
                    pList = playerDao.selectAllPlayers(0);
                } else if (information.get(0).equals(Constants.WANT_TO_LOGIN)) {
                    System.out.println(information.get(0) + "  " + information.get(1) + "   " + information.get(2));
                    loginRequest();
                    name = information.get(1);
                    sendMessageToPlayer(message);
                    pList = playerDao.selectAllPlayers(0);
                } else if(information.get(0).equals(Constants.WANT_TO_PLAY)){
                    message = new ArrayList<String>();
                    String opponentName = information.get(1);
                    String playerName = information.get(2);
                    PlayersHandeler attachedOpponent = attachOpponent(opponentName);
                    message.add(Constants.WANT_TO_PLAY);
                    message.add(playerName);
                    sendPlayingRequest(message, attachedOpponent);
                }

            } catch (EOFException s) {
                System.out.println("EOFException");
                try {
                    PlayersHandeler.this.objectInputStream.close();
                    PlayersHandeler.this.objectOutputStream.close();
                    playerDao.UpdatePlayerStatus(name, 0);
                    playersList.remove(PlayersHandeler.this);
                    playersList.remove(PlayersHandeler.this);
                    th.stop();
                    break;
                } catch (IOException ex) {
                    Logger.getLogger(PlayersHandeler.class.getName()).log(Level.SEVERE, null, ex);
                }

            } catch (SocketException ex) {

                playersList.remove(PlayersHandeler.this);
//                    objectOutputStream.close();
//                    objectInputStream.close();
//                    clientSocket.close();
                playerDao.UpdatePlayerStatus(name, 0);

                th.stop();
            } catch (IOException ex) {
                Logger.getLogger(PlayersHandeler.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(PlayersHandeler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void registration() {
        message = new ArrayList<String>();
        message.add(Constants.REGISTER);
        boolean userNameUsed = false;
        for (int i = 0; i < pList.size(); i++) {
            if (information.get(1).equals(pList.get(i).getName())) {
                userNameUsed = true;
                message.add(Constants.DUPPLICATED_NAME);
                break;
            }
        }
        if (!userNameUsed) {
            Player p = new Player(information.get(1), information.get(2), 0, 1);
            playerDao.insertPlayer(p);
            message.add(Constants.YOU_ARA_REGISTER);
        }
    }

    public void sendMessageToPlayer(ArrayList<String> message) {

        System.out.println("in the method:" + message);

        try {
           
                    this.objectOutputStream.writeObject(message);
           
        } catch (IOException ex) {
            Logger.getLogger(PlayersHandeler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loginRequest() {
        System.out.println("enter login");
        message = new ArrayList<String>();
        message.add(Constants.LOGIN);
        if (isLogged()) {
            message.add(Constants.YOU_LOGED_IN);
        } else {
            message.add(Constants.LOGIN_FAILURE);
        }
        System.out.println("after login");
        pList = playerDao.selectAllPlayers(0);
    }

    private boolean isLogged() {
        boolean isloged = false;
        pList = playerDao.selectAllPlayers(0);
        for (int i = 0; i < pList.size(); i++) {
            if (pList.get(i).getName().equals(information.get(1)) && pList.get(i).getPassword().equals(information.get(2))) {
                isloged = true;
                playerDao.UpdatePlayerStatus(pList.get(i).getName(), 1);
                break;
            }
        }
        return isloged;
    }
    
    private PlayersHandeler attachOpponent(String opponentName){
        
        PlayersHandeler assignedPlayer = null;
        PlayersHandeler localOpponent = null;
        
        for(PlayersHandeler player : playersList){
            
            if(player.name.equals(information.get(2)))
                assignedPlayer = player;
            else if(player.name.equals(opponentName))
                localOpponent = player; 
        }
      
        assignedPlayer.opponent = localOpponent;
        return localOpponent;
  
    }
    
    private void sendPlayingRequest(ArrayList<String>message, PlayersHandeler attachedOpponent){
        System.out.println("Hello from send playing request from server and opponent name is " + opponent.name);
        try {
            attachedOpponent.objectOutputStream.writeObject(message);
        } catch (IOException ex) {
            Logger.getLogger(PlayersHandeler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
