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
    ArrayList<String> informatin;
    String name;
    Socket clientSocket;
    public static Thread th;
    boolean flag = true;

    static Set<PlayersHandeler> playersSocket = new HashSet<PlayersHandeler>();
    PlayerDao playerDao;
    ArrayList<Player> pList;
    ArrayList<Player> apList;

    public PlayersHandeler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        playerDao = new PlayerDao();
        pList = playerDao.selectAllPlayers(0);
        try {
            objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
            playersSocket.add(this);
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
                informatin = (ArrayList<String>) objectInputStream.readObject();
                if (informatin.get(0).equals(Constants.WANT_TO_REGISTER)) {
                    System.out.println(informatin.get(0) + "  " + informatin.get(1) + "   " + informatin.get(2));
                    registration();
                    name = informatin.get(1);
                    sendMessageToPlayer(message);
                    pList = playerDao.selectAllPlayers(0);
                } else if (informatin.get(0).equals(Constants.WANT_TO_LOGIN)) {
                    System.out.println(informatin.get(0) + "  " + informatin.get(1) + "   " + informatin.get(2));
                    loginRequest();
                    name = informatin.get(1);
                    sendMessageToPlayer(message);
                    pList = playerDao.selectAllPlayers(0);
                } else if ((informatin.get(0).equals(Constants.GET_AVAILABLE_PLAYERS))) {
                    message = new ArrayList<>();
                    message.add(Constants.AVAILABLE_PLAYERS);
                    apList = new ArrayList<Player>();
                    System.out.println("aP " + apList);
                    apList = playerDao.selectAllPlayers();
                    System.out.println("aP " + apList);
                    for (int i = 0; i < apList.size(); i++) {
                        if (apList.get(i).getStatus() == 1 && !apList.get(i).getName().equals(name)) {
                            message.add(apList.get(i).getName());
                        }
                    }
                    System.out.println("aP " + apList);

                    sendMessageToPlayer(message);
                    System.out.println("aP is send to client");
                    apList.clear();
                    System.out.println("aP " + apList);
                } else if (informatin.get(0).equals(Constants.WANT_TO_PLAY)) {
                    String opponentName = informatin.get(1);
                    String playerName = this.name;

                    sendMessageToOpponent(opponentName, playerName, Constants.WANT_TO_PLAY);
                    System.out.println("send request " + opponentName + " " + playerName);
                } else if (informatin.get(0).equals(Constants.ACCEPT_PLAYING_REQUEST)) {
                    String opponentName = informatin.get(2);
                    String playerName = informatin.get(1);
                    playerDao.UpdatePlayerStatus(playerName, 2);
                    playerDao.UpdatePlayerStatus(opponentName, 2);
                    sendMessageToOpponent(opponentName, playerName, Constants.ACCEPT_PLAYING_REQUEST);
                    System.out.println("send accept " + opponentName + " " + playerName);
                } else if (informatin.get(0).equals(Constants.REJECT_PLAYING_REQUEST)) {
                    String opponentName = informatin.get(2);
                    String playerName = informatin.get(1);
                    sendMessageToOpponent(opponentName, playerName, Constants.REJECT_PLAYING_REQUEST);
                    System.out.println("send reject " + opponentName + " " + playerName);
                }

            } catch (EOFException s) {
                System.out.println("EOFException");
                try {
                    PlayersHandeler.this.objectInputStream.close();
                    PlayersHandeler.this.objectOutputStream.close();
                    playerDao.UpdatePlayerStatus(name, 0);
                    playersSocket.remove(PlayersHandeler.this);
                    playersSocket.remove(PlayersHandeler.this);
                    th.stop();
                    break;
                } catch (IOException ex) {
                    Logger.getLogger(PlayersHandeler.class.getName()).log(Level.SEVERE, null, ex);
                }

            } catch (SocketException ex) {

                playersSocket.remove(PlayersHandeler.this);
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
            if (informatin.get(1).equals(pList.get(i).getName())) {
                userNameUsed = true;
                message.add(Constants.DUPPLICATED_NAME);
                break;
            }
        }
        if (!userNameUsed) {
            Player p = new Player(informatin.get(1), informatin.get(2), 0, 1);
            playerDao.insertPlayer(p);
            message.add(Constants.YOU_ARA_REGISTER);
        }
    }

    public void sendMessageToPlayer(ArrayList<String> message) {

        System.out.println("in the method:" + message);

        try {
            for (PlayersHandeler playersHandler : playersSocket) {
                System.out.println("player" + playersHandler.name);

                if (name.equals(playersHandler.name)) {
                    System.out.println("if" + playersHandler.name);
                    playersHandler.objectOutputStream.writeObject(message);
                    playersHandler.objectOutputStream.flush();
                    break;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(PlayersHandeler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void sendMessageToOpponent(String opponentName, String playerName, String msg) {
        ArrayList<String> message = new ArrayList<>();
        message.add(msg);
        message.add(opponentName);
        message.add(playerName);
        for (PlayersHandeler playersHandler : playersSocket) {
            if (opponentName.equals(playersHandler.name)) {

                try {
                    playersHandler.objectOutputStream.writeObject(message);
                    playersHandler.objectOutputStream.flush();
                } catch (IOException ex) {
                    Logger.getLogger(PlayersHandeler.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
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
            if (pList.get(i).getName().equals(informatin.get(1)) && pList.get(i).getPassword().equals(informatin.get(2))) {
                isloged = true;
                playerDao.UpdatePlayerStatus(pList.get(i).getName(), 1);
                break;
            }
        }
        return isloged;
    }

}
