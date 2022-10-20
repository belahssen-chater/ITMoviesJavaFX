package com.itmovies.controllers;

import com.itmovies.models.Admin;
import com.itmovies.models.Film;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.sql.SQLException;

public class GestionAdminsController {
    private String nomValue;
    private String prenomValue;
    private String idValue;
    private String mdpValue;
    private Admin baseAdmin;


    @FXML
    private Button ajouterBtn;

    @FXML
    private Pane fieldsPane;
    @FXML
    private TextField idField;


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
        if (!fieldsPane.isVisible())
            fieldsPane.setVisible(true);
        else {
            if (!checkFields()){
                Utilities.showErrorMessage("Veuillez remplir tous les champs");
                return;
            }
            mdpValue = BCrypt.hashpw(mdpValue, Utilities.SALT);
            Admin adminToAdd = new Admin(idValue , nomValue, prenomValue, mdpValue);
            if (adminToAdd.ajouterAdmin()){
                Utilities.showSuccessMessage("Admin ajouté avec succès");
                Utilities.buildData("SELECT id, nom, prenom FROM admins", table);
                fieldsPane.setVisible(false);
                return;
            }
            Utilities.showErrorMessage("Erreur lors de l'ajout de l'admin");



        }

    }

    @FXML
    void onModifierBtnClick(ActionEvent event) {
        if (!checkFields()){
            Utilities.showErrorMessage("Veuillez remplir tous les champs");
            return;
        }
        Admin adminToModify = new Admin(idValue , nomValue, prenomValue, mdpValue);
        try {
            if (baseAdmin.modifierAdmin(adminToModify)){
                Utilities.showSuccessMessage("Admin modifié avec succès");
                Utilities.buildData("SELECT id, nom, prenom FROM admins", table);
                fieldsPane.setVisible(false);
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Utilities.showErrorMessage("Erreur lors de la modification de l'admin");

    }

    @FXML
    void onSupprimerBtnClick(ActionEvent event) {
        if (baseAdmin.supprimerAdmin()){
            Utilities.showSuccessMessage("Admin supprimé avec succès");
            Utilities.buildData("SELECT id, nom, prenom FROM admins", table);
            fieldsPane.setVisible(false);
            return;
        }
        Utilities.showErrorMessage("Erreur lors de la suppression de l'admin");


    }
    private void getValues(){
        nomValue = nomField.getText();
        prenomValue = prenomField.getText();
        mdpValue = mdpField.getText();
        idValue = idField.getText();
    }
    private void fillValues(Admin admin){
        // remplissage des champs sauf mdp et id
        nomField.setText(admin.getNom());
        prenomField.setText(admin.getPrenom());

    }
    private boolean checkFields(){
        // check if there is one or more blank fields
        return !(nomField.getText().isBlank() || prenomField.getText().isBlank() || mdpField.getText().isBlank() || idField.getText().isBlank());
    }
    private void showFieldsWithIdAndMdp(){
        // show fields with id and mdp
        fieldsPane.setVisible(true);
        idField.setVisible(true);
        mdpField.setVisible(true);
    }
    private void hideFieldsWithIdAndMdp(){
        // hide fields with id and mdp
        fieldsPane.setVisible(false);

    }
    private void showFieldsWithoutIdAndMdp(){
        // show fields without id and mdp
        fieldsPane.setVisible(true);
        idField.setVisible(false);
        mdpField.setVisible(false);
    }
    private void hideFieldsWithoutIdAndMdp(){
        // hide fields without id and mdp
        fieldsPane.setVisible(false);
        idField.setVisible(true);
        mdpField.setVisible(true);
    }
}
