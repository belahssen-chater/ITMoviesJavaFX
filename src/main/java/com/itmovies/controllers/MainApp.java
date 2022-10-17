package com.itmovies.controllers;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        //writed by belahssen's PC

        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("SplashScreen.fxml"));
        Scene splashScene = new Scene(fxmlLoader.load());
        primaryStage.centerOnScreen();
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(splashScene);
        primaryStage.show();

        fxmlLoader = new FXMLLoader(getClass().getResource("LoginView.fxml"));
        Stage stage = new Stage();
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 1000, 600);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.setTitle("Connectez-vous");
        stage.setScene(scene);
        new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> {
                stage.show();
                primaryStage.hide();
                if (!stage.isShowing()){
                    Utilities.showErrorMessage("Une erreur s'est produite lors de l'affichage de la page d'accueil");
                }
            });

        }).start();














        /*System.out.println("Client "+ BCrypt.hashpw("op0580600", Utilities.SALT));
        System.out.println("admin1 "+ BCrypt.hashpw("admin1", Utilities.SALT));*/

        //FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("LoginView.fxml"));
        //or
        //FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LoginView.fxml"));

        /*Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.setTitle("Connectez-vous");
        stage.setScene(scene);
        stage.show();*/
        //for splash screen
        /*splash.show();
        new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> {
                stage.show();
            });

        }).start();*/


    }

    public static void main(String[] args) {
        launch();
    }
}