/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoeserverapp;

import tictactoeserverapp.databaseLayer.Player;
import tictactoeserverapp.databaseLayer.PlayerDao;
import java.sql.Connection;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tictactoeserverapp.databaseLayer.Player;
import tictactoeserverapp.databaseLayer.Player;
import tictactoeserverapp.databaseLayer.PlayerDao;
import tictactoeserverapp.databaseLayer.PlayerDao;

/**
 *
 * @author Rahma Ayman
 */
public class TicTacToeServerApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
        //new Server();
    }

}
