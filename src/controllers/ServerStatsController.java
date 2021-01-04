/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.net.URL;
import static java.util.Collections.list;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.input.MouseEvent;
import tictactoeserverapp.databaseLayer.PlayerDao;

/**
 * FXML Controller class
 *
 * @author Rahma Ayman
 */
public class ServerStatsController implements Initializable {

    @FXML
    private PieChart serverPieChart;
    
    PlayerDao playerDao=new PlayerDao();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
    }    
    @FXML
    private void click(MouseEvent event) {
        int offlineCount=-1;
        int onlineCount=-1;
        int busyCount=-1;
        offlineCount=playerDao.getPlayersCount(0);
        onlineCount=playerDao.getPlayersCount(1);
        busyCount=playerDao.getPlayersCount(2);
        System.out.println("Offline Players count = "+offlineCount);
        System.out.println("Online Players count = "+onlineCount);
        System.out.println("Busy Players count = "+busyCount);
        ObservableList<PieChart.Data> list =FXCollections.observableArrayList(
                new PieChart.Data("Online Players", onlineCount),
                new PieChart.Data("Offline Players", offlineCount),
                new PieChart.Data("Busy Players", busyCount)
        );
        serverPieChart.setData(list);
    }
}
