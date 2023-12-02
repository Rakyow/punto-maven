package com.rakyow.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnexionSQLite {
    private Connection connexion;

    // Méthode pour établir la connexion à la base de données SQLite
    public void connect(String nomFichierDB) {
        String url = "jdbc:sqlite:" + nomFichierDB;

        try {
            connexion = DriverManager.getConnection(url);
            System.out.println("Connexion à la base de données SQLite établie !");
        } catch (SQLException e) {
            System.out.println("La connexion à la base de données SQLite a échoué !");
            e.printStackTrace();
        }
    }

    // Méthode pour obtenir la connexion établie
    public Connection getConnection() {
        return connexion;
    }

    // Méthode pour fermer la connexion à la base de données
    public void closeConnection() {
        try {
            if (connexion != null && !connexion.isClosed()) {
                connexion.close();
                System.out.println("Connexion à la base de données SQLite fermée !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
