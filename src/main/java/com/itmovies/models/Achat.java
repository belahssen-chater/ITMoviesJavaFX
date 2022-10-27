package com.itmovies.models;

import com.itmovies.controllers.Utilities;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Achat {
    private PreparedStatement preparedStatement;
    private ResultSet rs;
    private Statement statement;
    private String query;
    private int rowsAffected = 0;


    private int id;
    private String cinClient;
    private String idFilm;
    public Achat(int id) {
        this.id = id;
    }
    //a constructor that takes cinClient and idFilm as parameters
    public Achat(String cinClient, String idFilm) {
        this.cinClient = cinClient;
        this.idFilm = idFilm;
    }
    public boolean validerAchat(){
        try {
            if (Utilities.con.isClosed()){
                Utilities.connectDB();
            }
            query = "UPDATE achats SET etat='Validé' WHERE id=?;";
            preparedStatement = Utilities.con.prepareStatement(query);
            preparedStatement.setInt(1, this.id);
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            Utilities.showErrorMessage(e.getMessage());
        }
        return rowsAffected > 0;
    }
    public boolean refuserAchat(){
        try {
            if (Utilities.con.isClosed()){
                Utilities.connectDB();
            }
            query = "UPDATE achats SET etat='Refusé' WHERE id=?;";
            preparedStatement = Utilities.con.prepareStatement(query);
            preparedStatement.setInt(1, this.id);
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            Utilities.showErrorMessage(e.getMessage());
        }
        return rowsAffected > 0;
    }
    public boolean demanderAchat(){
        try {
            if (Utilities.con.isClosed()){
                Utilities.connectDB();
            }
            query = "INSERT INTO achats (cinClient, idFilm) VALUES (?, ?);";
            preparedStatement = Utilities.con.prepareStatement(query);
            preparedStatement.setString(1, this.cinClient);
            preparedStatement.setString(2, this.idFilm);
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            Utilities.showErrorMessage(e.getMessage());
        }
        return rowsAffected > 0;
    }
}
