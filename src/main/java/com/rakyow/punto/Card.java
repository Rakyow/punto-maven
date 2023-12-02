package com.rakyow.punto;
public class Card {

    // valeur de la carte
    private int value;

    // couleur de la carte
    private Color color;

    private int x;

    private int y;

    // constructeur de la carte
    public Card(int value, Color color) {
        this.value = value;
        this.color = color;
    }

    public Card(int value, Color color, int x, int y) {
        this.value = value;
        this.color = color;
        this.x = x;
        this.y = y;
    }

    public String toString() {
        // Ajoutez ici la logique pour convertir la carte en une représentation textuelle
        return "Card{" +
                "value=" + value +
                ", color=" + color +
                '}';
    }

    public String printCard() {
        // return la valeur et la première lettre de la couleur
        String ret = value + color.getValue().substring(0, 1);
        return ret;
    }

    public String printCardWithCoord() {
        // return la valeur et la première lettre de la couleur
        String ret = value + color.getValue().substring(0, 1) + " (" + x + "," + y + ")";
        return ret;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    public int getValue() {
        return value;
    }

    public Color getColor() {
        return color;
    }
}
