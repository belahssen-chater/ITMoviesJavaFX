package com.itmovies.models;

import com.itmovies.controllers.Utilities;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Client {
    private Statement statement;
    private PreparedStatement preparedStatement;
    private String query;
    private int rowsAffected = 0;


    private String cin;
    private String nom;
    private String mdp;
    private String tel;

    public Client(String cin, String nom, String mdp, String tel) {
        this.cin = cin;
        this.nom = nom;
        this.mdp = mdp;
        this.tel = tel;
    }
    // a constructor with only cin
    public Client(String cin) {
        this(cin, "", "", "");
    }
    // a constructor of all the fields without cin
    public Client(String nom, String mdp, String tel) {
        this("", nom, mdp, tel);
    }
    //a constructor of all the fields without password and cin
    public Client(String nom, String tel) {
        this("", nom, "", tel);
    }

    public boolean ajouterClient(){
        try {
            if (Utilities.con.isClosed()){
                Utilities.connectDB();
            }
            statement = Utilities.con.createStatement();
            if (statement.executeQuery("SELECT * FROM clients WHERE cin='"+this.cin+"'").next()){
                Utilities.showErrorMessage("CIN existant");
                statement.close();
                return false;
            } else if (statement.executeQuery("SELECT * FROM clients WHERE tel='"+this.tel+"'").next()){
                Utilities.showErrorMessage("Téléphone existant");
                statement.close();
                return false;
            }
            query = "INSERT INTO clients (cin, nom, tel, password) VALUES (?, ?, ?, ?);";
            preparedStatement = Utilities.con.prepareStatement(query);
            preparedStatement.setString(1, this.cin);
            preparedStatement.setString(2, this.nom);
            preparedStatement.setString(3, this.tel);
            preparedStatement.setString(4, this.mdp);
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            Utilities.showErrorMessage(e.getMessage());
            return false;
        }

        return rowsAffected > 0;
    }

    public boolean modifierClient(Client client) throws SQLException, ClassNotFoundException {
        if (Utilities.con.isClosed()){
            Utilities.connectDB();
        }
        if (client.mdp.equals("")){
            query = "UPDATE clients SET nom=?, tel=? WHERE cin=?;";
            preparedStatement = Utilities.con.prepareStatement(query);
            preparedStatement.setString(1, client.nom);
            preparedStatement.setString(2, client.tel);
            preparedStatement.setString(3, this.cin);
            rowsAffected = preparedStatement.executeUpdate();
        } else {
            query = "UPDATE clients SET nom=?, tel=?, password=? WHERE id=?;";
            preparedStatement = Utilities.con.prepareStatement(query);
            preparedStatement.setString(1, client.nom);
            preparedStatement.setString(2, client.tel);
            preparedStatement.setString(3, client.mdp);
            preparedStatement.setString(4, this.cin);
            rowsAffected = preparedStatement.executeUpdate();
        }
        return rowsAffected > 0;
    }

    public boolean supprimerClient(){
        try {
            if (Utilities.con.isClosed()){
                Utilities.connectDB();
            }
            query = "DELETE FROM clients WHERE cin=?";
            preparedStatement = Utilities.con.prepareStatement(query);
            preparedStatement.setString(1, this.cin);
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            Utilities.showErrorMessage(e.getMessage());
        }
        return rowsAffected > 0;
    }

    public boolean equals(Client client){
        return this.nom.equals(client.nom) && this.tel.equals(client.tel);
    }


    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}