package jp.topse.swdev.bigdata.blackjack;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by doi on 2017/09/26.
 */
public class HandTest {

    @Test
    public void TwoAndTreeShouldBe5() {
        Hand hand = new Hand();
        hand.add(Card.TWO);
        hand.add(Card.THREE);

        assertThat(hand.eval(), is(5));
        assertThat(hand.getCount(), is(2));
    }

    @Test
    public void FourAndKingShouldBe14() {
        Hand hand = new Hand();
        hand.add(Card.FOUR);
        hand.add(Card.KING);

        assertThat(hand.eval(), is(14));
        assertThat(hand.getCount(), is(2));
    }

    @Test
    public void AceAndEightShouldBe19() {
        Hand hand = new Hand();
        hand.add(Card.ACE);
        hand.add(Card.EIGHT);

        assertThat(hand.eval(), is(19));
        assertThat(hand.getCount(), is(2));
    }

    @Test
    public void AceCanBe1() {
        Hand hand = new Hand();
        hand.add(Card.ACE);
        hand.add(Card.FOUR);
        hand.add(Card.SEVEN);

        assertThat(hand.eval(), is(12));
        assertThat(hand.getCount(), is(3));
    }

    @Test
    public void AceCanBe1EvenIfMoreThanOneAcesAreIncluded() {
        Hand hand = new Hand();
        hand.add(Card.ACE);
        hand.add(Card.FOUR);
        hand.add(Card.ACE);
        hand.add(Card.SEVEN);

        assertThat(hand.eval(), is(13));
        assertThat(hand.getCount(), is(4));
    }

    @Test
    public void AceCanBe1UpTo21() {
        Hand hand = new Hand();
        hand.add(Card.ACE);
        hand.add(Card.FOUR);
        hand.add(Card.ACE);
        hand.add(Card.THREE);

        assertThat(hand.eval(), is(19));
        assertThat(hand.getCount(), is(4));
    }

}
