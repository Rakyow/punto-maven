package com.rakyow.punto;
import java.util.ArrayList;
import java.util.List;

public class Board {

    // tableau des cartes jouées
    private List<Card> cardsPlayed;

    private List<Card> cardsPlayable;

    private static final int BOARD_SIZE = 11;

    private Card[][] board;

    public Board() {
        this.cardsPlayed = new ArrayList<Card>();
        this.board = new Card[BOARD_SIZE][BOARD_SIZE];
        this.cardsPlayable = new ArrayList<Card>();
    }

    public void printBoard() {
        String  stringBoard = "------------------------------------------------------------------\n";
        for (int x = 0; x < BOARD_SIZE; x++) {
            for (int y = 0; y < BOARD_SIZE; y++){
                if (board[y][x] == null) {
                    stringBoard += "|   ";
                } else {
                    stringBoard += "| " + board[y][x].printCard();
                }
                stringBoard += " |";
            }
            stringBoard += "\n";
            stringBoard += "------------------------------------------------------------------\n";
        }
        System.out.println(stringBoard);
        
    }

    public void printBoardPlayable() {
        // la meme chose que printBoard mais cela ajoute des X sur les cases non jouables
        String  stringBoard = "------------------------------------------------------------------\n";
        for (int x = 0; x < BOARD_SIZE; x++) {
            for (int y = 0; y < BOARD_SIZE; y++){
                if (board[y][x] == null && !this.isPlayable(x, y)) {
                    stringBoard += "| X ";
                } else {
                    stringBoard += "| " + board[y][x].printCard();
                }
                stringBoard += " |";
            }
            stringBoard += "\n";
            stringBoard += "------------------------------------------------------------------\n";
        }
        System.out.println(stringBoard);
    }

    public void printBoardWithTab() {
        
        String[][] stringBoard = new String[BOARD_SIZE][BOARD_SIZE];

        for (int x = 0; x < BOARD_SIZE; x++) {
            for (int y = 0; y < BOARD_SIZE; y++){
                if (this.board[y][x] == null) {
                    if(this.isPlayable(y, x)) {
                        stringBoard[y][x] = "O";
                    } else {
                        stringBoard[y][x] = "X";
                    }
                } else {
                    stringBoard[y][x] = board[y][x].printCard();
                }
            }
        }

        for (int x = 0; x < BOARD_SIZE; x++) {
            System.out.println("------------------------------------------------------------------");
            for (int y = 0; y < BOARD_SIZE; y++){
                System.out.print("| " + stringBoard[x][y] + " ");
            }
            System.out.println("|");
        }

        //affichage du tableau this.cardsPlayable
        System.out.println("this.cardsPlayable :"+ this.cardsPlayable.size());
        for (Card card : this.cardsPlayable) {
            System.out.println(card.printCardWithCoord());
        }
    }

    public boolean playCards(Card card, int x, int y) {

        boolean ret = true;

        if (this.board[x][y] == null) {
            
            this.board[x][y] = card;
            this.addCardPlayed(card, x, y);
            
        } else if(this.board[x][y].getValue() < card.getValue()) {

            this.board[x][y] = card;
            this.addCardPlayed(card, x, y);

        } else {
            System.out.println("Vous ne pouvez pas jouer cette carte ici");
            ret = false;
        }
        return ret;

    }

    // en fonction des cartes jouées sur le plateau, on créer un tableau des cases jouables
    public void createCardsPlayable() {
        // on ajoute les cartes jouable en fonction des cartes jouées
        // de sorte à ce que les cases jouables soient les cases adjacentes aux cartes jouées
        // que se soit en haut, en bas, à gauche ou à droite, mais aussi en diagonale
        this.cardsPlayable.clear();
        for (Card card : this.cardsPlayed) {
            int x = card.getX();
            int y = card.getY();
            if (x > 0) {
                this.addCardPlayable(new Card(0, Color.NONE, x - 1, y));

            }
            if (x < BOARD_SIZE - 1) {
                this.addCardPlayable(new Card(0, Color.NONE, x + 1, y));
            }
            if (y > 0) {
                this.addCardPlayable(new Card(0, Color.NONE, x, y - 1));
            }
            if (y < BOARD_SIZE - 1) {
                this.addCardPlayable(new Card(0, Color.NONE, x, y + 1));
            }
            if (x > 0 && y > 0) {
                this.addCardPlayable(new Card(0, Color.NONE, x - 1, y - 1));
            }
            if (x < BOARD_SIZE - 1 && y < BOARD_SIZE - 1) {
                this.addCardPlayable(new Card(0, Color.NONE, x + 1, y + 1));
            }
            if (x > 0 && y < BOARD_SIZE - 1) {
                this.addCardPlayable(new Card(0, Color.NONE, x - 1, y + 1));
            }
            if (x < BOARD_SIZE - 1 && y > 0) {
                this.addCardPlayable(new Card(0, Color.NONE, x + 1, y - 1));
            }
        }
    }

    public boolean isPlayable(int x, int y) {
        boolean ret = false;

        for (Card card : this.cardsPlayable) {
            if (card.getX() == x && card.getY() == y) {
                ret = true;
            }
        }
        return ret;
    }

    public void addCardPlayable(Card card) {
        // recherche dans le tableau si la carte n'y est pas déjà
        boolean found = false;
        for (Card cardPlayable : this.cardsPlayable) {
            System.out.println("Coord : "+cardPlayable.getX()+" | "+card.getX()+" / "+cardPlayable.getY()+" | "+card.getY());
            if (cardPlayable.getX() == card.getX() && cardPlayable.getY() == card.getY()) {
                found = true;
            }
        }

        if (!found) {
            this.cardsPlayable.add(card);
        }

    }

    public void addCardPlayed(Card card, int x, int y) {
        card.setX(x);
        card.setY(y);
        this.cardsPlayed.add(card);
        System.out.println("AAAAA"+card.getX()+"/"+card.getY());
    }

    public boolean isFull() {
        boolean ret = true;
        for (int x = 0; x < BOARD_SIZE; x++) {
            for (int y = 0; y < BOARD_SIZE; y++){
                if (board[y][x] == null) {
                    ret = false;
                }
            }
        }
        return ret;
    }

    public boolean isWin(int nbPlayers) {
        //si le nombre de joueurs est de 2 alors il suffit d'aligner 5 cartes
        //si le nombre de joueurs est de 3 ou 4 alors il suffit d'aligner 4 cartes
        boolean win = false;

        int nbCardsToWin = 5;

        if (nbPlayers > 2) {
            nbCardsToWin = 4;
        }

        // if (this.isWinInColumn(nbCardsToWin) || this.isWinInLine(nbCardsToWin) || this.isWinInDiagonal(nbCardsToWin)) {
        //     win = true;
        // }

        if (this.isWinInLine(nbCardsToWin)) {
            win = true;
            
        }

        return win;
    }

    boolean isWinInLine(int nbCardsToWin) {
        boolean win = false;

        for (Card card : this.cardsPlayed) {
            int x = card.getX();
            int y = card.getY();

            // on vérifie si on peut gagner en ligne
            int nbCards = 1;
            for (int i = 1; i < nbCardsToWin; i++) {
                if (x + i < BOARD_SIZE && this.board[x + i][y] != null && this.board[x + i][y].getColor() == card.getColor()) {
                    nbCards++;
                }
            }
            if (nbCards == nbCardsToWin) {
                win = true;
            }
        }

        return win;
    }

}
