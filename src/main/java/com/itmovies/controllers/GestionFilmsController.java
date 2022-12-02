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
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private TextField stockField;

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
    private ImageView retourBtn;

    //private ObservableList<ObservableList> data;
    private String titreVal;
    private String realisateurVal;
    private String langueVal;
    private String dureeVal;
    private int anneeVal;
    private int idVal;
    private double prixVal;
    private int stockVal;

    Film baseFilm;
    Film filmToAdd;
    Film filmToModify;

    String keyword;


    public void initialize(){
        okBtn.setVisible(false);
        Utilities.buildData("SELECT * FROM `films`", table);
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
                        Double.parseDouble(row[6]), Integer.parseInt(row[7]));
                fillValues(baseFilm);
                modifierBtn.setDisable(false);
                ajouterBtn.setDisable(true);
                supprimerBtn.setDisable(false);
            }
        });
    }


    @FXML
    void onAjouterBtnClick(ActionEvent event) {
        okBtn.setVisible(false);
        if (textFields.isVisible()){
            if (checkFields() && checkFieldsPattern()){
                getValues();
                filmToAdd = new Film(titreVal, realisateurVal, langueVal, anneeVal, dureeVal, prixVal, stockVal);
                try {
                    if (filmToAdd.ajouterFilm()){
                        Utilities.showErrorMessage("Succes");
                        Utilities.clearTable(table);
                        Utilities.buildData("SELECT * FROM FILMS", table);
                    } else {
                        Utilities.showErrorMessage("Echec");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    Utilities.showErrorMessage("Une erreur est survenue");
                    Utilities.showErrorMessage(e.toString());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    Utilities.showErrorMessage("Une erreur est survenue");
                    Utilities.showErrorMessage(e.toString());
                }

            } else{
                Utilities.showErrorMessage("Verifiez les champs");

            }

        } else {
            textFields.setVisible(true);
        }


    }

    @FXML
    void onModifierBtnClick(ActionEvent event) {
        if (checkFields() && checkFieldsPattern()){
            getValues();
            filmToModify = new Film(titreVal, realisateurVal, langueVal, anneeVal, dureeVal, prixVal, stockVal);
            if (filmToModify.equals(baseFilm)){
                Utilities.showErrorMessage("Rien n'a été modifié");
            } else {
                try {
                    if (baseFilm.modifierFilm(filmToModify)){
                        Utilities.showErrorMessage("Succes");
                        Utilities.clearTable(table);
                        Utilities.buildData("SELECT * FROM FILMS", table);
                        ajouterBtn.setDisable(false);
                        modifierBtn.setDisable(true);
                        supprimerBtn.setDisable(true);
                        textFields.setVisible(false);
                    } else {
                        Utilities.showErrorMessage("Echec");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    Utilities.showErrorMessage("Une erreur est survenue");
                    Utilities.showErrorMessage(e.toString());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    Utilities.showErrorMessage("Une erreur est survenue");
                    Utilities.showErrorMessage(e.toString());
                }
            }
        } else{
            Utilities.showErrorMessage("Verifiez les champs");
        }
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
                prix like ? OR stock like ?
                """;
        query = query.replaceAll("\\?", "'%" +keyword+ "%'");
        Utilities.clearTable(table);
        Utilities.buildData(query, table);
    }

    @FXML
    void onSupprimerBtnClick(ActionEvent event) {
        try {
            if (baseFilm.supprimerFilm()){
                 Utilities.showErrorMessage("Succes");
                 Utilities.clearTable(table);
                 Utilities.buildData("SELECT * FROM FILMS", table);
                 ajouterBtn.setDisable(false);
                 modifierBtn.setDisable(true);
                 supprimerBtn.setDisable(true);
                 textFields.setVisible(false);
            } else {
                Utilities.showErrorMessage("Echec");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Utilities.showErrorMessage("Une erreur est survenue");
            Utilities.showErrorMessage(e.toString());
        }
    }
    @FXML
    void onRetourBtnClick(MouseEvent event) throws IOException {
        getUserData(event);
        System.out.println(userID);
        System.out.println(userType);
        if (userType == "client"){
            Utilities.switchScene("AccueilClient.fxml", "Espace client", userType, userID, event);
        } else {
            Utilities.switchScene("AccueilAdmin.fxml", "Espace client", userType, userID, event);
        }
    }

    private void getUserData(MouseEvent event){
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        userType = ((String) stage.getUserData()).split("-")[0];
        userID = ((String) stage.getUserData()).split("-")[1];
        System.out.println(userID);
    }


    private void getValues(){
        titreVal = titreField.getText();
        realisateurVal = realField.getText();
        langueVal = langueField.getText();
        dureeVal = dureeField.getText();
        anneeVal = Integer.parseInt(anneeField.getText());
        prixVal = Double.parseDouble(prixField.getText());
        stockVal = Integer.parseInt(stockField.getText());
    }
    private void fillValues(Film film){
        titreField.setText(film.getTitre());
        realField.setText(film.getRealisateur());
        langueField.setText(film.getLangue());
        dureeField.setText(film.getDuree());
        anneeField.setText(Integer.toString(film.getAnnee()));
        prixField.setText(Double.toString(film.getPrix()));
        stockField.setText(Integer.toString(film.getStock()));
    }
    private boolean checkFields(){

        TextField[] fields = {
                titreField,
                realField,
                langueField,
                dureeField,
                anneeField,
                prixField,
                stockField
        };
        for (TextField field:
             fields) {
            if (field.getText().isBlank())
                return false;
        }
        return true;
    }

    // TODO: 29/11/2022 READ THIS
    private boolean checkFieldsPattern(){
        // dureeField, pattern = hh+h+mm
        // anneeField, pattern = yyyy
        // prixField, pattern = 0.00
        // stockField, pattern = 0

        //duree field
        Pattern dureePattern = Pattern.compile("^(0?\\d|1[0-9]|2[0-4])h0?[0-5][0-9]?$");
        Matcher dureeMatcher = dureePattern.matcher(dureeField.getText());
        if (!dureeMatcher.matches()) {
            Utilities.showErrorMessage("La durée doit être au format hh+h+mm");
            return false;
        }
        //annee field
        Pattern anneePattern = Pattern.compile("^\\d{4}$");
        Matcher anneeMatcher = anneePattern.matcher(anneeField.getText());
        //get the current year
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        if (!anneeMatcher.matches()) {
            Utilities.showErrorMessage("L'année doit être au format yyyy");
            return false;
        } else if (Integer.parseInt(anneeField.getText()) > currentYear){
            Utilities.showErrorMessage("L'année ne peut pas être supérieure à l'année actuelle");
            return false;
        }
        //prix field
        Pattern prixPattern = Pattern.compile("^\\d+(\\.\\d{1,2})?$");
        Matcher prixMatcher = prixPattern.matcher(prixField.getText());
        if (!prixMatcher.matches()) {
            Utilities.showErrorMessage("Le prix doit être au format 0.00");
            return false;
        }
        //stock field
        Pattern stockPattern = Pattern.compile("^\\d+$");
        Matcher stockMatcher = stockPattern.matcher(stockField.getText());
        if (!stockMatcher.matches()) {
            Utilities.showErrorMessage("Le stock doit être un nombre entier");
            return false;
        }
        return true;
    }
}
