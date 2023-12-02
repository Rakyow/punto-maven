package com.rakyow.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnexionMySQL {
    public static void main(String[] args) {
        // Informations pour la connexion à la base de données
        String url = "jdbc:mysql://localhost:3306/nom_de_ta_base"; // URL de la base de données
        String utilisateur = "ton_utilisateur"; // Nom d'utilisateur MySQL
        String motDePasse = "ton_mot_de_passe"; // Mot de passe MySQL

        // Établissement de la connexion
        try {
            Connection connexion = DriverManager.getConnection(url, utilisateur, motDePasse);
            if (connexion != null) {
                System.out.println("Connexion réussie à la base de données !");
                // Ici, tu peux exécuter tes requêtes SQL ou effectuer d'autres opérations
                // ...
                connexion.close(); // N'oublie pas de fermer la connexion lorsque tu as fini
            }
        } catch (SQLException e) {
            System.out.println("La connexion à la base de données a échoué !");
            e.printStackTrace();
        }
    }
}
