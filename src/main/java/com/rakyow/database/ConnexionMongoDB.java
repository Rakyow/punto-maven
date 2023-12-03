package com.rakyow.database;

import com.mongodb.MongoException;
import com.mongodb.client.*;

import java.util.ArrayList;

import org.bson.Document;

public class ConnexionMongoDB {
    private MongoClient mongoClient;
    private MongoDatabase database;

    public ConnexionMongoDB() {
        String connectionString = "mongodb://localhost:27017"; // Remplacez par votre URL de connexion
        String databaseName = "punto"; // Remplacez par le nom de votre base de données

        try {
            mongoClient = MongoClients.create(connectionString);
            database = mongoClient.getDatabase(databaseName);
            System.out.println("MongoDB : Connexion établie !");
            createCollections();
        } catch (Exception e) {
            System.out.println("MongoDB : Connexion échoué !");
            e.printStackTrace();
        }
    }

    private void createCollections() {
        createCollection("Game");
        createCollection("Round");
        createCollection("Play");
        createCollection("Player");
    }

    private void createCollection(String collectionName) {
        if (!database.listCollectionNames().into(new ArrayList<>()).contains(collectionName)) {
            database.createCollection(collectionName);
            System.out.println("MongoDB : Collection " + collectionName + " créée avec succès !");
        } else {
            System.out.println("MongoDB : La collection " + collectionName + " existe déjà !");
        }
    }

    public void closeConnection() {
        mongoClient.close();
    }

    public void addGame(String name) {
        try {
            MongoCollection<Document> collection = database.getCollection("Game");
            Document gameObject = new Document("name", name);
            collection.insertOne(gameObject);
            System.out.println("MongoDB : Game ajouté avec succès !");
        } catch (MongoException e) {
            System.out.println("MongoDB : Erreur lors de l'ajout du jeu : " + e.getMessage());
        }
    }

    public void addPlay(String game, int round_number, String player, int score, String color, int coordX, int coordY) {
        try {
            MongoCollection<Document> collection = database.getCollection("Play");
            Document play = new Document("game", game).append("round_number", round_number).append("player", player).append("score", score).append("color", color).append("coordX", coordX).append("coordY", coordY);
            collection.insertOne(play);
            System.out.println("MongoDB : Play ajouté avec succès !");
        } catch (MongoException e) {
            System.out.println("MongoDB : Erreur lors de l'ajout du play : " + e.getMessage());
        }
    }

    public void addPlayer(String name) {
        try {

            MongoCollection<Document> collection = database.getCollection("Player");
            if(collection.find(new Document("name", name)).first() != null) {
                System.out.println("MongoDB : Le joueur existe déjà !");
                return;
            } else {
                Document player = new Document("name", name);
                collection.insertOne(player);
                System.out.println("MongoDB : Player ajouté avec succès !");
            }
        } catch (MongoException e) {
            System.out.println("MongoDB : Erreur lors de l'ajout du joueur : " + e.getMessage());
        }
    }

    public void addRound(String game, int round_number) {
        try {
            MongoCollection<Document> collection = database.getCollection("Round");
            Document round = new Document("game", game).append("round_number", round_number);
            collection.insertOne(round);
            System.out.println("MongoDB : Round ajouté avec succès !");
        } catch (MongoException e) {
            System.out.println("MongoDB : Erreur lors de l'ajout du round : " + e.getMessage());
        }
    }
    public void updateGame(String game, String winner) {
        try {
            MongoCollection<Document> collection = database.getCollection("Game");
            Document gameToUpdate = collection.find(new Document("name", game)).first();
            gameToUpdate.append("winner", winner);
            collection.replaceOne(new Document("name", game), gameToUpdate);
            System.out.println("MongoDB : Game mis à jour avec succès !");
        } catch (MongoException e) {
            System.out.println("MongoDB : Erreur lors de la mise à jour du jeu : " + e.getMessage());
        }
    }
    
    public void updateRound(String game, int round_number, String winner) {
        try {
            MongoCollection<Document> collection = database.getCollection("Round");
            Document roundToUpdate = collection.find(new Document("game", game).append("round_number", round_number)).first();
            roundToUpdate.append("winner", winner);
            collection.replaceOne(new Document("game", game).append("round_number", round_number), roundToUpdate);
            System.out.println("MongoDB : Round mis à jour avec succès !");
        } catch (MongoException e) {
            System.out.println("MongoDB : Erreur lors de la mise à jour du round : " + e.getMessage());
        }
    }

    public boolean isAvailableName(String name) {
        try {
            MongoCollection<Document> collection = database.getCollection("Game");
            if(collection.find(new Document("name", name)).first() != null) {
                return false;
            } else {
                return true;
            }
        } catch (MongoException e) {
            System.out.println("MongoDB : Erreur lors de la vérification du nom : " + e.getMessage());
            return false;
        }
    }
}