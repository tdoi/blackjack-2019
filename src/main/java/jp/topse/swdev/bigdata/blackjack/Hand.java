package jp.topse.swdev.bigdata.blackjack;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by doi on 2017/09/26.
 */
public class Hand {

    private List<Card> cards = new LinkedList<Card>();

    public int getCount() {
        return cards.size();
    }

    public void add(Card card) {
        cards.add(card);
    }

    public Card get(int index) {
        return cards.get(index);
    }

    public int eval() {
        int score = cards.stream()
                .map(card -> card.getValue())
                .reduce(0, (sum, n) -> sum + n);
        int numOfAce = (int)cards.stream()
                .filter(card -> card.equals(Card.ACE))
                .count();
        for (int i = 0; i < numOfAce; ++i) {
            if (score > 21) {
                score -= 10;
            }
        }

        return score;
    }
}
