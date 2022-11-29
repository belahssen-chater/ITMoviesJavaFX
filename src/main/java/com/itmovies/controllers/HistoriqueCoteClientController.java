package com.itmovies.controllers;

import com.itmovies.models.Achat;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class HistoriqueCoteClientController {
    private int nbHover = 0;
    private String userID;
    private String userType;
    private void getUserData(MouseEvent event){
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        userType = ((String) stage.getUserData()).split("-")[0];
        userID = ((String) stage.getUserData()).split("-")[1];
        System.out.println(userID);
    }


    @FXML
    private Button retourBtn;

    @FXML
    private TableView<?> table;
    @FXML
    private TextField searchField;
    @FXML
    void onSearchFieldChange(KeyEvent event) {
        String keyword = searchField.getText();
        String query = """
            SELECT f.titre Film, DATE_FORMAT(a.date, '%d-%b-%Y') \"Date d'achat\", a.etat Etat, f.prix Prix
            FROM achats a
            JOIN films f ON a.idFilm=f.id
            WHERE a.cinClient='"""+userID+
                "' AND (f.titre LIKE ? OR a.etat LIKE ? OR DATE_FORMAT(a.date, '%d-%b-%Y') LIKE ? OR f.prix LIKE ?)";
        query = query.replaceAll("\\?", "'%" +keyword+ "%'");
        Utilities.clearTable(table);
        Utilities.buildData(query, table);
    }


    @FXML
    void onMouseHoverPane(MouseEvent event) {

        if (nbHover == 0){
            getUserData(event);
            String query = """
            SELECT f.titre Film, DATE_FORMAT(a.date, '%d-%b-%Y') \"Date d'achat\", a.etat Etat, f.prix Prix
            FROM achats a
            JOIN films f ON a.idFilm=f.id
            WHERE a.cinClient='"""+userID+
                    "'";
            Utilities.buildData(query, table);
            nbHover++;
        }
    }

    @FXML
    void onRetourBtnClick(ActionEvent event) {
        try {
            Utilities.switchScene("AccueilClient.fxml", "Espace client", userType, userID, event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
