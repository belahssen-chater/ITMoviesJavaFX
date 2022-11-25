package com.itmovies.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChangerMdpController {
    PreparedStatement preparedStatement;
    ResultSet rs;
    private String userID;
    private String userType;
    private void getUserData(ActionEvent event){
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        userType = ((String) stage.getUserData()).split("-")[0];
        userID = ((String) stage.getUserData()).split("-")[1];
        System.out.println(userID);
        System.out.println(userType);
    }

    @FXML
    private PasswordField ancienMdpField;

    @FXML
    private Button changerMdpBtn;

    @FXML
    private PasswordField nouveauMdpField;
    @FXML
    private Button retourBtn;



    @FXML
    void onRetourBtnClick(ActionEvent event) throws IOException {
        getUserData(event);
        if (userType.equals("client")){
            Utilities.switchScene("AccueilClient.fxml", "Espace client", userType, userID, event);
        } else {
            Utilities.switchScene("AccueilAdmin.fxml", "Espace client", userType, userID, event);
        }
    }

    @FXML
    void onChangerMdpClick(ActionEvent event) {
        try{
            changeMdp(event);
        } catch (SQLException | ClassNotFoundException e){
            System.err.println(e);
        }

    }
    void changeMdp(ActionEvent event) throws SQLException, ClassNotFoundException {
        String query;
        if (Utilities.con.isClosed()) {
            Utilities.connectDB();
        }
        getUserData(event);
        if (userType.equals("client")){
            query = "UPDATE clients SET password=? WHERE password=? and cin=?;";
        } else {
            query = "UPDATE admins SET password=? WHERE password=? and id=?;";
        }
        preparedStatement = Utilities.con.prepareStatement(query);
        preparedStatement.setString(1, BCrypt.hashpw(nouveauMdpField.getText(), Utilities.SALT));
        preparedStatement.setString(2, BCrypt.hashpw(ancienMdpField.getText(), Utilities.SALT));
        preparedStatement.setString(3, userID);
        int rowsAffected = preparedStatement.executeUpdate();
        if (rowsAffected > 0)
            Utilities.showErrorMessage("Modifié avec succès");
        else
            Utilities.showErrorMessage("Une erreur s'est produite");
    }

}