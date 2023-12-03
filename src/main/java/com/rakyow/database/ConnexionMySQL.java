package com.rakyow.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnexionMySQL {
    private Connection connection;

    public ConnexionMySQL() {
        String url = "jdbc:mysql://localhost:3306/punto";
        String user = "rakyow";
        String password = "Azer";

        try {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("MySQL : Connexion établie !");
            createTables();
        } catch (SQLException e) {
            System.out.println("MySQL : Connexion échoué !");
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void createTables() {
        
        try {

            Statement statement = connection.createStatement();

            // Création de la table Game
            String createGameTable = "CREATE TABLE IF NOT EXISTS Game ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "name VARCHAR(50) UNIQUE,"
                    + "isWinnerGame VARCHAR(50),"
                    + "FOREIGN KEY(isWinnerGame) REFERENCES Player(name))";

            // Création de la table Round
            String createRoundTable = "CREATE TABLE IF NOT EXISTS Round ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "game_id INT,"
                    + "round_number INT,"
                    + "isWinnerRound VARCHAR(50),"
                    + "FOREIGN KEY(game_id) REFERENCES Game(id),"
                    + "FOREIGN KEY(isWinnerRound) REFERENCES Player(name))";

            // Création de la table Play
            String createPlayTable = "CREATE TABLE IF NOT EXISTS Play ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "round_id INT,"
                    + "player_id INT,"
                    + "color VARCHAR(50),"
                    + "score INT,"
                    + "coordX INT,"
                    + "coordY INT,"
                    + "FOREIGN KEY(round_id) REFERENCES Round(id),"
                    + "FOREIGN KEY(player_id) REFERENCES Player(id))";

            // Création de la table Player 
            String createPlayerTable = "CREATE TABLE IF NOT EXISTS Player ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "name VARCHAR(50) UNIQUE)";

            // Exécution des requêtes SQL de création des tables dans la base de données
            // on donne un retour à l'utilisateur
            statement.executeUpdate(createPlayerTable);
            statement.executeUpdate(createGameTable);
            statement.executeUpdate(createRoundTable);
            statement.executeUpdate(createPlayTable);

            System.out.println("MySQL : Tables créées avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("MySQL : Connexion fermée !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String addGame() {

        try {
            String gameName = generateName();

            Statement statement = connection.createStatement();

            while (statement.executeQuery("SELECT * FROM Game WHERE name = '" + gameName + "'").next()) {
                gameName = generateName();
            }

            String addGame = "INSERT INTO Game (name) VALUES ('" + gameName + "')";
            statement.executeUpdate(addGame);
            System.out.println("MySQL : Game ajouté");

            return gameName;
        } catch (SQLException e) {
            System.out.println("MySQL : Erreur lors de l'ajout du jeu : " + e.getMessage());
            return null;
        }


    }

        // example of name generation Game-A73B9
        private String generateName() {
            String name = "Game-";
            String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            String numbers = "0123456789";
    
            for (int i = 0; i < 8; i++) {
                
                int choice = (int) Math.floor(Math.random() * 2);
                if (choice == 0) {
                    name += alphabet.charAt((int) Math.floor(Math.random() * alphabet.length()));
                } else {
                    name += numbers.charAt((int) Math.floor(Math.random() * numbers.length()));
                }
            }
    
            return name;
        }
}
