package com.itmovies.controllers;

import com.itmovies.models.Admin;
import com.itmovies.models.Film;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

public class GestionAdminsController {
    private String nomValue;
    private String prenomValue;
    private String mdpValue;
    private Admin baseAdmin;


    @FXML
    private Button ajouterBtn;

    @FXML
    private Pane fieldsPane;

    @FXML
    private PasswordField mdpField;

    @FXML
    private Button modifierBtn;

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private Button supprimerBtn;

    @FXML
    private TableView<?> table;

    public void initialize(){
        Utilities.buildData("SELECT id, nom, prenom FROM admins", table);
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fieldsPane.setVisible(true);
                TableView.TableViewSelectionModel selectionModel =
                        table.getSelectionModel();
                selectionModel.setSelectionMode(
                        SelectionMode.SINGLE);
                ObservableList selectedItems =
                        selectionModel.getSelectedItems();
                String[] row = selectedItems.get(0).toString().split(", ");
                row[0] = row[0].substring(1);
                row[row.length - 1] = row[row.length - 1].replace("]", "");
                baseAdmin = new Admin(row[0], row[1], row[2]);
                fillValues(baseAdmin);
                modifierBtn.setDisable(false);
                ajouterBtn.setDisable(true);
                supprimerBtn.setDisable(false);
            }
        });
    }

    @FXML
    void onAjouterBtnClick(ActionEvent event) {
        fieldsPane.setVisible(true);

    }

    @FXML
    void onModifierBtnClick(ActionEvent event) {

    }

    @FXML
    void onSupprimerBtnClick(ActionEvent event) {

    }
    private void getValues(){
        nomValue = nomField.getText();
        prenomValue = prenomField.getText();
        mdpValue = mdpField.getText();
    }
    private void fillValues(Admin admin){
        // remplissage des champs sauf mdp
        nomField.setText(admin.getNom());
        prenomField.setText(admin.getPrenom());

    }
}
