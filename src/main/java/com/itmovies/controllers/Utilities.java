package com.itmovies.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Utilities {
    public static Connection con;
    private static Stage stage;
    private static Scene scene;
    private static Parent root;
    public final static String SALT = "$2a$10$xrWyOY5Eo/0JgXgNDCBwoe";


    public static void showErrorMessage(String error){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Une erreur s'est produite");
        alert.setContentText(error);
        alert.show();
    }
    public static void showSuccessMessage(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setContentText(message);
        alert.show();
    }


    public static void switchScene(String fxmlFileName, String title, ActionEvent event) throws IOException {
        //root = FXMLLoader.load(getClass().getResource(fxmlFileName));
        root = FXMLLoader.load(Utilities.class.getResource(fxmlFileName));

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        //stage.setUserData(userType);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }

    public static void switchScene(String fxmlFileName, String title, String userType, ActionEvent event) throws IOException {
        //root = FXMLLoader.load(getClass().getResource(fxmlFileName));
        root = FXMLLoader.load(Utilities.class.getResource(fxmlFileName));

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setUserData(userType);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }

    public static void switchScene(String fxmlFileName, String title, String userType, String userId, ActionEvent event) throws IOException {
        //root = FXMLLoader.load(getClass().getResource(fxmlFileName));
        root = FXMLLoader.load(Utilities.class.getResource(fxmlFileName));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setUserData(userType + "-" + userId);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }


    public static void connectDB() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");

        //con = DriverManager.getConnection("jdbc:mysql://localhost:3306/moviesdb","root","root");
        con = DriverManager.getConnection("jdbc:mysql://195.179.238.52/u962515021_moviesdb","u962515021_root","ITMovies2022");

    }
    public static void clearTable(TableView table){
        for ( int i = 0; i<table.getItems().size(); i++) {
            table.getItems().clear();
        }
        table.getColumns().clear();
    }
    public static void switchScene(String fxmlFileName, String title, String userType, String userId, MouseEvent event) throws IOException {
        //root = FXMLLoader.load(getClass().getResource(fxmlFileName));
        root = FXMLLoader.load(Utilities.class.getResource(fxmlFileName));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setUserData(userType + "-" + userId);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }

    public static void buildData(String sql, TableView table){
        ObservableList<ObservableList> data;
        data = FXCollections.observableArrayList();

        try{
            if (Utilities.con.isClosed())
                Utilities.connectDB();
            //SQL FOR SELECTING ALL OF CUSTOMER
            //ResultSet
            ResultSet rs = Utilities.con.createStatement().executeQuery(sql);

            /**********************************
             * TABLE COLUMN ADDED DYNAMICALLY *
             **********************************/
            for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++){
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>, ObservableValue<String>>(){
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });
                table.getColumns().addAll(col);
                System.out.println("Column ["+i+"] ");
            }

            /********************************
             * Data added to ObservableList *
             ********************************/
            while(rs.next()){
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                System.out.println("Row [1] added "+row );
                data.add(row);

            }


            //FINALLY ADDED TO TableView
            table.setItems(data);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println(e.toString());
            System.out.println("Error on Building Data");
        }
    }
}
