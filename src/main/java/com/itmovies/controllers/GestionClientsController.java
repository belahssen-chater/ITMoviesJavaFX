package com.itmovies.controllers;

import com.itmovies.models.Admin;
import com.itmovies.models.Client;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class GestionClientsController {
    private String nomValue;
    private String telValue;
    private String cinValue;
    private String mdpValue;
    private String userType;
    private String userID;

    private Client baseClient;


    @FXML
    private Button ajouterBtn;

    @FXML
    private TextField cinField;

    @FXML
    private TextField mdpField;

    @FXML
    private Button modifierBtn;

    @FXML
    private TextField nomField;

    @FXML
    private Button retourBtn;

    @FXML
    private Button supprimerBtn;

    @FXML
    private TableView<?> table;

    @FXML
    private TextField telField;

    @FXML
    private Pane fieldsPane;
    @FXML
    private TextField searchField;
    @FXML
    void onSearchFieldChange(KeyEvent event) {
        String keyword = searchField.getText();
        String query = """
                SELECT cin, nom, tel FROM clients
                WHERE cin LIKE ? OR nom LIKE ? OR tel LIKE ?
                """;
        query = query.replaceAll("\\?", "'%" +keyword+ "%'");
        Utilities.clearTable(table);
        Utilities.buildData(query, table);
    }

    @FXML
    void initialize() {
        Utilities.buildData("SELECT cin, nom, tel FROM clients", table);
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
                baseClient = new Client(row[0], row[1], "", row[2]);
                fillValues(baseClient);
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
            if (!checkFields()) {
                Utilities.showErrorMessage("Veuillez remplir tous les champs");
                return;
            }
            getValues();
            mdpValue = BCrypt.hashpw(mdpValue, Utilities.SALT);
            Client clientToAdd = new Client(cinValue, nomValue, mdpValue, telValue);
            if (clientToAdd.ajouterClient()) {
                Utilities.showSuccessMessage("Client ajout?? avec succ??s");
                clearTable();
                Utilities.buildData("SELECT cin, nom, tel FROM clients", table);
                fieldsPane.setVisible(false);
                return;
            }
            Utilities.showErrorMessage("Erreur lors de l'ajout du client");
        }
    }

    @FXML
    void onModifierBtnClick(ActionEvent event) {
        getValues();
        Client clientToModify;
        if (checkFieldsWithoutCin()){
            mdpValue = BCrypt.hashpw(mdpValue, Utilities.SALT);
            clientToModify = new Client(cinValue, nomValue, mdpValue, telValue);
        } else if (checkFieldsWithoutCINandMdp()){
            clientToModify = new Client(cinValue, nomValue, "", telValue);
        } else{
            Utilities.showErrorMessage("Veuillez remplir tous les champs");
            return;
        }
        try {
            if (baseClient.modifierClient(clientToModify)) {
                Utilities.showSuccessMessage("client modifi?? avec succ??s");
                clearTable();
                Utilities.buildData("SELECT cin, nom, tel FROM clients", table);
                fieldsPane.setVisible(false);
                ajouterBtn.setDisable(false);
                modifierBtn.setDisable(true);
                supprimerBtn.setDisable(true);
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Utilities.showErrorMessage("Erreur lors de la modification du client");
        cinField.setText("");
        nomField.setText("");
        telField.setText("");
        mdpField.setText("");
        ajouterBtn.setDisable(false);
        modifierBtn.setDisable(true);
        supprimerBtn.setDisable(true);

    }

    @FXML
    void onRetourBtnClick(ActionEvent event) {
        getUserData(event);
        System.out.println(userID);
        System.out.println(userType);
        try {
            Utilities.switchScene("AccueilAdmin.fxml", "Espace admin", userType, userID, event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onSupprimerBtnClick(ActionEvent event) {
        if (baseClient.supprimerClient()){
            Utilities.showSuccessMessage("Client supprim?? avec succ??s");
            clearTable();
            Utilities.buildData("SELECT cin, nom, tel FROM clients", table);
            fieldsPane.setVisible(false);
            return;
        }
        Utilities.showErrorMessage("Erreur lors de la suppression du client");
    }


    private void getValues(){
        nomValue = nomField.getText();
        telValue = telField.getText();
        cinValue = cinField.getText();
        mdpValue = mdpField.getText();
    }
    private void fillValues(Client client){
        // remplissage des champs sauf mdp et cin
        nomField.setText(client.getNom());
        telField.setText(client.getTel());




    }
    private boolean checkFields(){
        // check if there is one or more blank fields
        return !(nomField.getText().isBlank() || telField.getText().isBlank() || cinField.getText().isBlank() || mdpField.getText().isBlank());
    }
    private boolean checkFieldsWithoutCin(){
        // check if there is one or more blank fields
        return !(nomField.getText().isBlank() || telField.getText().isBlank() || mdpField.getText().isBlank());
    }
    private boolean checkFieldsWithoutCINandMdp(){
        // check if there is one or more blank fields
        return !(nomField.getText().isBlank() || telField.getText().isBlank());
    }
    private void getUserData(ActionEvent event){
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        userType = ((String) stage.getUserData()).split("-")[0];
        userID = ((String) stage.getUserData()).split("-")[1];
        System.out.println(userID);
    }


}
