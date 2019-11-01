package jp.topse.swdev.bigdata.blackjack;

import java.util.Arrays;

public enum Card {
    ACE(1, 11),
    TWO(2, 2),
    THREE(3, 3),
    FOUR(4, 4),
    FIVE(5, 5),
    SIX(6, 6),
    SEVEN(7, 7),
    EIGHT(8, 8),
    NINE(9, 9),
    TEN(10, 10),
    JACK(11, 10),
    QUEEN(12, 10),
    KING(13, 10);

    public static Card indexOf(int index) {
        return Arrays.stream(Card.values())
                .filter(card -> card.getIndex() == index)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("There is no card for " + index));
    }

    public static int numberOfTypes() {
        return Card.values().length;
    }

    private final int index;
    private final int value;

    private Card(int index, int value)
    {
        this.index = index;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public int getIndex() {
        return index;
    }
}