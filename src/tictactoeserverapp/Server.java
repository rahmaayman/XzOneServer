/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoeserverapp;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rahma Ayman
 */
public class Server implements Runnable{
    ServerSocket serverSocket;
    public static Thread th;
    PlayersHandeler playersHandeler;
    public Server(){
        th=new Thread(this);
        th.start();
    }

    @Override
    public void run() {
        try {
            serverSocket=new ServerSocket(5000);
            while(true){
                System.out.println("before accept");
                Socket socket=serverSocket.accept();
                System.out.println("after accept");
                //PrintStream dis = new PrintStream(socket.getOutputStream());
                //dis.println("you are connected now");
                PlayersHandeler playersHandeler = new PlayersHandeler(socket);
            }
        }catch(SocketException ex){
            try {
                serverSocket.close();
            } catch (IOException ex1) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        
    }
}
