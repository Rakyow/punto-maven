package com.rakyow.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class is used to connect to the database.
 */
public class ConnexionSQLite {

    private Connection connexion; // connection

    /**
     * This constructor is used to create a ConnexionSQLite.
     */
    public ConnexionSQLite() {
        String url = "jdbc:sqlite:./db/punto.sqlite";

        try {
            this.connexion = DriverManager.getConnection(url);
            createTables();
        } catch (SQLException e) {
            System.err.println("SQLite : Connexion échoué !");
            e.printStackTrace();
        }
    }

    /**
     * This method is used to get the connection.
     * @return connection
     */
    public Connection getConnexion() {
        return this.connexion;
    }

    /**
     * This method is used to create the tables.
     */
    public void createTables() {

        try {
            Statement statement = connexion.createStatement();

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Game ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "name TEXT UNIQUE,"
                    + "isWinnerGame TEXT," 
                    + "FOREIGN KEY(isWinnerGame) REFERENCES Player(name))");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Round ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "game_id INTEGER,"
                    + "round_number INTEGER,"
                    + "isWinnerRound TEXT,"
                    + "FOREIGN KEY(game_id) REFERENCES Game(id),"
                    + "FOREIGN KEY(isWinnerRound) REFERENCES Player(name))");

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

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Player (" 
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "name TEXT UNIQUE)");

            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to close the connection.
     */
    public void closeConnection() {
        try {
            if (connexion != null && !connexion.isClosed()) {
                connexion.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to check if the name is available.
     * @param name name to check
     * @return true if the name is available, false otherwise
     */
    public boolean isAvailableName(String name) {
        try {
            Statement statement = connexion.createStatement();
            return statement.executeQuery("SELECT id FROM Game WHERE name = '" + name + "'").getInt("id") == 0;
        } catch (SQLException e) {
            System.err.println("SQLite : Erreur lors de l'ajout du joueur : " + e.getMessage());
            return false;
        }
    }

    /**
     * This method is used to add a game.
     * @param name game's name
     * @return game's name
     */
    public String addGame(String name) {

        try {

            Statement statement = connexion.createStatement();

            statement.executeUpdate("INSERT INTO Game (name) VALUES ('" + name + "')");

            return name;
        } catch (SQLException e) {
            System.err.println("SQLite : Erreur lors de l'ajout du joueur : " + e.getMessage());
            return null;
        }

    }

    /**
     * This method is used to add a round.
     * @param game game's name
     * @param round_number round's number
     */
    public void addRound(String game, int round_number) {
        try {
            Statement statement = connexion.createStatement();
            int game_id = statement.executeQuery("SELECT id FROM Game WHERE name = '" + game + "'").getInt("id");
            statement.executeUpdate("INSERT INTO Round (game_id, round_number) VALUES ('" + game_id + "', '" + round_number + "')");
        } catch (SQLException e) {
            System.err.println("SQLite : Erreur lors de l'ajout du joueur : " + e.getMessage());
        }
    }

    /**
     * This method is used to add a player.
     * @param name player's name
     */
    public void addPlayer(String name) {
        try {
            Statement statement = connexion.createStatement();
            // check if player already exists
            if (statement.executeQuery("SELECT id FROM Player WHERE name = '" + name + "'").getInt("id") != 0) {

            } else {
                statement.executeUpdate("INSERT INTO Player (name) VALUES ('" + name + "')");
            }
        } catch (SQLException e) {
            System.err.println("SQLite : Erreur lors de l'ajout du joueur : " + e.getMessage());
        }
    }

    /**
     * This method is used to add a play.
     * @param game game's name
     * @param round_number round's number
     * @param player player's name
     * @param score card's score
     * @param color card's color
     * @param coordX card's x coordinate
     * @param coordY card's y coordinate
     */
    public void addPlay(String game, int round_number, String player, int score, String color, int coordX, int coordY) {
        try {
            Statement statement = connexion.createStatement();

            int game_id = statement.executeQuery("SELECT id FROM Game WHERE name = '" + game + "'").getInt("id");
            int round_id = statement.executeQuery("SELECT id FROM Round WHERE game_id = '" + game_id + "' AND round_number = '" + round_number + "'").getInt("id");
            int player_id = statement.executeQuery("SELECT id FROM Player WHERE name = '" + player + "'").getInt("id");
            statement.executeUpdate("INSERT INTO Play (round_id, player_id, score, color, coordX, coordY) VALUES ('" + round_id + "', '" + player_id + "', '" + score + "', '" + color + "', '" + coordX + "', '" + coordY + "')");
        } catch (SQLException e) {
            System.err.println("SQLite : Erreur lors de l'ajout du joueur : " + e.getMessage());
        }
    }

    /**
     * This method is used to update the game's winner.
     * @param game game's name
     * @param winner game's winner
     */
    public void updateGame(String game, String winner) {
        try {
            Statement statement = connexion.createStatement();

            int game_id = statement.executeQuery("SELECT id FROM Game WHERE name = '" + game + "'").getInt("id");
            statement.executeUpdate("UPDATE Game SET isWinnerGame = '" + winner + "' WHERE id = '" + game_id + "'");
        } catch (SQLException e) {
            System.err.println("SQLite : Erreur lors de l'ajout du joueur : " + e.getMessage());
        }
    }

    /**
     * This method is used to update the round's winner.
     * @param game game's name
     * @param round_number round's number
     * @param winner round's winner
     */
    public void updateRound(String game, int round_number, String winner) {
        try {
            Statement statement = connexion.createStatement();

            int game_id = statement.executeQuery("SELECT id FROM Game WHERE name = '" + game + "'").getInt("id");
            int round_id = statement.executeQuery("SELECT id FROM Round WHERE game_id = '" + game_id + "' AND round_number = '" + round_number + "'").getInt("id");
            statement.executeUpdate("UPDATE Round SET isWinnerRound = '" + winner + "' WHERE id = '" + round_id + "'");
        } catch (SQLException e) {
            System.err.println("SQLite : Erreur lors de l'ajout du joueur : " + e.getMessage());
        }
    }

    public void dropTable() {
        try {
            Statement statement = connexion.createStatement();
            statement.executeUpdate("DROP TABLE IF EXISTS Game");
            statement.executeUpdate("DROP TABLE IF EXISTS Round");
            statement.executeUpdate("DROP TABLE IF EXISTS Play");
            statement.executeUpdate("DROP TABLE IF EXISTS Player");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
