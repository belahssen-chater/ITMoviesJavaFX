package com.itmovies.models;

import com.itmovies.controllers.Utilities;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Film {
    private int id;
    private String titre;
    private String realisateur;
    private String langue;
    private int annee;
    private String duree;
    private double prix;

    private PreparedStatement preparedStatement;
    private ResultSet rs;
    private String query;
    private int rowsAffected;


    public Film(int id, String titre, String realisateur, String langue, int annee, String duree, double prix) {
        this.id = id;
        this.titre = titre;
        this.realisateur = realisateur;
        this.langue = langue;
        this.annee = annee;
        this.duree = duree;
        this.prix = prix;
    }
    public Film(String titre, String realisateur, String langue, int annee, String duree, double prix) {
        this.titre = titre;
        this.realisateur = realisateur;
        this.langue = langue;
        this.annee = annee;
        this.duree = duree;
        this.prix = prix;
    }
    public Film(int id){
        this(id, "", "", "", 0, "", 0);
    }
    public boolean ajouterFilm() throws SQLException, ClassNotFoundException {
        if (Utilities.con.isClosed()){
            Utilities.connectDB();
        }
        query = "INSERT INTO films (titre, realisateur, langue, annee, duree, prix) VALUES (?, ?, ?, ?, ?, ?);";
        preparedStatement = Utilities.con.prepareStatement(query);
        preparedStatement.setString(1, this.titre);
        preparedStatement.setString(2, this.realisateur);
        preparedStatement.setString(3, this.langue);
        preparedStatement.setInt(4, this.annee);
        preparedStatement.setString(5, this.duree);
        preparedStatement.setDouble(6, this.prix);
        rowsAffected = preparedStatement.executeUpdate();
        return rowsAffected > 0;
    }
    public boolean supprimerFilm() throws SQLException, ClassNotFoundException {
        if (Utilities.con.isClosed()){
            Utilities.connectDB();
        }
        query = "DELETE FROM films WHERE id=?";
        preparedStatement = Utilities.con.prepareStatement(query);
        preparedStatement.setInt(1, this.id);
        rowsAffected = preparedStatement.executeUpdate();
        return rowsAffected > 0;
    }
    public boolean modifierFilm(Film updatedFilm) throws SQLException, ClassNotFoundException {
        if (Utilities.con.isClosed()){
            Utilities.connectDB();
        }
        query = "UPDATE films SET titre=?, realisateur=?, langue=?, annee=?, duree=?, prix=? WHERE id=?;";
        preparedStatement = Utilities.con.prepareStatement(query);
        preparedStatement.setString(1, updatedFilm.titre);
        preparedStatement.setString(2, updatedFilm.realisateur);
        preparedStatement.setString(3, updatedFilm.langue);
        preparedStatement.setInt(4, updatedFilm.annee);
        preparedStatement.setString(5, updatedFilm.duree);
        preparedStatement.setDouble(6, updatedFilm.prix);
        preparedStatement.setInt(7, this.id);
        rowsAffected = preparedStatement.executeUpdate();
        return rowsAffected > 0;
    }
    public boolean equals(Film film){
        if (this.annee != film.annee) return false;
        if (this.prix != film.prix) return false;
        if (!(this.duree.equals(film.duree))) return false;
        if (!(this.langue.equals(film.langue))) return false;
        if (!(this.realisateur.equals(film.realisateur))) return false;
        return this.titre.equals(film.titre);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getRealisateur() {
        return realisateur;
    }

    public void setRealisateur(String realisateur) {
        this.realisateur = realisateur;
    }

    public String getLangue() {
        return langue;
    }

    public void setLangue(String langue) {
        this.langue = langue;
    }

    public int getAnnee() {
        return annee;
    }

    public void setAnnee(int annee) {
        this.annee = annee;
    }

    public String getDuree() {
        return duree;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }
}
