package com.rakyow.punto;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game {

    // Le tableau de jeu
    private Board board;

    // joueurs de la partie
    private List<Player> players;

    // cartes du jeu
    private List<Card> cards;

    // joueur qui doit jouer
    private Player currentPlayer;

    // numéro du round
    private int round;

    private Scanner scanner;

    // constructeur de la partie, avec comme paramètre le nombre de joueurs
    public Game(int nbPlayers) {
        this.board = new Board();
        this.cards = createCards();
        this.scanner = new Scanner(System.in);
        this.createPlayers();
        this.currentPlayer = pickRandomPlayer();
        this.round = 1;
        start();
    }

    public void start() {
        while (maxRoundWin() < 2) {
            System.out.println("Round " + round);
            startDistribution();
            while (!board.isFull()&&!this.board.isWin(this.players.size())) {
                board.printBoardWithTab();
                play();
                this.board.createCardsPlayable();

            }
            round++;
        }
    }

    public void play() {
        // attend que le joueur rentre la case où il veut jouer
       
        System.out.println("Joueur " + currentPlayer.getName() + " a vous de jouer :");
        
        Card cardChoose = this.currentPlayer.randomCard();

        boolean played = false;
        while(!played) {
            System.out.println("Voici la carte que vous avez pioche : " + cardChoose.printCard());     
            System.out.println("Entrez la case ou vous voulez jouer :");      
            System.out.println("Entrez la ligne :");
            int posY = this.scanner.nextInt();
            System.out.println("Entrez la colonne :");
            int posX = this.scanner.nextInt();
        
            played = this.board.playCards(cardChoose, posX, posY);
        }
        this.currentPlayer = this.players.get((this.players.indexOf(this.currentPlayer) + 1) % this.players.size());
    }

    public int maxRoundWin() {
        int maxRound = 0;
        for (Player player : players) {
            if (player.getRound() > maxRound) {
                maxRound = player.getRound();
            }
        }
        return maxRound;
    }

    // créer les cartes du jeu de punto
    public List<Card> createCards() {
        List<Card> cards = new ArrayList<>();

        Color[] colors = {Color.VERT, Color.ROUGE, Color.BLEU, Color.ORANGE};

        // une carte possède une couleur et une valeur et il y a toujours deux cartes identiques

        for (Color color : colors) {
            for (int i = 1; i <= 10; i++) {
                cards.add(new Card(i, color));
                cards.add(new Card(i, color));
            }
        }
        return cards;
    }

    public void createPlayers() {
        this.players = new ArrayList<>();
        String[] names = {"Joueur 1", "Joueur 2", "Joueur 3", "Joueur 4"};

        // on demande le nombre de joueurs
        
        System.out.println("Entrez le nombre de joueurs :");
        int nbPlayers = this.scanner.nextInt();
        // on ferme le scanner
        
        // on crée le nombre de joueurs demandé
        for (int i = 0; i < nbPlayers; i++) {
            this.players.add(new Player(names[i]));
        }
        System.out.println("Il y a " + nbPlayers + " joueurs");
    }

    // distribution des cartes
    public void startDistribution() {
        int nbPlayers = players.size();
        if (nbPlayers == 2) {
            distributeCardsTwoPlayers();
        }
    }

    // distribue les cartes à deux joueurs
    public void distributeCardsTwoPlayers() {
        // donne les 36 premières cartes au joueur 1 et les 36 dernières au joueur 2
        players.get(0).setCards(cards.subList(0, 36));
        players.get(1).setCards(cards.subList(36, 72));
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
