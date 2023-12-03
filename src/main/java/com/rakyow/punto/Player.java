package com.rakyow.punto;

import java.util.ArrayList;

/**
 * This class is used to represent a player.
 */
public class Player {
    
    private String name; // player's name
    
    private ArrayList<Card> cards; // player's cards

    private int score; // player's score

    private int round;  // player's round

    /**
     * This constructor is used to create a player.
     * @param name player's name
     */
    public Player(String name) {
        this.name = name;
        this.cards = new ArrayList<Card>();
        this.score = 0;
        this.round = 0;
    }

    /**
     * This method is used to create a string representing player's cards.
     */
    public void printCardsPlayer() {
        String stringCards = "";
        for (Card card : this.cards) {
            stringCards += card.printCard() + " ";
        }
        System.out.println(stringCards);
    }

    /**
     * This method is used to get a random card from the player's cards.
     * @return a random card from the player's cards
     */
    public Card randomCard() {
        Card card = null;

        if (this.cards.size() > 0) {
            
            int random = (int) (Math.random() * this.cards.size());
            card = this.cards.get(random);
            this.cards.remove(random);
        }
        return card;
    }
    
    /**
     * This method is used to add a card to the player's cards.
     * @param card card to add
     */
    public void addCard(Card card) {
        this.cards.add(card);
    }

    /**
     * This method is used to add a point to the player's score.
     */
    public String getName() {
        return this.name;
    }

    /**
     * This method is used to get the player's cards.
     */
    public ArrayList<Card> getCards() {
        return this.cards;
    }

    /**
     * This method is used to get the player's score.
     */
    public int getScore() {
        return this.score;
    }

    /**
     * This method is used to add a point to the player's score.
     */
    public int getRound() {
        return this.round;
    }

    /**
     * This method is used to add a point to the player's score.
     */
    public void addRound() {
        System.out.println("\n----------------------------------------\n");
        System.out.println(this.name + " a gagné la manche");
        this.round++;
    }
}
