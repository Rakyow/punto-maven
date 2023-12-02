package com.rakyow.punto;

public enum Color {
    VERT("vert"),
    ROUGE("rouge"),
    BLEU("bleu"),
    ORANGE("orange"),
    NONE("none");

    private final String value;

    private Color(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
