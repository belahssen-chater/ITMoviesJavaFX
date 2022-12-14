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

public class ConsulterListeFilmsController {
    Achat achat;

    @FXML
    private Button acheterBtn;

    @FXML
    private TableView<?> table;
    @FXML
    private TextField searchField;
    @FXML
    void onSearchFieldChange(KeyEvent event) {
        String keyword = searchField.getText();
        String query = """
            SELECT * FROM `films` WHERE stock>0
            AND (id like ? or titre like ? or realisateur like ? or langue like ? or annee like ? or duree like ? or prix like ? or stock like ?)
            """;
        query = query.replaceAll("\\?", "'%" +keyword+ "%'");
        Utilities.clearTable(table);
        Utilities.buildData(query, table);
    }

    public void initialize(){
        Utilities.buildData("SELECT * FROM `films` WHERE stock>0", table);
        System.out.println(userType);
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                TableView.TableViewSelectionModel<?> selectionModel =
                        table.getSelectionModel();
                selectionModel.setSelectionMode(
                        SelectionMode.SINGLE);
                ObservableList<?> selectedItems =
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
    void onAcheterBtnClick() {
        if (achat.demanderAchat()){
            Utilities.showSuccessMessage("Achat effectu?? avec succ??s");
        }else{
            Utilities.showErrorMessage("Erreur lors de l'achat");
        }
    }
    int nbHover = 0;
    @FXML
    private void onMouseOverPane(MouseEvent event) {
        if (nbHover == 0) {
            getUserData(event);
            System.out.println(userType);
            System.out.println(userID);
            nbHover++;
        }

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
