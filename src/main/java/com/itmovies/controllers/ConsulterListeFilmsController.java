package com.itmovies.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;

public class ConsulterListeFilmsController {

    @FXML
    private Button acheterBtn;

    @FXML
    private Button retourBtn;

    @FXML
    private TableView<?> table;

    public void initialize(){
        Utilities.buildData("SELECT * FROM FILMS", table);
        System.out.println(userType);


    }

    @FXML
    void onAcheterBtnClick(ActionEvent event) {

    }
    private String userID;
    private String userType;
    private void getUserData(ActionEvent event){
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        userType = ((String) stage.getUserData()).split("-")[0];
        userID = ((String) stage.getUserData()).split("-")[1];
        System.out.println(userID);
    }

    @FXML
    void onRetourBtnClick(ActionEvent event) throws IOException {
        getUserData(event);
        Utilities.switchScene("AccueilClient.fxml", "Espace client", userType, userID, event);
    }

}
