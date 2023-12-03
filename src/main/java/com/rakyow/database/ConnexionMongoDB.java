package com.rakyow.database;

import com.mongodb.MongoException;
import com.mongodb.client.*;

import java.sql.SQLException;
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

    public void addGame() {
        try {
            MongoCollection<Document> collection = database.getCollection("Game");
            String gameName = generateName();
            Document game = new Document("name", gameName);
            // on vérifie que le nom du jeu n'existe pas déjà
            while(collection.find(game).first() != null) {
                gameName = generateName();
                game = new Document("name", gameName);
            }
            collection.insertOne(game);
            System.out.println("MongoDB : Game ajouté avec succès !");
        } catch (MongoException e) {
            System.out.println("MongoDB : Erreur lors de l'ajout du jeu : " + e.getMessage());
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