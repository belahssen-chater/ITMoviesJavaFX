package com.itmovies.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class SplashScreenController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    @FXML
    private ImageView background;
    @FXML
    private ProgressBar progressBar;

    @FXML
    void initialize() {
        // fake a progress bar for 2  sec
        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                progressBar.setProgress(i / 100.0);
            }
            /*Platform.runLater(() -> {
                try {
                    Stage stage = (Stage) background.getScene().getWindow();
                    stage.close();
                    stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/fxml/Main.fxml"))));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });*/
        }).start();



    }

}
