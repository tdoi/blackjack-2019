package jp.topse.swdev.bigdata.blackjack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by doi on 2017/09/28.
 */
public class Result {

    public static enum Type {
        WIN,
        DRAW,
        LOSE
    }

    private Hand dealerHand;
    private List<Player> players;
    private Map<Player, Hand> playerHands;
    private Map<Player, Type> standings;

    public Result(Hand dealerHand, List<Player> players, Map<Player, Hand> playerHands) {
        this.dealerHand = dealerHand;
        this.players = players;
        this.playerHands = playerHands;
        standings = new HashMap<Player, Type>();
        playerHands.entrySet().stream().forEach(entry -> {
            Player player = entry.getKey();
            Hand hand = entry.getValue();
            standings.put(player, checkResult(dealerHand, hand));
        });
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Dealer");
        appendHand(builder, dealerHand);
        players.stream().forEach(player -> {
            builder.append(",");
            builder.append(player.getName());
            Hand hand = playerHands.get(player);
            appendHand(builder, hand);
            builder.append(",");
            builder.append(standings.get(player));
        });

        return builder.toString();
    }

    private void appendHand(StringBuilder builder, Hand hand) {
        for (int i = 0; i < 5; ++i) {
            builder.append(",");
            try {
                Card card = hand.get(i);
                builder.append(card.toString());
            } catch (IndexOutOfBoundsException e) {
                // Do nothing
            }
        }
    }

    public Hand getDealerHand() {
        return dealerHand;
    }

    public Map<Player, Hand> getPlayerHands() {
        return playerHands;
    }

    public Map<Player,Type> getStandings() {
        return standings;
    }

    private Type checkResult(Hand dealerHand, Hand hand) {
        int dealerPoint = dealerHand.eval();
        int playerPoint = hand.eval();
        if (dealerPoint > 21) {
            if (playerPoint > 21) {
                return Type.LOSE;
            } else {
                return Type.WIN;
            }
        } else {
            if (playerPoint > 21 || dealerPoint > playerPoint) {
                return Type.LOSE;
            } else if (dealerPoint < playerPoint) {
                return Type.WIN;
            } else {
                return Type.DRAW;
            }
        }
    }

}
