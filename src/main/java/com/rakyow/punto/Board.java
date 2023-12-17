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
    
        // Remplissage du tableau de chaînes de caractères
        for (int x = 0; x < BOARD_SIZE; x++) {
            for (int y = 0; y < BOARD_SIZE; y++) {
                if (this.board[y][x] == null) {
                    stringBoard[y][x] = this.isPlayable(y, x) ? "○" : "×";
                } else {
                    stringBoard[y][x] = board[y][x].printCard();
                }
            }
        }
    
        // Affichage du plateau de jeu
        for (int x = 0; x < BOARD_SIZE; x++) {
            printHorizontalLine();
            printRowBorder();
            for (int y = 0; y < BOARD_SIZE; y++) {
                System.out.print("║ " + formatSymbol(stringBoard[x][y]) + " ");
            }
            System.out.println("║");
            printRowBorder();
        }
        printHorizontalLine();
    }
    
    // Méthode pour afficher une ligne horizontale de séparation
    private void printHorizontalLine() {
        System.out.print("╔");
        for (int i = 0; i < BOARD_SIZE * 4; i++) {
            System.out.print("═");
        }
        System.out.println("╗");
    }
    
    // Méthode pour afficher la bordure de chaque ligne
    private void printRowBorder() {
        System.out.print("║");
    }
    
    // Méthode pour formater les symboles visuels
    private String formatSymbol(String symbol) {
        return switch (symbol) {
            case "○" -> "\u001B[37m" + symbol + "\u001B[0m"; // Bleu pour les espaces jouables
            case "×" -> "\u001B[30m" + symbol + "\u001B[0m";// Noir pour les espaces non jouables
            default -> symbol;
        };
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
        boolean ret = false;
        
        int nbLigne = 0;

        int x = 0;
        int y = 0;

        while (x < BOARD_SIZE && !ret) {
            boolean ligne = false;
            int nbCards = 0;
            while (y < BOARD_SIZE && !ligne) {
                if (this.board[x][y] == null) {
                    nbCards = 0;
                } else {
                    nbCards++;
                }
                if (nbCards == 6) {
                    ligne = true;
                    nbLigne++;
                }
                y++;
            }
            if (nbLigne == 6) {
                ret = true;
            }
            y = 0;
            x++;
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

        return win;
    }

    
    /**
     * This method is used to check if a player has won in diagonal.
     * @param nbCardsToWin The number of cards to win.
     * @return true if the player has won in diagonal, false otherwise.
     */
    boolean isWinInDiagonal(int nbCardsToWin) {
        return isWinInDiagonalDirection(nbCardsToWin, 1) || isWinInDiagonalDirection(nbCardsToWin, -1);
    }
    
    /**
     * This method is used to check if a player has won in diagonal.
     * @param nbCardsToWin The number of cards to win.
     * @param direction The direction of the diagonal.
     * @return true if the player has won in diagonal, false otherwise.
     */
    private boolean isWinInDiagonalDirection(int nbCardsToWin, int direction) {
        for (Card card : this.cardsPlayed) {
            int x = card.getX();
            int y = card.getY();
    
            int nbCards = 1;
            for (int i = 1; i < nbCardsToWin; i++) {
                int newX = x + i * direction;
                int newY = y + i;
                if (newX >= 0 && newX < BOARD_SIZE && newY >= 0 && newY < BOARD_SIZE &&
                    this.board[newX][newY] != null &&
                    this.board[newX][newY].getColor() == card.getColor()) {
                    nbCards++;
                } else {
                    break;
                }
            }
            if (nbCards >= nbCardsToWin) {
                return true;
            }
        }
    
        return false;
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
            }
        }

        return win;
    }

}
