package com.rakyow.punto;
import java.util.ArrayList;
import java.util.List;

public class Board {

    // tableau des cartes jouées
    private List<Card> cardsPlayed;

    private ArrayList<Card> cardsPlayable;

    private static final int BOARD_SIZE = 11;

    private Card[][] board;

    public Board() {
        this.cardsPlayed = new ArrayList<Card>();
        this.board = new Card[BOARD_SIZE][BOARD_SIZE];
        // ON REMPLIT LE L'ArrayList carsPlayable de carte de valeur 0 et de couleur NONE
        this.cardsPlayable = new ArrayList<Card>();
        for (int x = 0; x < BOARD_SIZE; x++) {
            for (int y = 0; y < BOARD_SIZE; y++){
                this.cardsPlayable.add(new Card(0, Color.NONE, x, y));
            }
        }

    }

    public void printBoardWithTab() {
        
        String[][] stringBoard = new String[BOARD_SIZE][BOARD_SIZE];

        for (int x = 0; x < BOARD_SIZE; x++) {
            for (int y = 0; y < BOARD_SIZE; y++){
                if (this.board[y][x] == null) {
                    if(this.isPlayable(y, x)) {
                        stringBoard[y][x] = "O ";
                    } else {
                        stringBoard[y][x] = "X ";
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
    }

    public boolean playCards(Card card, int x, int y) {

        boolean ret = false;
        if(isPlayable(x, y)) {
            if (this.board[x][y] == null) {
            
                this.board[x][y] = card;
                this.addCardPlayed(card, x, y);
                ret = true;
                
            } else if(this.board[x][y].getValue() < card.getValue()) {

                this.board[x][y] = card;
                this.addCardPlayed(card, x, y);
                ret = true;

            } 
        } else {
            System.out.println("Vous ne pouvez pas jouer cette carte ici");
        }
        return ret;

    }

    // en fonction des cartes jouées sur le plateau, on créer un tableau des cases jouables autour des cartes jouées mais aussi sur les cases jouées
    public void createCardsPlayable() {
        
        this.cardsPlayable.clear();
        this.cardsPlayable.add(this.cardsPlayed.get(0));
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

        ArrayList<Card> filteredPlayableCards = new ArrayList<Card>(this.cardsPlayable);

        int maxBottom = 5;
        int maxTop = 5;
        int maxLeft = 5;
        int maxRight = 5;


        for (Card cardPlayed : this.cardsPlayed) {

            if (cardPlayed.getX() > maxBottom) {
                maxBottom = cardPlayed.getX();
            }

            if (cardPlayed.getX() < maxTop) {
                maxTop = cardPlayed.getX();
            }

            if (cardPlayed.getY() > maxRight) {
                maxRight = cardPlayed.getY();
            }

            if (cardPlayed.getY() < maxLeft) {
                maxLeft = cardPlayed.getY();
            }
        }
        

        int diffX = maxBottom - maxTop;
        int diffY = maxRight - maxLeft;

            if (diffX == 5 || diffX == -5) {
                for (Card cardPlayable : this.cardsPlayable) {
                    if (cardPlayable.getX() < maxTop || cardPlayable.getX() > maxBottom) {
                        filteredPlayableCards.remove(cardPlayable);
                    }
                }
            }

            if (diffY == 5 || diffY == -5) {
                for (Card cardPlayable : this.cardsPlayable) {
                    if (cardPlayable.getY() > maxRight || cardPlayable.getY() < maxLeft) {
                        filteredPlayableCards.remove(cardPlayable);
                    }
                }
            }

        this.cardsPlayable = filteredPlayableCards;
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

        boolean found = false;
        for (Card cardPlayable : this.cardsPlayable) {
            
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

        if (this.isWinInLine(nbCardsToWin) || this.isWinInColumn(nbCardsToWin) || this.isWinInDiagonal(nbCardsToWin)) {
            win = true;
            
        }

        System.out.println("win : " + win);

        return win;
    }

    boolean isWinInDiagonal(int nbCardsToWin) {
        boolean win = false;

        for (Card card : this.cardsPlayed) {
            int x = card.getX();
            int y = card.getY();

            // on vérifie si on peut gagner en diagonale
            int nbCards = 1;
            for (int i = 1; i < nbCardsToWin; i++) {
                if (x + i < BOARD_SIZE && y + i < BOARD_SIZE && this.board[x + i][y + i] != null && this.board[x + i][y + i].getColor() == card.getColor()) {
                    nbCards++;
                }
            }
            if (nbCards == nbCardsToWin) {
                win = true;
                System.out.println("win en diagonale");
            }
        }

        return win;
    }
    boolean isWinInColumn(int nbCardsToWin) {
        boolean win = false;

        for (Card card : this.cardsPlayed) {
            int x = card.getX();
            int y = card.getY();

            // on vérifie si on peut gagner en colonne
            int nbCards = 1;
            for (int i = 1; i < nbCardsToWin; i++) {
                if (y + i < BOARD_SIZE && this.board[x][y + i] != null && this.board[x][y + i].getColor() == card.getColor()) {
                    nbCards++;
                }
            }
            if (nbCards == nbCardsToWin) {
                win = true;
                System.out.println("win en colonne");
            }
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
                System.out.println("win en ligne");
            }
        }

        return win;
    }

}
