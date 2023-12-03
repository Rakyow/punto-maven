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
            System.out.println("MySQL : Connexion établie !");
            createTables();
        } catch (SQLException e) {
            System.out.println("MySQL : Connexion échoué !");
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

            System.out.println("MySQL : Tables créées avec succès !");
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
                System.out.println("MySQL : Connexion fermée !");
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
            System.out.println("MySQL : Game ajouté");

        } catch (SQLException e) {
            System.out.println("MySQL : Erreur lors de l'ajout du jeu : " + e.getMessage());
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
                System.out.println("MySQL: Game id récupéré : " + game_id);

                String selectRoundIdQuery = "SELECT id FROM Round WHERE game_id = " + game_id + " AND round_number = " + round_number;
                resultSet = statement.executeQuery(selectRoundIdQuery);

                if (resultSet.next()) {
                    int round_id = resultSet.getInt("id");
                    System.out.println("MySQL: Round id récupéré : " + round_id);

                    String selectPlayerIdQuery = "SELECT id FROM Player WHERE name = '" + player + "'";
                    resultSet = statement.executeQuery(selectPlayerIdQuery);

                    if (resultSet.next()) {
                        int player_id = resultSet.getInt("id");
                        System.out.println("MySQL: Player id récupéré : " + player_id);

                        String addPlayQuery = "INSERT INTO Play (round_id, player_id, color, score, coordX, coordY) VALUES (" + round_id + ", " + player_id + ", '" + color + "', " + score + ", " + coordX + ", " + coordY + ")";
                        statement.executeUpdate(addPlayQuery);
                        System.out.println("MySQL: Play ajouté");
                    } else {
                        System.out.println("MySQL: Aucun joueur trouvé avec le nom : " + player);
                    }
                } else {
                    System.out.println("MySQL: Aucun round trouvé avec le numéro : " + round_number);
                }
            } else {
                System.out.println("MySQL: Aucun jeu trouvé avec le nom : " + game);
            }
        } catch (SQLException e) {
            System.out.println("MySQL: Erreur lors de l'ajout du play : " + e.getMessage());
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
            System.out.println("MySQL: Game mis à jour");
        } catch (SQLException e) {
            System.out.println("MySQL: Erreur lors de la mise à jour du game : " + e.getMessage());
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
                System.out.println("MySQL: Game id récupéré : " + game_id);

                String selectRoundIdQuery = "SELECT id FROM Round WHERE game_id = " + game_id + " AND round_number = " + round_number;
                resultSet = statement.executeQuery(selectRoundIdQuery);

                if (resultSet.next()) {
                    int round_id = resultSet.getInt("id");
                    System.out.println("MySQL: Round id récupéré : " + round_id);

                    String updateRoundQuery = "UPDATE Round SET isWinnerRound = '" + winner + "' WHERE id = " + round_id;
                    statement.executeUpdate(updateRoundQuery);
                    System.out.println("MySQL: Round mis à jour");
                } else {
                    System.out.println("MySQL: Aucun round trouvé avec le numéro : " + round_number);
                }
            } else {
                System.out.println("MySQL: Aucun jeu trouvé avec le nom : " + game);
            }
        } catch (SQLException e) {
            System.out.println("MySQL: Erreur lors de la mise à jour du round : " + e.getMessage());
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

            if (resultSet.next()) {
                System.out.println("MySQL : Le joueur existe déjà !");
            } else {
                String addPlayerQuery = "INSERT INTO Player (name) VALUES ('" + name + "')";
                statement.executeUpdate(addPlayerQuery);
                System.out.println("MySQL : Player ajouté avec succès !");
            }
        } catch (SQLException e) {
            System.out.println("MySQL : Erreur lors de l'ajout du joueur : " + e.getMessage());
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
                System.out.println("SQLite: Game id récupéré : " + game_id);
    
                String addRoundQuery = "INSERT INTO Round (game_id, round_number) VALUES (" + game_id + ", " + round_number + ")";
                statement.executeUpdate(addRoundQuery);
                System.out.println("SQLite: Round ajouté");
            } else {
                System.out.println("SQLite: Aucun jeu trouvé avec le nom : " + game);
            }
        } catch (SQLException e) {
            System.out.println("SQLite: Erreur lors de l'ajout du round : " + e.getMessage());
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
            System.out.println("MySQL : Erreur lors de la vérification du nom du jeu : " + e.getMessage());
            return false;
        }
    }
}
