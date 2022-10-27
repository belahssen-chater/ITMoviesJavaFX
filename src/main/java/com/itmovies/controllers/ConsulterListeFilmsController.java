package com.itmovies.controllers;

import com.itmovies.models.Achat;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class ConsulterListeFilmsController {
    Achat achat;

    @FXML
    private Button acheterBtn;

    @FXML
    private Button retourBtn;

    @FXML
    private TableView<?> table;

    public void initialize(){
        Utilities.buildData("SELECT * FROM `films`", table);
        System.out.println(userType);
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                TableView.TableViewSelectionModel selectionModel =
                        table.getSelectionModel();
                selectionModel.setSelectionMode(
                        SelectionMode.SINGLE);
                ObservableList selectedItems =
                        selectionModel.getSelectedItems();
                String[] row = selectedItems.get(0).toString().split(", ");
                row[0] = row[0].substring(1);
                row[row.length - 1] = row[row.length - 1].replace("]", "");
                achat = new Achat(userID, row[0]);
                acheterBtn.setDisable(false);

            }
        });


    }

    @FXML
    void onAcheterBtnClick(ActionEvent event) {
        if (achat.demanderAchat()){
            Utilities.showSuccessMessage("Achat effectué avec succès");
        }else{
            Utilities.showErrorMessage("Erreur lors de l'achat");
        }
    }
    @FXML
    private void onMouseEnteredOnTable(MouseEvent event) {
        System.out.println("Mouse entered on table");
        getUserData(event);
    }
    private String userID;
    private String userType;
    private void getUserData(MouseEvent event){
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        userType = ((String) stage.getUserData()).split("-")[0];
        userID = ((String) stage.getUserData()).split("-")[1];
        System.out.println(userID);
    }
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
