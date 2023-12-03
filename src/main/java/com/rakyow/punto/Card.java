package com.rakyow.punto;

/**
 * This class is used to represent a card.
 */
public class Card {


    private int value; // card's value

    
    private Color color; // card's color

    private int x; // card's x coordinate

    private int y; // card's y coordinate

    /**
     * This constructor is used to create a card.
     * @param value card's value
     * @param color card's color
     */
    public Card(int value, Color color) {
        this.value = value;
        this.color = color;
    }

    /**
     * This constructor is used to create a card.
     * @param value card's value
     * @param color card's color
     * @param x card's x coordinate
     * @param y card's y coordinate
     */
    public Card(int value, Color color, int x, int y) {
        this.value = value;
        this.color = color;
        this.x = x;
        this.y = y;
    }

    /**
     * This method is used to create a string representing the card.
     * @return string representing the card
     */
    public String toString() {
        
        return "Card{" +
                "value=" + value +
                ", color=" + color +
                '}';
    }

    /**
     * This method is used to create a string representing the card.
     * like 6o for 6 of orange
     * @return a string representing the card
     */
    public String printCard() {
        
        String ret = value + color.getValue().substring(0, 1);
        return ret;
    }
   
    /**
     * This method is used to create a string representing the card.
     * like 6o (1,2) for 6 of orange at coordinates (1,2)
     * @return a string representing the card
     */
    public String printCardWithCoord() {
        
        String ret = value + color.getValue().substring(0, 1) + " (" + x + "," + y + ")";
        return ret;
    }

    /**
     * This method is used to set the card's x coordinate.
     * @param x card's x coordinate
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * This method is used to set the card's y coordinate.
     * @param y card's y coordinate
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * This method is used to set the card's value.
     * @param value card's value
     */
    public int getX() {
        return x;
    }

    /**
     * This method is used to set the card's value.
     * @param value card's value
     */
    public int getY() {
        return y;
    }

    /**
     * This method is used to set the card's value.
     * @param value card's value
     */
    public int getValue() {
        return value;
    }

    /**
     * This method is used to set the card's color.
     * @param color card's color
     */
    public Color getColor() {
        return color;
    }
}
