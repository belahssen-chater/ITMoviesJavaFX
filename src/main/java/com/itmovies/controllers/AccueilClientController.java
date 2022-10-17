package com.itmovies.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class AccueilClientController {
    private String userID;
    private String userType;
    private void getUserData(ActionEvent event){
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        userType = ((String) stage.getUserData()).split("-")[0];
        userID = ((String) stage.getUserData()).split("-")[1];
    }

    @FXML
    private Button afficherHistoriqueBtn;

    @FXML
    private Button changerMdpBtn;

    @FXML
    private Button consulterLesFilmsBtn;

    @FXML
    private void onAfficherHistoriqueClick(ActionEvent event){

    }

    @FXML
    private void onConsulterFilmsClick(ActionEvent event) throws IOException {
        getUserData(event);
        Utilities.switchScene("ConsulterListeFilms.fxml", "Liste des films", userType, userID, event);
        System.out.println(userType);

    }

    @FXML
    private void onChangerMdpClick(ActionEvent event) throws IOException {
        getUserData(event);
        Utilities.switchScene("ChangerMdp.fxml", "Changez votre mot de passe", userType, userID, event);
    }
}
