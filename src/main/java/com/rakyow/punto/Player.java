package com.rakyow.punto;

import java.util.ArrayList;

public class Player {

    // nom du joueur
    private String name;

    // cartes du joueur
    private ArrayList<Card> cards;

    // score du joueur
    private int score;

    // round gagné par le joueur
    private int round;

    public Player(String name) {
        this.name = name;
        this.cards = new ArrayList<Card>();
        this.score = 0;
        this.round = 0;
    }

    public void printCardsPlayer() {
        String stringCards = "";
        for (Card card : this.cards) {
            stringCards += card.printCard() + " ";
        }
        System.out.println(stringCards);
    }

    public Card randomCard() {
        Card card = null;

        if (this.cards.size() > 0) {
            
            int random = (int) (Math.random() * this.cards.size());
            card = this.cards.get(random);
            this.cards.remove(random);
        }
        return card;
    }

    public void addCard(Card card) {
        this.cards.add(card);
    }


    public String getName() {
        return this.name;
    }

    public ArrayList<Card> getCards() {
        return this.cards;
    }

    public int getScore() {
        return this.score;
    }

    public int getRound() {
        return this.round;
    }

    public void addRound() {
        System.out.println(this.name + " a gagné la manche");
        this.round++;
        System.out.println(this.name + " a " + this.score + " points");
    }
}
