package jp.topse.swdev.bigdata.blackjack;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * Created by doi on 2017/09/27.
 */
public class DeckTest {

    @Test (expected=RuntimeException.class)
    public void FailToGetCardFromEmptyDeck() {
        Map<Card, Integer> map = new HashMap<Card, Integer>();
        map.put(Card.TWO,   0);
        map.put(Card.THREE, 0);
        map.put(Card.FOUR,  0);
        map.put(Card.FIVE,  0);
        map.put(Card.SIX,   0);
        map.put(Card.SEVEN, 0);
        map.put(Card.EIGHT, 0);
        map.put(Card.NINE,  0);
        map.put(Card.TEN,   0);
        map.put(Card.JACK,  0);
        map.put(Card.QUEEN, 0);
        map.put(Card.KING,  0);
        map.put(Card.ACE,   0);
        Deck deck = new Deck(map);

        Card card = deck.nextCard();
    }

    @Test
    public void NumbersOfEachCardsAreDefinedInConstructor() {
        Map<Card, Integer> map = new HashMap<Card, Integer>();
        map.put(Card.TWO,   0);
        map.put(Card.THREE, 0);
        map.put(Card.FOUR,  0);
        map.put(Card.FIVE,  0);
        map.put(Card.SIX,   0);
        map.put(Card.SEVEN, 0);
        map.put(Card.EIGHT, 0);
        map.put(Card.NINE,  0);
        map.put(Card.TEN,   0);
        map.put(Card.JACK,  0);
        map.put(Card.QUEEN, 0);
        map.put(Card.KING,  0);
        map.put(Card.ACE,   1);

        Deck deck = new Deck(map);

        Card card = deck.nextCard();

        assertThat(card, is(Card.ACE));
    }

    @Test (expected = RuntimeException.class)
    public void FailToGetCardAfterDeckBecomesEmpty() {
        Map<Card, Integer> map = new HashMap<Card, Integer>();
        map.put(Card.TWO,   0);
        map.put(Card.THREE, 0);
        map.put(Card.FOUR,  0);
        map.put(Card.FIVE,  0);
        map.put(Card.SIX,   0);
        map.put(Card.SEVEN, 0);
        map.put(Card.EIGHT, 0);
        map.put(Card.NINE,  0);
        map.put(Card.TEN,   0);
        map.put(Card.JACK,  0);
        map.put(Card.QUEEN, 0);
        map.put(Card.KING,  0);
        map.put(Card.ACE,   1);

        Deck deck = new Deck(map);
        deck.nextCard();

        deck.nextCard();
    }

    @Test
    public void DrawCardsIn13TimesFromDeckWith13Cards() {
        Map<Card, Integer> map = new HashMap<Card, Integer>();
        map.put(Card.TWO,   1);
        map.put(Card.THREE, 1);
        map.put(Card.FOUR,  1);
        map.put(Card.FIVE,  1);
        map.put(Card.SIX,   1);
        map.put(Card.SEVEN, 1);
        map.put(Card.EIGHT, 1);
        map.put(Card.NINE,  1);
        map.put(Card.TEN,   1);
        map.put(Card.JACK,  1);
        map.put(Card.QUEEN, 1);
        map.put(Card.KING,  1);
        map.put(Card.ACE,   1);
        Deck deck = new Deck(map);

        for (int i = 0; i < 13; ++i) {
            deck.nextCard();
        }

        try {
            deck.nextCard();
            fail();
        } catch (RuntimeException e) {
        }
    }

}
