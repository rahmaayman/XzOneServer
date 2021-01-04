/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;


/**
 *
 * @author khattab
 */
public class DashBoardController implements Initializable {
    
    @FXML
    private Button homeBtn;
    @FXML
    private Button statsBtn;
    @FXML
    private BorderPane bp;
    @FXML
    private VBox home;
    @FXML
    private ImageView openServerImgView;
    
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void home(ActionEvent event) {
        
        bp.setCenter(home);
    }

    @FXML
    private void stats(ActionEvent event) {
        
        navigatePages("/views/ServerStatsView.fxml");
    }
    @FXML
    private void openServer(ActionEvent event) {
        System.out.println("You clicked me!");
        new tictactoeserverapp.Server();
    }
    
    private void navigatePages(String page){
        
        Parent root = null;
        
        try {
            root = FXMLLoader.load(getClass().getResource(page));
        } catch (IOException ex) {
            Logger.getLogger(DashBoardController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        bp.setCenter(root);
        
    }
    
    
    
}



