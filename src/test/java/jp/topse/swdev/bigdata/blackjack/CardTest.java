package jp.topse.swdev.bigdata.blackjack;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by doi on 2017/09/28.
 */
public class CardTest {

    @Test
    public void numberOfTypesShouldBe13() {
        assertThat(Card.numberOfTypes(), is(13));
    }

    @Test
    public void AceShouldBeEvaluatedAs11() {
        assertThat(Card.ACE.getValue(), is(11));
    }

    @Test
    public void IndexOfQueenShouldBe12() {
        assertThat(Card.QUEEN.getIndex(), is(12));
    }

    @Test
    public void CardObjectCanBeRetrievedByIndex() {
        assertThat(Card.indexOf(11), is(Card.JACK));
    }

}
