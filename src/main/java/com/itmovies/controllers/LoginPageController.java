package com.itmovies.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class LoginPageController {
    PreparedStatement preparedStatement;
    ResultSet rs;
    String userID;
    String userType;
    //Login scene
    @FXML
    private Button connectBtn;
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ImageView exitIcon;

    @FXML
    void onExitIconClick(MouseEvent event) {
        // Close the program
        System.exit(0);
    }

    @FXML
    private void onKeyPressedPwdField(KeyEvent event){
        // on enter pressed, fire the connect btn
        if(event.getCode().toString().equals("ENTER")){
            connectBtn.fire();
        }
    }

    @FXML
    private void onConnectBtnClick(ActionEvent event){
        try{
            Utilities.connectDB();
            String query = "SELECT cin FROM clients WHERE cin=? AND password=?";
            preparedStatement = Utilities.con.prepareStatement(query);
            preparedStatement.setString(1, loginField.getText());
            preparedStatement.setString(2, BCrypt.hashpw(passwordField.getText(), Utilities.SALT));
            rs = preparedStatement.executeQuery();
            if (rs.next()){
                userID = rs.getString("cin");
                userType = "client";
                Utilities.switchScene("AccueilClient.fxml", "Espace client", userType, userID, event);
                System.out.println(userType);
                rs.close();
            } else {
                query = "SELECT id FROM admins WHERE id=? AND password=?";
                preparedStatement = Utilities.con.prepareStatement(query);
                preparedStatement.setString(1, loginField.getText());
                preparedStatement.setString(2, BCrypt.hashpw(passwordField.getText(), Utilities.SALT));
                rs = preparedStatement.executeQuery();
                if (rs.next()){
                    userID = rs.getString("id");
                    userType = userID.equals("super") ? "superAdmin" : "admin";
                    Utilities.switchScene("AccueilAdmin.fxml", "Espace Administrateur", userType, userID, event);
                    rs.close();
                } else {
                    Utilities.showErrorMessage("Login ou mot de passe incorrect");
                    Utilities.con.close();
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Utilities.showErrorMessage("Une erreur liée à la base de données s'est produite");
        } catch (IOException e) {
            e.printStackTrace();
            Utilities.showErrorMessage("Une erreur s'est produite lors de l'affichage de la page d'accueil");

        }
    }

}