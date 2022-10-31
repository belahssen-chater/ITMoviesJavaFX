package com.itmovies.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class AccueilAdminController {
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
    private Button gererLesFilmsBtn;
    @FXML
    private Button gererLesClientsBtn;
    @FXML
    private Button afficherHistoriqueAdminBtn;
    @FXML
    private Button gererLesAdminsBtn;
    @FXML
    private Button changerMdpBtn;

    @FXML
    private void onClickGererLesAdminsBtn(ActionEvent event){
        getUserData(event);
        if (userType.equals("superAdmin")) {
            // changement de scene vers la gestion des admins
            try {
                Utilities.switchScene("GestionAdmins.fxml", "Gestion des administrateurs", userType, userID, event);
            } catch (IOException e) {
                Utilities.showErrorMessage(e.getMessage());
            }
        } else
            Utilities.showErrorMessage("You are not allowed to manage admins");

    }

    @FXML
    private void onChangerMdpClick(ActionEvent event) throws IOException {
        getUserData(event);
        Utilities.switchScene("ChangerMdp.fxml", "Changez votre mot de passe", userType, userID, event);
    }

    @FXML
    private void onGererLesFilmsClick(ActionEvent event) throws IOException {
        getUserData(event);

        Utilities.switchScene("GestionFilms.fxml", "Gestion des films", userType, userID, event);
    }
    @FXML
    private void onGererLesClientsClick(ActionEvent event) throws IOException {
        getUserData(event);

        Utilities.switchScene("GestionClients.fxml", "Gestion des clients", userType, userID, event);
    }
    @FXML
    private void onHistoriqueBtnClick(ActionEvent event) throws IOException {
        getUserData(event);

        Utilities.switchScene("AchatsCoteAdmin.fxml", "Historique d'achat", userType, userID, event);
    }
}
