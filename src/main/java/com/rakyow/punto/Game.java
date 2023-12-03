package com.rakyow.punto;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.rakyow.database.ConnexionMongoDB;
import com.rakyow.database.ConnexionSQLite;
import com.rakyow.database.ConnexionMySQL;

public class Game {

    private String gameName = "Punto";

    // Le tableau de jeu
    private Board board;

    // joueurs de la partie
    private List<Player> players;

    // cartes du jeu
    private ArrayList<Card> cards;

    // joueur qui doit jouer
    private Player currentPlayer;

    // numéro du round
    private int round;

    private Scanner scanner;

    ConnexionSQLite sqliteConnexion;
    ConnexionMySQL mysqlConnexion;
    ConnexionMongoDB mongoConnexion;

    public Game() {
        createConnexion();
        this.cards = createCards();
        this.scanner = new Scanner(System.in);
        this.createPlayers(0);
        this.currentPlayer = null;
        this.round = 1;
        start();
    }

    // constructeur de la partie, avec comme paramètre le nombre de joueurs
    public Game(int nbPlayers) {
        createConnexion();
        this.cards = createCards();
        this.scanner = new Scanner(System.in);
        this.createPlayers(nbPlayers);
        this.currentPlayer = null;
        this.round = 1;
        start();
    }

    private void createConnexion() {
        this.sqliteConnexion = new ConnexionSQLite();
        this.mongoConnexion = new ConnexionMongoDB();
        this.mysqlConnexion = new ConnexionMySQL();
    }

    private void closeConnexion() {
        this.sqliteConnexion.closeConnection();
        this.mongoConnexion.closeConnection();
        this.mysqlConnexion.closeConnection();
    }

    private void addGame() {
        this.sqliteConnexion.addGame();
        this.mongoConnexion.addGame();
        this.mysqlConnexion.addGame();
    }

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
                } else {
                    this.currentPlayer.addRound();
                    this.sqliteConnexion.updateRound(this.gameName, this.round, this.currentPlayer.getName());
                }
            }
            round++;
        }
        this.sqliteConnexion.updateGame(this.gameName, this.currentPlayer.getName());
        closeConnexion();
    }

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

    public void startRound() {

        this.board = new Board();
        this.sqliteConnexion.addRound(this.gameName, this.round);
        startDistribution();
        Player randomPlayer = pickRandomPlayer();
        System.out.println("Round " + round);
        System.out.println("Le joueur qui commence est " + randomPlayer.getName());
        this.currentPlayer = randomPlayer;
        firstPlay();
    }

    // au début d'une manche le joueur doit forcément jouer sur la case du milieu
    private void firstPlay() {
        Card cardChoose = this.currentPlayer.randomCard();
        int x = 5;
        int y = 5;
        this.board.playCards(cardChoose, x, y);
        this.board.createCardsPlayable();
        this.currentPlayer = this.players.get((this.players.indexOf(this.currentPlayer) + 1) % this.players.size());
        this.sqliteConnexion.addPlay(this.gameName, this.round, this.currentPlayer.getName(), cardChoose.getValue(), cardChoose.getColor().toString(), x, y);
            }
    public void play() {
        // attend que le joueur rentre la case où il veut jouer
       
        System.out.println("Joueur " + currentPlayer.getName() + " a vous de jouer :");
        
        Card cardChoose = this.currentPlayer.randomCard();

        boolean played = false;
        while(!played) {
            System.out.println("Voici la carte que vous avez pioche : " + cardChoose.printCard());          
            System.out.println("Entrez la ligne :");
            int posX = input();
            System.out.println("Entrez la colonne :");
            int posY = input();
        
            played = this.board.playCards(cardChoose, posX, posY);
            if(played) {
                this.sqliteConnexion.addPlay(this.gameName, this.round, this.currentPlayer.getName(), cardChoose.getValue(), cardChoose.getColor().toString(), posX, posY);
            } 
        }
        this.board.createCardsPlayable();
    }

    private int input() {
        int pos = -1;

        while (true) {
            if (this.scanner.hasNextInt()) {
                pos = this.scanner.nextInt();
                if (pos >= 0 && pos <= 9) {
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

    public int maxRoundWin() {
        int maxRound = 0;
        for (Player player : players) {
            System.out.println(player.getName() + " a gagné " + player.getRound() + " manches");
            if (player.getRound() > maxRound) {
                maxRound = player.getRound();
            }
        }

        return maxRound;
    }

    // créer les cartes du jeu de punto
    public ArrayList<Card> createCards() {
        ArrayList<Card> cards = new ArrayList<>();

        Color[] colors = {Color.VERT, Color.ROUGE, Color.BLEU, Color.ORANGE};

        // une carte possède une couleur et une valeur et il y a toujours deux cartes identiques

        for (Color color : colors) {
            for (int i = 1; i < 10; i++) {
                cards.add(new Card(i, color));
                cards.add(new Card(i, color));
            }
        }
        return cards;
    }

    public void createPlayers(int nbPlayers) {
        this.players = new ArrayList<>();
        String[] names = {"Joueur 1", "Joueur 2", "Joueur 3", "Joueur 4"};

        // on demande le nombre de joueurs
        if(nbPlayers == 0) {
            System.out.println("Entrez le nombre de joueurs :");
            nbPlayers = this.scanner.nextInt();
        }
        
        // on crée le nombre de joueurs demandé
        for (int i = 0; i < nbPlayers; i++) {
            this.players.add(new Player(names[i]));
            this.sqliteConnexion.addPlayer(names[i]);
        }

    }

    // distribution des cartes
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

    private void removeCardForWinningRound() {
        // on retire une carte au hasard pour chaque manche gagné par le joueur
        for (Player player : players) {
            for (int i = 0; i < player.getRound(); i++) {
                player.getCards().remove((int) (Math.random() * player.getCards().size()));
            }
        }
    }

    // distribue les cartes à deux joueurs
    public void distributeCardsTwoPlayers() {
        
        for (int i = 0; i < 36; i++) {
            players.get(0).addCard(cards.get(i));
            players.get(1).addCard(cards.get(i + 36));
        }

    }

    // distribue les cartes à trois joueurs
    public void distributeCardsThreePlayers() {
        for (int i = 0; i < 18; i++) {
            players.get(0).addCard(cards.get(i));
            players.get(1).addCard(cards.get(i + 18));
            players.get(2).addCard(cards.get(i + 36));
        }
    }

    // distribue les cartes à quatre joueurs
    public void distributeCardsFourPlayers() {
        for (int i = 0; i < 18; i++) {
            players.get(0).addCard(cards.get(i));
            players.get(1).addCard(cards.get(i + 18));
            players.get(2).addCard(cards.get(i + 36));
            players.get(3).addCard(cards.get(i + 54));
        }
    }

    // retourne deux couleurs au hasard
    public Color[] pickRandomColors() {
        Color[] colors = {Color.VERT, Color.ROUGE, Color.BLEU, Color.ORANGE};
        Color[] randomColors = new Color[2];

        for (int i = 0; i < 2; i++) {
            Color randomColor = colors[(int) (Math.random() * colors.length)];
            randomColors[i] = randomColor;
        }
        return randomColors;
    }

    // retourne un joueur au hasard pour commencer la partie
    public Player pickRandomPlayer() {
        return players.get((int) (Math.random() * players.size()));
    }
}
