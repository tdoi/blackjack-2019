package jp.topse.swdev.bigdata.blackjack;

import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by doi on 2017/09/28.
 */
public class ResultTest {

    private Player player = new Player("Alice");
    private Hand dealerHand = new Hand();
    private Hand playerHand = new Hand();

    private Result makeResult() {
        List<Player> players = new LinkedList<>();
        players.add(player);
        Map<Player, Hand> playerHands = new HashMap<Player, Hand>();
        playerHands.put(player, playerHand);

        return new Result(dealerHand, players, playerHands);
    }

    @Test
    public void loseIfDealerIsBlackJackAndPlayerIsNotBlackJack() {
        dealerHand.add(Card.ACE);
        dealerHand.add(Card.KING);

        playerHand.add(Card.QUEEN);
        playerHand.add(Card.JACK);

        Result result = makeResult();
        Map<Player, Result.Type> standings = result.getStandings();

        assertThat(result.getDealerHand(), is(dealerHand));
        assertThat(result.getPlayerHands().size(), is(1));
        assertThat(standings.size(), is(1));
        assertThat(standings.get(player), is(Result.Type.LOSE));
    }

    @Test
    public void winIfPlayerPointIsMoreThanDealerPoint() {
        dealerHand.add(Card.TEN);
        dealerHand.add(Card.KING);

        playerHand.add(Card.ACE);
        playerHand.add(Card.JACK);

        Result result = makeResult();
        Map<Player, Result.Type> standings = result.getStandings();

        assertThat(result.getDealerHand(), is(dealerHand));
        assertThat(result.getPlayerHands().size(), is(1));
        assertThat(standings.size(), is(1));
        assertThat(standings.get(player), is(Result.Type.WIN));
    }

    @Test
    public void loseIfPlayerIsBurst() {
        dealerHand.add(Card.TEN);
        dealerHand.add(Card.KING);

        playerHand.add(Card.TEN);
        playerHand.add(Card.FIVE);
        playerHand.add(Card.SEVEN);

        Result result = makeResult();
        Map<Player, Result.Type> standings = result.getStandings();

        assertThat(result.getDealerHand(), is(dealerHand));
        assertThat(result.getPlayerHands().size(), is(1));
        assertThat(standings.size(), is(1));
        assertThat(standings.get(player), is(Result.Type.LOSE));
    }

    @Test
    public void loseIfPlayerAndDealerAreBurst() {
        dealerHand.add(Card.EIGHT);
        dealerHand.add(Card.EIGHT);
        dealerHand.add(Card.EIGHT);

        playerHand.add(Card.TEN);
        playerHand.add(Card.FIVE);
        playerHand.add(Card.SEVEN);

        Result result = makeResult();
        Map<Player, Result.Type> standings = result.getStandings();

        assertThat(result.getDealerHand(), is(dealerHand));
        assertThat(result.getPlayerHands().size(), is(1));
        assertThat(standings.size(), is(1));
        assertThat(standings.get(player), is(Result.Type.LOSE));
    }

    @Test
    public void winIfPlayerIsNotBurstAndDealerIsBurst() {
        dealerHand.add(Card.EIGHT);
        dealerHand.add(Card.EIGHT);
        dealerHand.add(Card.EIGHT);

        playerHand.add(Card.TEN);
        playerHand.add(Card.SEVEN);

        Result result = makeResult();
        Map<Player, Result.Type> standings = result.getStandings();

        assertThat(result.getDealerHand(), is(dealerHand));
        assertThat(result.getPlayerHands().size(), is(1));
        assertThat(standings.size(), is(1));
        assertThat(standings.get(player), is(Result.Type.WIN));
    }

    @Test
    public void drawIfDealerAndPlayerPointIsSame() {
        dealerHand.add(Card.EIGHT);
        dealerHand.add(Card.NINE);

        playerHand.add(Card.TEN);
        playerHand.add(Card.SEVEN);

        Result result = makeResult();
        Map<Player, Result.Type> standings = result.getStandings();

        assertThat(result.getDealerHand(), is(dealerHand));
        assertThat(result.getPlayerHands().size(), is(1));
        assertThat(standings.size(), is(1));
        assertThat(standings.get(player), is(Result.Type.DRAW));
    }

    @Test
    public void toStringGenerateCsv() {
        dealerHand.add(Card.FIVE);
        dealerHand.add(Card.NINE);
        dealerHand.add(Card.TWO);
        dealerHand.add(Card.THREE);

        playerHand.add(Card.TEN);
        playerHand.add(Card.SEVEN);

        Result result = makeResult();
        String csv = result.toString();

        assertThat(csv, is("Dealer,FIVE,NINE,TWO,THREE,,Alice,TEN,SEVEN,,,,LOSE"));
    }
}
