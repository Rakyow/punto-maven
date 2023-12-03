package com.rakyow.punto;

/**
 * This enum is used to represent a card's color.
 */
public enum Color {
    VERT("vert"),
    ROUGE("rouge"),
    BLEU("bleu"),
    ORANGE("orange"),
    NONE("none");

    private final String value; // color's value

    /**
     * This constructor is used to create a color.
     * @param value color's value
     */
    private Color(String value) {
        this.value = value;
    }

    /**
     * This method is used to get the color's value.
     * @return color's value
     */
    public String getValue() {
        return value;
    }

    /**
     * This method is used to create a string representing the color.
     * @return string representing the color
     */
    public String toString() {
        return value;
    }
}
