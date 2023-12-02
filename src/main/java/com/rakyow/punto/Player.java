package com.rakyow.punto;

import java.util.List;

public class Player {

    // nom du joueur
    private String name;

    // cartes du joueur
    private List<Card> cards;

    // score du joueur
    private int score;

    // round gagné par le joueur
    private int round;

    public Player(String name) {
        this.name = name;
        this.cards = null; // ou new ArrayList<>() si vous voulez une liste vide par défaut
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

    public void setCards(List<Card> playerCards) {
        this.cards = playerCards;
    }



    public String getName() {
        return this.name;
    }

    public List<Card> getCards() {
        return this.cards;
    }

    public int getScore() {
        return this.score;
    }

    public int getRound() {
        return this.round;
    }
}
