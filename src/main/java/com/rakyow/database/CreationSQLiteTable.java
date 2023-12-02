package com.rakyow.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreationSQLiteTable {
    public static void main(String[] args) {
        Connection connexion = null;
        Statement statement = null;

        try {
            // Connexion à la base de données SQLite
            String url = "jdbc:sqlite:./punto.db";
            connexion = DriverManager.getConnection(url);
            statement = connexion.createStatement();

            // Requêtes de création de tables
            String[] requetesCreation = {
                "DROP TABLE IF EXISTS Card;",
                "DROP TABLE IF EXISTS Game;",
                "DROP TABLE IF EXISTS Player;",
                "CREATE TABLE Game (idGame INTEGER, createAt DATE);",
                "CREATE TABLE Player (idPlayer INTEGER, name VARCHAR(50), PRIMARY KEY(idPlayer));",
                "CREATE TABLE Card (idCard INTEGER, name VARCHAR(50), value INTEGER, color VARCHAR(50), idPlayer INTEGER, PRIMARY KEY(idCard), FOREIGN KEY(idPlayer) REFERENCES Player(idPlayer));"
            };

            // Exécution des requêtes de création de tables
            for (String requete : requetesCreation) {
                statement.execute(requete);
                System.out.println("Requête exécutée avec succès : " + requete);
            }

        } catch (SQLException e) {
            System.out.println("Une erreur s'est produite lors de la création des tables !");
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connexion != null) {
                    connexion.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

