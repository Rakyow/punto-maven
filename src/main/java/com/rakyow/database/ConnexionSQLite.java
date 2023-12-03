package com.rakyow.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnexionSQLite {
    private Connection connexion;

    public ConnexionSQLite() {
        String url = "jdbc:sqlite:./db/punto.sqlite";

        try {
            this.connexion = DriverManager.getConnection(url);
            System.out.println("SQLite : Connexion établie !");
            createTables();
        } catch (SQLException e) {
            System.out.println("SQLite : Connexion échoué !");
            e.printStackTrace();
        }
    }

    public Connection getConnexion() {
        return this.connexion;
    }

    public void createTables() {

        try {
            Statement statement = connexion.createStatement();

            // Création de la table Game
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Game ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "name TEXT UNIQUE,"
                    + "isWinnerGame TEXT," 
                    + "FOREIGN KEY(isWinnerGame) REFERENCES Player(name))");

            // Création de la table Round
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Round ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "game_id TEXT,"
                    + "round_number INTEGER,"
                    + "isWinnerRound TEXT,"
                    + "FOREIGN KEY(game_id) REFERENCES Game(id),"
                    + "FOREIGN KEY(isWinnerRound) REFERENCES Player(name))");

            // Création de la table Play
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Play ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "round_id INTEGER,"
                    + "player_id INTEGER,"
                    + "color TEXT,"
                    + "score INTEGER,"
                    + "coordX INTEGER,"
                    + "coordY INTEGER,"
                    + "FOREIGN KEY(round_id) REFERENCES Round(id),"
                    + "FOREIGN KEY(player_id) REFERENCES Player(id))");

            // Création de la table Player 
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Player (" // le nom du joueur est unique
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "name TEXT UNIQUE)");

            System.out.println("SQLite : Tables créées avec succès !");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour fermer la connexion à la base de données
    public void closeConnection() {
        try {
            if (connexion != null && !connexion.isClosed()) {
                connexion.close();
                System.out.println("SQLite : Connexion fermée !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String addGame() {

        try {
            String name = generateName();
            Statement statement = connexion.createStatement();

            while (statement.executeQuery("SELECT id FROM Game WHERE name = '" + name + "'").getInt("id") != 0) {
                name = generateName();
            }

            statement.executeUpdate("INSERT INTO Game (name) VALUES ('" + name + "')");
            System.out.println("SQLite : Partie ajoutée");

            return name;
        } catch (SQLException e) {
            System.out.println("SQLite : Erreur lors de l'ajout du joueur : " + e.getMessage());
            return null;
        }

    }

    public void addRound(String game, int round_number) {
        try {
            Statement statement = connexion.createStatement();

            int game_id = statement.executeQuery("SELECT id FROM Game WHERE name = '" + game + "'").getInt("id");
            statement.executeUpdate("INSERT INTO Round (game_id, round_number) VALUES ('" + game_id + "', '" + round_number + "')");
            System.out.println("SQLite : Round ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("SQLite : Erreur lors de l'ajout du joueur : " + e.getMessage());
        }
    }

    public void addPlayer(String name) {
        try {
            Statement statement = connexion.createStatement();
            // check if player already exists
            if (statement.executeQuery("SELECT id FROM Player WHERE name = '" + name + "'").getInt("id") != 0) {
                System.out.println("SQLite : Le joueur existe déjà !");

            } else {
                statement.executeUpdate("INSERT INTO Player (name) VALUES ('" + name + "')");
                System.out.println("SQLite : Joueur ajouté avec succès !");
            }
        } catch (SQLException e) {
            System.out.println("SQLite : Erreur lors de l'ajout du joueur : " + e.getMessage());
        }
    }

    public void addPlay(String game, int round_number, String player, int score, String color, int coordX, int coordY) {
        try {
            Statement statement = connexion.createStatement();

            int game_id = statement.executeQuery("SELECT id FROM Game WHERE name = '" + game + "'").getInt("id");
            int round_id = statement.executeQuery("SELECT id FROM Round WHERE game_id = '" + game_id + "' AND round_number = '" + round_number + "'").getInt("id");
            int player_id = statement.executeQuery("SELECT id FROM Player WHERE name = '" + player + "'").getInt("id");
            statement.executeUpdate("INSERT INTO Play (round_id, player_id, score, color, coordX, coordY) VALUES ('" + round_id + "', '" + player_id + "', '" + score + "', '" + color + "', '" + coordX + "', '" + coordY + "')");
        } catch (SQLException e) {
            System.out.println("SQLite : Erreur lors de l'ajout du joueur : " + e.getMessage());
        }
    }

    public void updateGame(String game, String winner) {
        try {
            Statement statement = connexion.createStatement();

            int game_id = statement.executeQuery("SELECT id FROM Game WHERE name = '" + game + "'").getInt("id");
            statement.executeUpdate("UPDATE Game SET isWinnerGame = '" + winner + "' WHERE id = '" + game_id + "'");
        } catch (SQLException e) {
            System.out.println("SQLite : Erreur lors de l'ajout du joueur : " + e.getMessage());
        }
    }

    public void updateRound(String game, int round_number, String winner) {
        try {
            Statement statement = connexion.createStatement();

            int game_id = statement.executeQuery("SELECT id FROM Game WHERE name = '" + game + "'").getInt("id");
            int round_id = statement.executeQuery("SELECT id FROM Round WHERE game_id = '" + game_id + "' AND round_number = '" + round_number + "'").getInt("id");
            statement.executeUpdate("UPDATE Round SET isWinnerRound = '" + winner + "' WHERE id = '" + round_id + "'");
        } catch (SQLException e) {
            System.out.println("SQLite : Erreur lors de l'ajout du joueur : " + e.getMessage());
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
