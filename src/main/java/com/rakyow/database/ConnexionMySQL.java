package com.rakyow.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class is used to connect to the MySQL database.
 */
public class ConnexionMySQL {

    private Connection connection; // connection to the MySQL database

    /**
     * This constructor is used to connect to the MySQL database.
     */
    public ConnexionMySQL() {
        String url = "jdbc:mysql://localhost:3306/punto";
        String user = "rakyow";
        String password = "Azer";

        try {
            connection = DriverManager.getConnection(url, user, password);
            createTables();
        } catch (SQLException e) {
            System.err.println("MySQL : Connexion échoué !");
            e.printStackTrace();
        }
    }

    /**
     * This method is used to get the connection to the MySQL database.
     * @return the connection to the MySQL database
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * This method is used to create the tables.
     */
    public void createTables() {
        
        try {

            Statement statement = connection.createStatement();

            String createGameTable = "CREATE TABLE IF NOT EXISTS Game ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "name VARCHAR(50) UNIQUE,"
                    + "isWinnerGame VARCHAR(50),"
                    + "FOREIGN KEY(isWinnerGame) REFERENCES Player(name))";

            String createRoundTable = "CREATE TABLE IF NOT EXISTS Round ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "game_id INT,"
                    + "round_number INT,"
                    + "isWinnerRound VARCHAR(50),"
                    + "FOREIGN KEY(game_id) REFERENCES Game(id),"
                    + "FOREIGN KEY(isWinnerRound) REFERENCES Player(name))";

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

            String createPlayerTable = "CREATE TABLE IF NOT EXISTS Player ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "name VARCHAR(50) UNIQUE)";

            statement.executeUpdate(createPlayerTable);
            statement.executeUpdate(createGameTable);
            statement.executeUpdate(createRoundTable);
            statement.executeUpdate(createPlayTable);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to close the connection.
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to add a game.
     * @param name game's name
     */
    public void addGame(String name) {

        try {
            Statement statement = connection.createStatement();

            String addGame = "INSERT INTO Game (name) VALUES ('" + name + "')";
            statement.executeUpdate(addGame);

        } catch (SQLException e) {
            System.err.println("MySQL : Erreur lors de l'ajout du jeu : " + e.getMessage());
        }
    }

    /**
     * This method is used to add a play.
     * @param game game's name
     * @param round_number round's number
     * @param player player's name
     * @param score player's score
     * @param color card's color
     * @param coordX card's x coordinate
     * @param coordY card's y coordinate
     */
    public void addPlay(String game, int round_number, String player, int score, String color, int coordX, int coordY) {
        try {
            Statement statement = connection.createStatement();

            String selectGameIdQuery = "SELECT id FROM Game WHERE name = '" + game + "'";
            ResultSet resultSet = statement.executeQuery(selectGameIdQuery);

            if (resultSet.next()) {
                int game_id = resultSet.getInt("id");

                String selectRoundIdQuery = "SELECT id FROM Round WHERE game_id = " + game_id + " AND round_number = " + round_number;
                resultSet = statement.executeQuery(selectRoundIdQuery);

                if (resultSet.next()) {
                    int round_id = resultSet.getInt("id");

                    String selectPlayerIdQuery = "SELECT id FROM Player WHERE name = '" + player + "'";
                    resultSet = statement.executeQuery(selectPlayerIdQuery);

                    if (resultSet.next()) {
                        int player_id = resultSet.getInt("id");

                        String addPlayQuery = "INSERT INTO Play (round_id, player_id, color, score, coordX, coordY) VALUES (" + round_id + ", " + player_id + ", '" + color + "', " + score + ", " + coordX + ", " + coordY + ")";
                        statement.executeUpdate(addPlayQuery);
                    } 
                } 
            } 
        } catch (SQLException e) {
            System.err.println("MySQL: Erreur lors de l'ajout du play : " + e.getMessage());
        }
    }

    /**
     * This method is used to update the game.
     * @param game game's name
     * @param winner game's winner
     */
    public void updateGame(String game, String winner) {
        try {
            Statement statement = connection.createStatement();

            String updateGameQuery = "UPDATE Game SET isWinnerGame = '" + winner + "' WHERE name = '" + game + "'";
            statement.executeUpdate(updateGameQuery);
        } catch (SQLException e) {
            System.err.println("MySQL: Erreur lors de la mise à jour du game : " + e.getMessage());
        }
    }

    /**
     * This method is used to update the round.
     * @param game game's name
     * @param round_number round's number
     * @param winner round's winner
     */
    public void updateRound(String game, int round_number, String winner) {
        try {
            Statement statement = connection.createStatement();

            String selectGameIdQuery = "SELECT id FROM Game WHERE name = '" + game + "'";
            ResultSet resultSet = statement.executeQuery(selectGameIdQuery);

            if (resultSet.next()) {
                int game_id = resultSet.getInt("id");

                String selectRoundIdQuery = "SELECT id FROM Round WHERE game_id = " + game_id + " AND round_number = " + round_number;
                resultSet = statement.executeQuery(selectRoundIdQuery);

                if (resultSet.next()) {
                    int round_id = resultSet.getInt("id");

                    String updateRoundQuery = "UPDATE Round SET isWinnerRound = '" + winner + "' WHERE id = " + round_id;
                    statement.executeUpdate(updateRoundQuery);
                } 
            } 
        } catch (SQLException e) {
            System.err.println("MySQL: Erreur lors de la mise à jour du round : " + e.getMessage());
        }
    }
    
    /**
     * This method is used to add a player.
     * @param name player's name
     */
    public void addPlayer(String name) {
        try {

            Statement statement = connection.createStatement();

            String selectPlayerQuery = "SELECT * FROM Player WHERE name = '" + name + "'";
            ResultSet resultSet = statement.executeQuery(selectPlayerQuery);

            if (!resultSet.next()) {
                String addPlayerQuery = "INSERT INTO Player (name) VALUES ('" + name + "')";
                statement.executeUpdate(addPlayerQuery);
            } 
        } catch (SQLException e) {
            System.err.println("MySQL : Erreur lors de l'ajout du joueur : " + e.getMessage());
        }
    }

    /**
     * This method is used to add a round.
     * @param game game's name
     * @param round_number round's number
     */
    public void addRound(String game, int round_number) {
        try {
            Statement statement = connection.createStatement();
    
            String selectGameIdQuery = "SELECT id FROM Game WHERE name = '" + game + "'";
            ResultSet resultSet = statement.executeQuery(selectGameIdQuery);
    
            if (resultSet.next()) {
                int game_id = resultSet.getInt("id");
    
                String addRoundQuery = "INSERT INTO Round (game_id, round_number) VALUES (" + game_id + ", " + round_number + ")";
                statement.executeUpdate(addRoundQuery);
            } 
        } catch (SQLException e) {
            System.err.println("SQLite: Erreur lors de l'ajout du round : " + e.getMessage());
        }
    }

    /**
     * This method is to check if the game's name is available.
     * @param name game's name
     * @return true if the game's name is available, false otherwise
     */
    public boolean isAvailableName(String name) {
        try {
            Statement statement = connection.createStatement();

            String selectGameQuery = "SELECT * FROM Game WHERE name = '" + name + "'";
            ResultSet resultSet = statement.executeQuery(selectGameQuery);

            if (resultSet.next()) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            System.err.println("MySQL : Erreur lors de la vérification du nom du jeu : " + e.getMessage());
            return false;
        }
    }

    public void dropTable() {
        try {
            Statement statement = connection.createStatement();

            String dropGameTable = "DROP TABLE IF EXISTS Game";
            String dropRoundTable = "DROP TABLE IF EXISTS Round";
            String dropPlayTable = "DROP TABLE IF EXISTS Play";
            String dropPlayerTable = "DROP TABLE IF EXISTS Player";

            statement.executeUpdate(dropGameTable);
            statement.executeUpdate(dropRoundTable);
            statement.executeUpdate(dropPlayTable);
            statement.executeUpdate(dropPlayerTable);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
