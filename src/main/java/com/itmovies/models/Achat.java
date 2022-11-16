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
            // verifier si l'achat a deja ete traite
            if (this.isTreated()){
                return false;
            }

            
            
            // get the id of the film from the purchase
            query = "SELECT idFilm FROM achats WHERE id = " + this.id + ";";
            statement = Utilities.con.createStatement();
            rs = statement.executeQuery(query);
            rs.next();
            int idFilm = rs.getInt("idFilm");
            rs.close();
            // get the stock of the film
            query = "SELECT stock FROM films WHERE id = " + idFilm + ";";
            statement = Utilities.con.createStatement();
            rs = statement.executeQuery(query);
            rs.next();
            int stock = rs.getInt("stock");
            rs.close();
            // if the stock is 0, show an error message and return false
            if (stock == 0){
                Utilities.showErrorMessage("Stock épuisé");
                return false;
            }
            // else, decrement the stock by 1
            query = "UPDATE films SET stock = " + (stock - 1) + " WHERE id = " + idFilm + ";";
            statement = Utilities.con.createStatement();
            rowsAffected = statement.executeUpdate(query);
            // if the stock is decremented successfully, insert the new achat
            if (rowsAffected > 0){
                // changement d'etat de l'achat
                query = "UPDATE achats SET etat='Validé' WHERE id=?;";
                preparedStatement = Utilities.con.prepareStatement(query);
                preparedStatement.setInt(1, this.id);
                rowsAffected = preparedStatement.executeUpdate();
            }



            // changement d'etat de l'achat
            /*query = "UPDATE achats SET etat='Validé' WHERE id=?;";
            preparedStatement = Utilities.con.prepareStatement(query);
            preparedStatement.setInt(1, this.id);
            rowsAffected = preparedStatement.executeUpdate();*/
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
            if (this.isTreated()){
                return false;
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
    private boolean isTreated(){
        try {
            if (Utilities.con.isClosed()){
                Utilities.connectDB();
            }
            query = "SELECT etat FROM achats WHERE id = " + this.id + ";";
            statement = Utilities.con.createStatement();
            rs = statement.executeQuery(query);
            if (rs.next()){
                if (!rs.getString("etat").equalsIgnoreCase("En attente")){
                    Utilities.showErrorMessage("Cet achat a déjà été traité");
                    return true;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            Utilities.showErrorMessage(e.getMessage());
        }
        return false;
    }
}
