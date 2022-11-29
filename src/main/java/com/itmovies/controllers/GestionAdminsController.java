package com.itmovies.controllers;

import com.itmovies.models.Admin;
import com.itmovies.models.Film;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class GestionAdminsController {
    private String nomValue;
    private String prenomValue;
    private String idValue;
    private String mdpValue;
    private Admin baseAdmin;
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
    private Button ajouterBtn;

    @FXML
    private Pane fieldsPane;
    @FXML
    private TextField idField;
    @FXML
    private Button retourBtn;

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

    @FXML
    private TextField searchField;
    @FXML
    void onSearchFieldChange(KeyEvent event) {
        String keyword = searchField.getText();
        String query = """
                SELECT id, nom, prenom FROM `admins` WHERE id!='super'
                AND (id LIKE ? OR nom LIKE ? OR prenom LIKE ?)
                """;
        query = query.replaceAll("\\?", "'%" +keyword+ "%'");
        Utilities.clearTable(table);
        Utilities.buildData(query, table);
    }

    public void initialize(){
        Utilities.buildData("SELECT id, nom, prenom FROM `admins` WHERE id!='super'", table);
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
    public void clearTable(){
        for ( int i = 0; i<table.getItems().size(); i++) {
            table.getItems().clear();
        }
        table.getColumns().clear();
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
            getValues();
            mdpValue = BCrypt.hashpw(mdpValue, Utilities.SALT);
            Admin adminToAdd = new Admin(idValue , nomValue, prenomValue, mdpValue);
            if (adminToAdd.ajouterAdmin()){
                Utilities.showSuccessMessage("Admin ajouté avec succès");
                clearTable();
                Utilities.buildData("SELECT id, nom, prenom FROM admins WHERE id!='super'", table);
                fieldsPane.setVisible(false);
                return;
            }
            Utilities.showErrorMessage("Erreur lors de l'ajout de l'admin");



        }

    }

    @FXML
    void onModifierBtnClick(ActionEvent event) {
        if (!checkFieldsWithoutId()){
            Utilities.showErrorMessage("Veuillez remplir tous les champs");
            return;
        }
        getValues();
        mdpValue = BCrypt.hashpw(mdpValue, Utilities.SALT);
        Admin adminToModify = new Admin("00", nomValue, prenomValue, mdpValue);
        try {
            if (baseAdmin.modifierAdmin(adminToModify)){
                Utilities.showSuccessMessage("Admin modifié avec succès");
                clearTable();
                Utilities.buildData("SELECT id, nom, prenom FROM admins WHERE id!='super'", table);
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
            clearTable();
            Utilities.buildData("SELECT id, nom, prenom FROM admins WHERE id!='super'", table);
            fieldsPane.setVisible(false);
            return;
        }
        Utilities.showErrorMessage("Erreur lors de la suppression de l'admin");


    }
    @FXML
    void onRetourBtnClick(ActionEvent event) throws IOException {
        getUserData(event);
            Utilities.switchScene("AccueilAdmin.fxml", "Espace client", userType, userID, event);
    }

    private void getValues(){
        nomValue = nomField.getText();
        prenomValue = prenomField.getText();
        mdpValue = mdpField.getText();
        idValue = idField.getText();
    }
    private void fillValues(Admin admin){
        // remplissage des champs sauf mdp
        idField.setText(admin.getId());
        nomField.setText(admin.getNom());
        prenomField.setText(admin.getPrenom());

    }
    private boolean checkFields(){
        // check if there is one or more blank fields
        return !(nomField.getText().isBlank() || prenomField.getText().isBlank() || mdpField.getText().isBlank() || idField.getText().isBlank());
    }
    private boolean checkFieldsWithoutId(){
        // check if there is one or more blank fields
        return !(nomField.getText().isBlank() || prenomField.getText().isBlank() || mdpField.getText().isBlank());
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
