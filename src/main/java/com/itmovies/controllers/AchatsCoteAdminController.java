package com.itmovies.controllers;

import com.itmovies.models.Achat;
import com.itmovies.models.Admin;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;

public class AchatsCoteAdminController {
    private String userID;
    private String userType;
    private Achat achat;
    private void getUserData(ActionEvent event){
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        userType = ((String) stage.getUserData()).split("-")[0];
        userID = ((String) stage.getUserData()).split("-")[1];
        System.out.println(userID);
    }
    public void clearTable(){
        for ( int i = 0; i<table.getItems().size(); i++) {
            table.getItems().clear();
        }
        table.getColumns().clear();
    }
    private String query = """
            select a.id ID, a.etat Etat, c.nom "Client", f.titre Film, DATE_FORMAT(date, '%d-%b-%Y') Date, f.prix Prix, f.stock Stock
            from achats a
            join films f on a.idFilm=f.id
            join clients c on a.cinClient=c.cin
            """;

    @FXML
    private Button retourBtn;

    @FXML
    private Button refuserBtn;

    @FXML
    private TableView<?> table;

    @FXML
    private Button validerBtn;

    @FXML
    void initialize() {
        Utilities.buildData(query, table);
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
                achat = new Achat(Integer.parseInt(row[0]));
                validerBtn.setDisable(false);
                refuserBtn.setDisable(false);
            }
        });
    }

    @FXML
    void onRetourBtnClick(ActionEvent event) {
        getUserData(event);
        System.out.println(userID);
        System.out.println(userType);
        try {
            Utilities.switchScene("AccueilAdmin.fxml", "Espace client", userType, userID, event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onValiderBtnClick(ActionEvent event) {
        if (achat.validerAchat()){
            Utilities.showSuccessMessage("Achat validé avec succès");
            clearTable();
            Utilities.buildData(query, table);
        } else {
            Utilities.showErrorMessage("Erreur lors de la validation de l'achat");
        }
    }

    @FXML
    void onRefuserBtnClick(ActionEvent event) {
        if (achat.refuserAchat()){
            Utilities.showSuccessMessage("Achat refusé avec succès");
            clearTable();
            Utilities.buildData(query, table);
        } else {
            Utilities.showErrorMessage("Erreur lors du refus de l'achat");
        }
    }

}
