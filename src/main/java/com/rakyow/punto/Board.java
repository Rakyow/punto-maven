package com.rakyow.punto;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to represent the board of the game.
 */
public class Board {

  
    private List<Card> cardsPlayed; // List of cards played on the board.

    private ArrayList<Card> cardsPlayable; // List of cards playable on the board. 

    private static final int BOARD_SIZE = 11; // Size of the board.

    private Card[][] board; // Array of cards representing the board.

    /**
     * This constructor is used to create a Board.
     */
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

    /**
     * This method is used to print the board.
     */
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

    /**
     * This method is used to print the board.
     */
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

    /**
     * This method is used to create the cards playable on the board.
     */
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

    /**
     * This method is used to check if a card is playable on the board.
     * @param x The x coordinate of the card.
     * @param y The y coordinate of the card.
     * @return true if the card is playable, false otherwise.
     */
    public boolean isPlayable(int x, int y) {
        boolean ret = false;

        for (Card card : this.cardsPlayable) {
            if (card.getX() == x && card.getY() == y) {
                ret = true;
            }
        }
        return ret;
    }

    /**
     * This method is used to add a card to the list of cards playable on the board.
     * @param card The card to add.
     */
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

    /**
     * This method is used to add a card to the list of cards played on the board.
     * @param card The card to add.
     * @param x The x coordinate of the card.
     * @param y The y coordinate of the card.
     */
    public void addCardPlayed(Card card, int x, int y) {
        card.setX(x);
        card.setY(y);
        this.cardsPlayed.add(card);
    }

    /**
     * This method is used to check if the board is full.
     * @return true if the board is full, false otherwise.
     */
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


    /**
     * This method is used to check if a player has won
     * @param nbPlayers The number of players.
     * @return true if a player has won, false otherwise.
     */
    public boolean isWin(int nbPlayers) {

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

    
    /**
     * This method is used to check if a player has won in diagonal.
     * @param nbCardsToWin The number of cards to win.
     * @return true if the player has won in diagonal, false otherwise.
     */
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

    /**
     * This method is used to check if a player has won in column.
     * @param nbCardsToWin The number of cards to win.
     * @return true if the player has won in column, false otherwise.
     */
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

    /**
     * This method is used to check if a player has won in line.
     * @param nbCardsToWin The number of cards to win.
     * @return true if the player has won in line, false otherwise.
     */
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
