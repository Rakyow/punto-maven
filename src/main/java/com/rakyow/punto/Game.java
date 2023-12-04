package com.rakyow.punto;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.rakyow.database.ConnexionMongoDB;
import com.rakyow.database.ConnexionSQLite;
import com.rakyow.database.ConnexionMySQL;

/**
 * This class is used to represent a game.
 */
public class Game {

    private String gameName = "Punto"; // game's name

    private Board board; // game's board

    private List<Player> players; // game's players

    private ArrayList<Card> cards; // game's cards

    private Player currentPlayer; // current player

    private int round; // current round

    private Scanner scanner; // Scanner used to get the user input.

    ConnexionSQLite sqliteConnexion; // connexion to SQLite database
    ConnexionMySQL mysqlConnexion; // connexion to MySQL database
    ConnexionMongoDB mongoConnexion; // connexion to MongoDB database

    /**
     * This constructor is used to create a game.
     * @param sc Scanner used to get the user input.
     */
    public Game(Scanner sc) {
        createConnexion();
        this.cards = createCards();
        this.scanner = sc;
        this.createPlayers(0);
        this.currentPlayer = null;
        this.round = 1;
    }

    /**
     * This constructor is used to create a game.
     * @param nbPlayers number of players
     * @param sc Scanner used to get the user input.
     */
    public Game(int nbPlayers, Scanner sc) {
        createConnexion();
        this.cards = createCards();
        this.scanner = sc;
        this.createPlayers(nbPlayers);
        this.currentPlayer = null;
        this.round = 1;
    }

    /**
     * This method is used to create all the connexions to the databases.
     */
    private void createConnexion() {
        this.sqliteConnexion = new ConnexionSQLite();
        this.mongoConnexion = new ConnexionMongoDB();
        this.mysqlConnexion = new ConnexionMySQL();
    }

    /**
     * This method is used to close all the connexions to the databases.
     */
    private void closeConnexion() {
        this.sqliteConnexion.closeConnection();
        this.mongoConnexion.closeConnection();
        this.mysqlConnexion.closeConnection();
    }

    /**
     * This method is used to add a game to the databases.
     */
    private void addGame() {
        this.gameName = generateName();
        
        while(!isAvailableName(this.gameName)) {
            this.gameName = generateName();
        }

        this.sqliteConnexion.addGame(gameName);
        this.mongoConnexion.addGame(gameName);
        this.mysqlConnexion.addGame(gameName);
    }

    /**
     * This method is used to check if the name of the game is available.
     * @param name name of the game
     * @return true if the name is available, false otherwise
     */
    private boolean isAvailableName(String name) {
        return this.sqliteConnexion.isAvailableName(name) && this.mongoConnexion.isAvailableName(name) && this.mysqlConnexion.isAvailableName(name);
    }

    /**
     * This method is used to add a round to the databases.
     */
    private void addRound() {
        this.sqliteConnexion.addRound(this.gameName, this.round);
        this.mongoConnexion.addRound(this.gameName, this.round);
        this.mysqlConnexion.addRound(this.gameName, this.round);
    }

    /**
     * This method is used to add a player to the databases.
     * @param name name of the player
     */
    private void addPlayer(String name) {
        this.sqliteConnexion.addPlayer(name);
        this.mongoConnexion.addPlayer(name);
        this.mysqlConnexion.addPlayer(name);
    }

    /**
     * This method is used to add a play to the databases.
     * @param game name of the game
     * @param score score of the play
     * @param color color of the play
     * @param coordX x coordinate of the play
     * @param coordY y coordinate of the play
     */
    private void addPlay(String game, int score, String color, int coordX, int coordY) {
        String playerName = this.currentPlayer.getName();
        this.sqliteConnexion.addPlay(game, this.round, playerName, score, color, coordX, coordY);
        this.mongoConnexion.addPlay(game, this.round, playerName, score, color, coordX, coordY);
        this.mysqlConnexion.addPlay(game, this.round, playerName, score, color, coordX, coordY);
    }

    /**
     * This method is used to update the game in the databases.
     */
    private void updateGame() {
        String playerName = this.currentPlayer.getName();
        this.sqliteConnexion.updateGame(this.gameName, playerName);
        this.mongoConnexion.updateGame(this.gameName, playerName);
        this.mysqlConnexion.updateGame(this.gameName, playerName);
    }
    
    /**
     * This method is used to update the round in the databases.
     */
    private void updateRound() {
        String playerName = this.currentPlayer.getName();
        this.sqliteConnexion.updateRound(this.gameName, this.round, playerName);
        this.mongoConnexion.updateRound(this.gameName, this.round, playerName);
        this.mysqlConnexion.updateRound(this.gameName, this.round, playerName);
    }

    /**
     * This method is used to start the game.
     */
    public void start() {
        addGame();
        while (maxRoundWin() < 2) {
            startRound();
            int win = -1;
            while (win == -1) {
                board.printBoardWithTab();
                play();
                win = isFinish();
                if(win == -1) {
                    this.currentPlayer = this.players.get((this.players.indexOf(this.currentPlayer) + 1) % this.players.size());
                } else if(win == 0) {
                    System.out.println("\n----------------------------------\n");
                    System.out.println("Egalité !");
                    System.out.println("\n----------------------------------\n");
                } else if(win == 2) {
                    System.out.println("\n----------------------------------\n");
                    System.out.println("Le plateau est plein !");
                    System.out.println("Le joueur ayant le moins de points gagne la manche !");
                    Player minPlayer = this.players.get(0);
                    for (Player player : players) {
                        
                        if (player.getScore() < minPlayer.getScore()) {
                            minPlayer = player;
                        }
                    }
                    // on vérifie si il y a égalité
                    int nbMinPlayer = 0;
                    for (Player player : players) {
                        
                        if (player.getScore() == minPlayer.getScore()) {
                            nbMinPlayer++;
                        }
                    }
                    if (nbMinPlayer > 1) {
                        System.out.println("Egalité !");
                    } else {
                       minPlayer.addRound();
                       this.updateRound();  
                    }
                } else {
                    this.currentPlayer.addRound();
                    this.updateRound();   
                }
                
            }
            round++;
        }
        System.out.println("\n----------------------------------\n");
        System.out.println("Felicitation a " + this.currentPlayer.getName()+ " qui a gagné la partie !");
        System.out.println("\n----------------------------------\n");
        this.updateGame();
    }

    /**
     * This method is used to check if the game is finished.
     * @return 0 if a player has won, 1 if the board is full, 2 otherwise
     */
    private int isFinish() {
        int type = -1;
        if (this.currentPlayer.getCards().size() == 0) {
            type = 0;
        } else if (this.board.isFull()) {
            type = 2;
        }   else if (this.board.isWin(this.players.size())) {
            type = 1;
        }

        return type;
    }

    /**
     * This method is used to start a round.
     */
    public void startRound() {

        this.board = new Board();
        for(Player player : this.players) {
            player.resetScore();
        }
        this.addRound();
        startDistribution();
        Player randomPlayer = pickRandomPlayer();
        System.out.println("Round " + round);
        System.out.println("Le joueur qui commence est " + randomPlayer.getName());
        this.currentPlayer = randomPlayer;
        firstPlay();
    }

    /**
     * This method is used to start the first play of the round.
     */
    private void firstPlay() {
        Card cardChoose = this.currentPlayer.randomCard();
        int x = 5;
        int y = 5;
        this.board.playCards(cardChoose, x, y);
        this.board.createCardsPlayable();
        addPlay(this.gameName, cardChoose.getValue(), cardChoose.getColor().toString(), x, y);
        this.currentPlayer = this.players.get((this.players.indexOf(this.currentPlayer) + 1) % this.players.size());
    }

    /**
     * This method is used to play a card.
     */
    public void play() {

        System.out.println("\n----------------------------------\n");
        System.out.println("Joueur " + currentPlayer.getName() + " a vous de jouer :");
        
        Card cardChoose = this.currentPlayer.randomCard();

        boolean played = false;
        while(!played) {
            System.out.println("Voici la carte que vous avez pioche : " + cardChoose.printCard());        
            System.out.println("\n----------------------------------\n");  
            System.out.println("Entrez la ligne :");
            int posX = input();
            System.out.println("Entrez la colonne :");
            int posY = input();
        
            played = this.board.playCards(cardChoose, posX, posY);
            if(played) {
                addPlay(this.gameName, cardChoose.getValue(), cardChoose.getColor().toString(), posX, posY);
            } 
        }
        this.board.createCardsPlayable();
    }

    /**
     * This method is used to get the user input.
     * @return user input
     */
    private int input() {
        int pos = -1;

        while (true) {
            if (this.scanner.hasNextInt()) {
                pos = this.scanner.nextInt();
                if (pos >= 0 && pos < 11) {
                    break; 
                } else {
                    System.out.println("Veuillez entrer un entier entre 0 et 9 :");
                }
            } else {
                System.out.println("Veuillez entrer un entier valide :");
                this.scanner.next(); 
            }
        }
        return pos;
    }

    /**
     * This method is used to get the game's name.
     * @return game's name
     */
    public int maxRoundWin() {
        int maxRound = 0;
        System.out.println("\n----------------------------------\n");
        for (Player player : players) {
            
            System.out.println(player.getName() + " a gagné " + player.getRound() + " manches");
            if (player.getRound() > maxRound) {
                maxRound = player.getRound();
            }
        }
        System.out.println("\n----------------------------------\n");

        return maxRound;
    }

    /**
     * This method is used to create the cards.
     * @return list of cards
     */
    public ArrayList<Card> createCards() {
        ArrayList<Card> cards = new ArrayList<>();

        Color[] colors = {Color.VERT, Color.ROUGE, Color.BLEU, Color.ORANGE};

        for (Color color : colors) {
            for (int i = 1; i < 10; i++) {
                cards.add(new Card(i, color));
                cards.add(new Card(i, color));
            }
        }
        return cards;
    }

    /**
     * This method is used to create the players.
     * @param nbPlayers number of players
     */
    public void createPlayers(int nbPlayers) {
        this.players = new ArrayList<>();
        String[] names = {"Joueur 1", "Joueur 2", "Joueur 3", "Joueur 4"};

        if(nbPlayers == 0) {
            while (true) {
                System.out.println("Veuillez entrer le nombre de joueurs (entre 2 et 4) :");
                if (this.scanner.hasNextInt()) {
                    nbPlayers = this.scanner.nextInt();
                    if (nbPlayers > 1 && nbPlayers < 5) {
                        break; 
                    } 
                } else {
                    System.out.println("Veuillez entrer un entier valide :");
                    this.scanner.next(); 
                }
            }
        }

        for (int i = 0; i < nbPlayers; i++) {
            this.players.add(new Player(names[i]));
            addPlayer(names[i]);
        }

    }

    /**
     * This method is used to start the distribution.
     */
    public void startDistribution() {
        int nbPlayers = players.size();
        if (nbPlayers == 2) {
            distributeCardsTwoPlayers();
        } else if (nbPlayers == 3) {
            distributeCardsThreePlayers();
        } else if (nbPlayers == 4) {
            distributeCardsFourPlayers();
        }
        removeCardForWinningRound();
    }

    /**
     * This method is used to remove cards for the winning round.
     */
    private void removeCardForWinningRound() {
        
        for (Player player : players) {
            for (int i = 0; i < player.getRound(); i++) {
                player.getCards().remove((int) (Math.random() * player.getCards().size()));
            }
        }
    }

    /**
     * This method is used to distribute the cards to two players.
     */
    public void distributeCardsTwoPlayers() {
        
        for (int i = 0; i < 36; i++) {
            players.get(0).addCard(cards.get(i));
            players.get(1).addCard(cards.get(i + 36));
        }

    }

    /**
     * This method is used to distribute the cards to three players.
     */
    public void distributeCardsThreePlayers() {
        for (int i = 0; i < 18; i++) {
            players.get(0).addCard(cards.get(i));
            players.get(1).addCard(cards.get(i + 18));
            players.get(2).addCard(cards.get(i + 36));
        }
    }

    /**
     * This method is used to distribute the cards to four players.
     */
    public void distributeCardsFourPlayers() {
        for (int i = 0; i < 18; i++) {
            players.get(0).addCard(cards.get(i));
            players.get(1).addCard(cards.get(i + 18));
            players.get(2).addCard(cards.get(i + 36));
            players.get(3).addCard(cards.get(i + 54));
        }
    }

    /**
     * This method is used to pick two random colors.
     * @return two random colors
     */
    public Color[] pickRandomColors() {
        Color[] colors = {Color.VERT, Color.ROUGE, Color.BLEU, Color.ORANGE};
        Color[] randomColors = new Color[2];

        for (int i = 0; i < 2; i++) {
            Color randomColor = colors[(int) (Math.random() * colors.length)];
            randomColors[i] = randomColor;
        }
        return randomColors;
    }

    /**
     * This method is used to pick a random player.
     * @return random player
     */
    public Player pickRandomPlayer() {
        return players.get((int) (Math.random() * players.size()));
    }

    /**
     * This method is used to generate a random name for the game.
     * @return random name for the game
     */
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