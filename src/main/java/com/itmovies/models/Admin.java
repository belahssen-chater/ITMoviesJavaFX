package com.itmovies.models;

import com.itmovies.controllers.Utilities;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Admin {
    String id;
    String nom;
    String prenom;
    String password;

    private PreparedStatement preparedStatement;
    private ResultSet rs;
    private Statement statement;
    private String query;
    private int rowsAffected = 0;

    public Admin(String id, String nom, String prenom, String password) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.password = password;
    }

    public Admin(String id, String nom, String prenom) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
    }

    public Admin(String id) {
        this.id = id;
    }

    public boolean ajouterAdmin(){
        try {
            if (Utilities.con.isClosed()){
                Utilities.connectDB();
            }
            statement = Utilities.con.createStatement();
            if (statement.executeQuery("SELECT * FROM ADMINS WHERE id='"+id+"'").next()){
                Utilities.showErrorMessage("Admin existant");
                return false;
            }
            query = "INSERT INTO admin (id, nom, prenom, password) VALUES (?, ?, ?, ?);";
            preparedStatement = Utilities.con.prepareStatement(query);
            preparedStatement.setString(1, this.id);
            preparedStatement.setString(2, this.nom);
            preparedStatement.setString(3, this.prenom);
            preparedStatement.setString(4, this.password);
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            Utilities.showErrorMessage(e.getMessage());
        }

        return rowsAffected > 0;
    }


    public boolean supprimerAdmin(){
        try {
            if (Utilities.con.isClosed()){
                Utilities.connectDB();
            }
            query = "DELETE FROM admins WHERE id=?";
            preparedStatement = Utilities.con.prepareStatement(query);
            preparedStatement.setString(1, this.id);
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            Utilities.showErrorMessage(e.getMessage());
        }
        return rowsAffected > 0;
    }
    public boolean modifierAdmin(Admin admin) throws SQLException, ClassNotFoundException {
        if (Utilities.con.isClosed()){
            Utilities.connectDB();
        }
        if (password == null){
            query = "UPDATE admins SET nom=?, prenom=? WHERE id=?;";
            preparedStatement = Utilities.con.prepareStatement(query);
            preparedStatement.setString(1, admin.nom);
            preparedStatement.setString(2, admin.prenom);
            preparedStatement.setString(3, this.id);
            rowsAffected = preparedStatement.executeUpdate();
        } else {
            query = "UPDATE admins SET nom=?, prenom=?, password=? WHERE id=?;";
            preparedStatement = Utilities.con.prepareStatement(query);
            preparedStatement.setString(1, admin.nom);
            preparedStatement.setString(2, admin.prenom);
            preparedStatement.setString(3, admin.password);
            preparedStatement.setString(4, this.id);
            rowsAffected = preparedStatement.executeUpdate();
        }
        return rowsAffected > 0;
    }


    public boolean equals(Admin admin){
        // fields check
        return this.nom.equals(admin.nom) && this.prenom.equals(admin.prenom);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
