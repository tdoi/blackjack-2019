package jp.topse.swdev.bigdata.blackjack;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * Created by doi on 2017/09/27.
 */
public class PlayerTest {

    @Test
    public void playerIsInitializedByName() {
        Player player = new Player("Taro");

        assertThat(player.getName(), is("Taro"));
    }

    @Test
    public void playerHitAccordingToDecisionMaker() {
        Deck deck = mock(Deck.class);
        when(deck.nextCard()).thenReturn(Card.ACE);
        Game game = new Game(deck);
        DecisionMaker decisionMaker = mock(DecisionMaker.class);
        Player player = new Player("Bob", decisionMaker);
        game.join(player);
        when(decisionMaker.decide(any(Player.class), any(Game.class))).thenReturn(Action.HIT).thenReturn(Action.STAND);
        game.setup();

        game.start();

        verify(decisionMaker, times(2)).decide(any(Player.class), any(Game.class));
    }


}
