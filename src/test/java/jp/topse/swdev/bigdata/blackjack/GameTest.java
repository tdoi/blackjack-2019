package jp.topse.swdev.bigdata.blackjack;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * Created by doi on 2017/09/27.
 */
public class GameTest {

    private Deck deck;
    private Game game;

    @Before
    public void setup() {
        deck = mock(Deck.class);
        game = new Game(deck);
    }

    @Test (expected = RuntimeException.class)
    public void gameShouldBeSetup() {
        game.getUpCard();
    }

    @Test
    public void gameProvidesUpCard() {
        when(deck.nextCard()).thenReturn(Card.ACE);
        game.setup();

        Card upCard = game.getUpCard();

        assertThat(upCard, is(Card.ACE));
    }

    @Test
    public void playerCanJoinTheGame() {
        Player taro = new Player("Taro");
        game.join(taro);

        assertThat(game.getNumberOfPlayers(), is(1));
    }

    @Test (expected = RuntimeException.class)
    public void playerCannotJoinTheGameAfterSetup() {
        game.setup();

        Player taro = new Player("Taro");
        game.join(taro);
    }

    @Test (expected = RuntimeException.class)
    public void failsToGetPlayerHandsBeforeSetup() {
        Player taro = new Player("Taro");
        game.join(taro);

        game.getPlayerHands();
    }

    @Test
    public void gameProvidesPlayersHand() {
        when(deck.nextCard()).thenReturn(Card.THREE)
                .thenReturn(Card.FOUR)
                .thenReturn(Card.NINE)
                .thenReturn(Card.KING);
        Player taro = new Player("taro");
        game.join(taro);
        game.setup();

        Map<Player, Hand> hands = game.getPlayerHands();
        Hand hand = hands.get(taro);

        assertThat(hand.eval(), is(19));
    }

    @Test
    public void gameIsProgressByAskPlayers() {
        when(deck.nextCard()).thenReturn(Card.ACE);
        Player player = mock(Player.class);
        when(player.action(game)).thenReturn(Action.STAND);
        game.join(player);
        game.setup();

        game.start();

        verify(player, times(1)).action(game);
    }

    @Test
    public void gameIsProgressUntilPlayerCallStand() {
        when(deck.nextCard()).thenReturn(Card.ACE);
        Player player = mock(Player.class);
        when(player.action(game)).thenReturn(Action.HIT).thenReturn(Action.STAND);
        game.join(player);
        game.setup();

        game.start();

        verify(player, times(2)).action(game);
    }

    @Test
    public void dealerHitCardsUntil17AfterAllPlayersArePlayed() {
        when(deck.nextCard())
                .thenReturn(Card.TWO, Card.EIGHT)
                .thenReturn(Card.QUEEN, Card.TEN)
                .thenReturn(Card.FIVE)
                .thenReturn(Card.SIX);
        Player player = mock(Player.class);
        when(player.action(game)).thenReturn(Action.STAND);
        game.join(player);
        game.setup();

        game.start();
        Hand hand = game.getDealerHand();

        assertThat(hand.eval(), is(21));
    }

    @Test (expected = RuntimeException.class)
    public void failsToGetResultsBeforeGameIsFinished() {
        when(deck.nextCard())
                .thenReturn(Card.ACE, Card.KING)
                .thenReturn(Card.QUEEN, Card.TEN);
        Player player = mock(Player.class);
        when(player.action(game)).thenReturn(Action.STAND);
        game.join(player);
        game.setup();

        game.result();
    }

    @Test
    public void gameProvideResult() {
        when(deck.nextCard())
                .thenReturn(Card.ACE, Card.KING)
                .thenReturn(Card.QUEEN, Card.TEN);
        Player player = mock(Player.class);
        when(player.action(game)).thenReturn(Action.STAND);
        game.join(player);
        game.setup();
        game.start();

        Result result = game.result();
        Map<Player, Result.Type> standings = result.getStandings();

        assertThat(standings.get(player), is(Result.Type.LOSE));
    }

    @Test
    public void gameProvideResultFor2Players() {
        when(deck.nextCard())
                .thenReturn(Card.NINE, Card.KING)
                .thenReturn(Card.QUEEN, Card.TEN)
                .thenReturn(Card.JACK, Card.EIGHT);
        Player player1 = mock(Player.class);
        when(player1.action(game)).thenReturn(Action.STAND);
        game.join(player1);
        Player player2 = mock(Player.class);
        when(player2.action(game)).thenReturn(Action.STAND);
        game.join(player2);
        game.setup();
        game.start();

        Result result = game.result();
        Map<Player, Result.Type> standings = result.getStandings();

        assertThat(standings.size(), is(2));
        assertThat(standings.get(player1), is(Result.Type.WIN));
        assertThat(standings.get(player2), is(Result.Type.LOSE));
    }

    @Test
    public void gameDoesNotAskDecisionFromPlayerIfThePlayerIsBust() {
        when(deck.nextCard())
                .thenReturn(Card.NINE, Card.KING)
                .thenReturn(Card.QUEEN, Card.FIVE)
                .thenReturn(Card.SEVEN);
        Player player = mock(Player.class);
        when(player.action(game)).thenReturn(Action.HIT);
        game.join(player);

        game.setup();
        game.start();

        Result result = game.result();
        Map<Player, Result.Type> standings = result.getStandings();

        verify(player, times(1)).action(game);

        assertThat(standings.size(), is(1));
        assertThat(standings.get(player), is(Result.Type.LOSE));
    }

}
