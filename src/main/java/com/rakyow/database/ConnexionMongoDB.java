package com.rakyow.database;

import com.mongodb.MongoException;
import com.mongodb.client.*;

import java.util.ArrayList;

import org.bson.Document;

/**
 * This class is used to connect to MongoDB.
 */
public class ConnexionMongoDB {

    private MongoClient mongoClient; // MongoDB client

    private MongoDatabase database; // MongoDB database

    /**
     * This constructor is used to create a ConnexionMongoDB.
     */
    public ConnexionMongoDB() {
        String connectionString = "mongodb://localhost:27017"; // Remplacez par votre URL de connexion
        String databaseName = "punto"; // Remplacez par le nom de votre base de données

        try {
            mongoClient = MongoClients.create(connectionString);
            database = mongoClient.getDatabase(databaseName);
            createCollections();
        } catch (Exception e) {
            System.err.println("MongoDB : Connexion échoué !");
            e.printStackTrace();
        }
    }

    /**
     * This method is used to create the collections.
     */
    private void createCollections() {
        createCollection("Game");
        createCollection("Round");
        createCollection("Play");
        createCollection("Player");
    }

    /**
     * This method is used to create a collection.
     * @param collectionName collection's name
     */
    private void createCollection(String collectionName) {
        if (!database.listCollectionNames().into(new ArrayList<>()).contains(collectionName)) {
            database.createCollection(collectionName);
        } 
    }

    /**
     * This method is used to close the connection.
     */
    public void closeConnection() {
        mongoClient.close();
    }

    /**
     * This method is used to add a game.
     * @param name game's name
     */
    public void addGame(String name) {
        try {
            MongoCollection<Document> collection = database.getCollection("Game");
            Document gameObject = new Document("name", name);
            collection.insertOne(gameObject);
            
        } catch (MongoException e) {
            System.err.println("MongoDB : Erreur lors de l'ajout du jeu : " + e.getMessage());
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
            MongoCollection<Document> collection = database.getCollection("Play");
            Document play = new Document("game", game).append("round_number", round_number).append("player", player).append("score", score).append("color", color).append("coordX", coordX).append("coordY", coordY);
            collection.insertOne(play);
        } catch (MongoException e) {
            System.err.println("MongoDB : Erreur lors de l'ajout du play : " + e.getMessage());
        }
    }

    /**
     * This method is used to add a player.
     * @param name player's name
     */
    public void addPlayer(String name) {
        try {

            MongoCollection<Document> collection = database.getCollection("Player");
            if(collection.find(new Document("name", name)).first() != null) {
                return;
            } else {
                Document player = new Document("name", name);
                collection.insertOne(player);
            }
        } catch (MongoException e) {
            System.err.println("MongoDB : Erreur lors de l'ajout du joueur : " + e.getMessage());
        }
    }

    /**
     * This method is used to add a round.
     * @param game game's name
     * @param round_number round's number
     */
    public void addRound(String game, int round_number) {
        try {
            MongoCollection<Document> collection = database.getCollection("Round");
            Document round = new Document("game", game).append("round_number", round_number);
            collection.insertOne(round);
            
        } catch (MongoException e) {
            System.err.println("MongoDB : Erreur lors de l'ajout du round : " + e.getMessage());
        }
    }

    /**
     * This method is used to update the game's winner.
     * @param game game's name
     * @param winner game's winner
     */
    public void updateGame(String game, String winner) {
        try {
            MongoCollection<Document> collection = database.getCollection("Game");
            Document gameToUpdate = collection.find(new Document("name", game)).first();
            gameToUpdate.append("winner", winner);
            collection.replaceOne(new Document("name", game), gameToUpdate);
            
        } catch (MongoException e) {
            System.err.println("MongoDB : Erreur lors de la mise à jour du jeu : " + e.getMessage());
        }
    }
    
    /**
     * This method is used to update a round's winner.
     * @param game game's name
     * @param round_number round's number
     * @param winner round's winner
     */
    public void updateRound(String game, int round_number, String winner) {
        try {
            MongoCollection<Document> collection = database.getCollection("Round");
            Document roundToUpdate = collection.find(new Document("game", game).append("round_number", round_number)).first();
            roundToUpdate.append("winner", winner);
            collection.replaceOne(new Document("game", game).append("round_number", round_number), roundToUpdate);
            
        } catch (MongoException e) {
            System.err.println("MongoDB : Erreur lors de la mise à jour du round : " + e.getMessage());
        }
    }

    /**
     * This method is used to get the game's name.
     * @param name game's name
     * @return the game's name
     */
    public boolean isAvailableName(String name) {
        try {
            MongoCollection<Document> collection = database.getCollection("Game");
            if(collection.find(new Document("name", name)).first() != null) {
                return false;
            } else {
                return true;
            }
        } catch (MongoException e) {
            System.err.println("MongoDB : Erreur lors de la vérification du nom : " + e.getMessage());
            return false;
        }
    }

    public void dropTable() {
        try {
            MongoCollection<Document> collection = database.getCollection("Game");
            collection.drop();
            collection = database.getCollection("Round");
            collection.drop();
            collection = database.getCollection("Play");
            collection.drop();
            collection = database.getCollection("Player");
            collection.drop();
        } catch (MongoException e) {
            System.err.println("MongoDB : Erreur lors de la suppression des tables : " + e.getMessage());
        }
    }
}