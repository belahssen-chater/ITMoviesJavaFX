package com.itmovies.controllers;

import com.itmovies.models.Film;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GestionFilmsController {
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
    private TextField anneeField;

    @FXML
    private TextField dureeField;

    @FXML
    private TextField langueField;

    @FXML
    private Button modifierBtn;

    @FXML
    private Button okBtn;

    @FXML
    private TextField prixField;

    @FXML
    private TextField realField;

    @FXML
    private TextField rechercheField;

    @FXML
    private Button supprimerBtn;

    @FXML
    private TableView<ObservableList> table;

    @FXML
    private Pane textFields;

    @FXML
    private TextField titreField;
    @FXML
    private Button retourBtn;

    //private ObservableList<ObservableList> data;
    private String titreVal;
    private String realisateurVal;
    private String langueVal;
    private String dureeVal;
    private int anneeVal;
    private int idVal;
    private double prixVal;

    Film baseFilm;
    Film filmToAdd;
    Film filmToModify;

    String keyword;


    public void initialize(){
        Utilities.buildData("SELECT * FROM FILMS", table);
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                textFields.setVisible(true);
                okBtn.setVisible(false);
                TableView.TableViewSelectionModel selectionModel =
                        table.getSelectionModel();
                selectionModel.setSelectionMode(
                        SelectionMode.SINGLE);
                ObservableList selectedItems =
                        selectionModel.getSelectedItems();
                String[] row = selectedItems.get(0).toString().split(", ");
                row[0] = row[0].substring(1);
                row[row.length - 1] = row[row.length - 1].replace("]", "");
                //String[] row = (String[]) selectedItems.get(0);
                baseFilm = new Film(Integer.parseInt(row[0]), row[1], row[2], row[3], Integer.parseInt(row[4]), row[5],
                        Double.parseDouble(row[6]));
                fillValues(baseFilm);
                modifierBtn.setDisable(false);
                ajouterBtn.setDisable(true);
                supprimerBtn.setDisable(false);
            }
        });
    }


    @FXML
    void onAjouterBtnClick(ActionEvent event) throws SQLException, ClassNotFoundException {
        okBtn.setVisible(false);
        if (textFields.isVisible()){
            if (checkFields()){
                getValues();
                filmToAdd = new Film(titreVal, realisateurVal, langueVal, anneeVal, dureeVal, prixVal);
                if (filmToAdd.ajouterFilm()){
                    Utilities.showErrorMessage("Succes");
                    clearTable();
                    Utilities.buildData("SELECT * FROM FILMS", table);
                } else {
                    Utilities.showErrorMessage("Echec");
                }

            } else{
                Utilities.showErrorMessage("Verifiez les champs");

            }

        } else {
            textFields.setVisible(true);
        }


    }

    @FXML
    void onModifierBtnClick(ActionEvent event) throws SQLException, ClassNotFoundException {
        if (checkFields()){
            getValues();
            filmToModify = new Film(titreVal, realisateurVal, langueVal, anneeVal, dureeVal, prixVal);
            if (filmToModify.equals(baseFilm)){
                Utilities.showErrorMessage("Rien n'a été modifié");
            } else {
                if (baseFilm.modifierFilm(filmToModify)){
                    Utilities.showErrorMessage("Succes");
                    clearTable();
                    Utilities.buildData("SELECT * FROM FILMS", table);
                    ajouterBtn.setDisable(false);
                    modifierBtn.setDisable(true);
                    supprimerBtn.setDisable(true);
                    textFields.setVisible(false);
                } else {
                    Utilities.showErrorMessage("Echec");
                }
            }
        } else{
            Utilities.showErrorMessage("Verifiez les champs");
        }
    }
    public void clearTable(){
        for ( int i = 0; i<table.getItems().size(); i++) {
            table.getItems().clear();
        }
        table.getColumns().clear();
    }

    @FXML
    void onOkBtnClick(ActionEvent event) throws SQLException, ClassNotFoundException {

    }

    @FXML
    void onRechercheFieldChange(KeyEvent event) {
        keyword = rechercheField.getText();
        String query = """
                SELECT * FROM FILMS WHERE id like ? OR 
                titre like ? OR realisateur like ? OR 
                langue like ? OR annee like ? OR duree like ? OR 
                prix like ?;
                """;
        query = query.replaceAll("\\?", "'%" +keyword+ "%'");
        clearTable();
        Utilities.buildData(query, table);
    }

    @FXML
    void onSupprimerBtnClick(ActionEvent event) throws SQLException, ClassNotFoundException {
        if (baseFilm.supprimerFilm()){
             Utilities.showErrorMessage("Succes");
             clearTable();
             Utilities.buildData("SELECT * FROM FILMS", table);
             ajouterBtn.setDisable(false);
             modifierBtn.setDisable(true);
             supprimerBtn.setDisable(true);
             textFields.setVisible(false);
        } else {
            Utilities.showErrorMessage("Echec");
        }
    }
    @FXML
    void onRetourBtnClick(ActionEvent event) throws IOException {
        getUserData(event);
        if (userType == "client"){
            Utilities.switchScene("AccueilClient.fxml", "Espace client", userType, userID, event);
        } else {
            Utilities.switchScene("AccueilAdmin.fxml", "Espace client", userType, userID, event);
        }
    }


    private void getValues(){
        titreVal = titreField.getText();
        realisateurVal = realField.getText();
        langueVal = langueField.getText();
        dureeVal = dureeField.getText();
        anneeVal = Integer.parseInt(anneeField.getText());
        prixVal = Double.parseDouble(prixField.getText());
    }
    private void fillValues(Film film){
        titreField.setText(film.getTitre());
        realField.setText(film.getRealisateur());
        langueField.setText(film.getLangue());
        dureeField.setText(film.getDuree());
        anneeField.setText(Integer.toString(film.getAnnee()));
        prixField.setText(Double.toString(film.getPrix()));
    }
    private boolean checkFields(){

        TextField[] fields = {
                titreField,
                realField,
                langueField,
                dureeField,
                anneeField,
                prixField
        };
        for (TextField field:
             fields) {
            if (field.getText().isBlank())
                return false;
        }
        return true;
    }
}
