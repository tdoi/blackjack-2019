package jp.topse.swdev.bigdata.blackjack;

import java.util.Map;
import java.util.Random;

/**
 * Created by doi on 2017/09/27.
 */
public class Player {

    private static class SimpleDecisionMaker implements DecisionMaker {
        @Override
        public Action decide(Player player, Game game) {
            Map<Player, Hand> playerHands = game.getPlayerHands();
            Hand hand = playerHands.get(player);
            if (hand.eval()< 17) {
                return Action.HIT;
            } else {
                return Action.STAND;
            }
        }
    }

    private static class RiskyDecisionMaker implements DecisionMaker {
        @Override
        public Action decide(Player player, Game game) {
            Map<Player, Hand> playerHands = game.getPlayerHands();
            Hand hand = playerHands.get(player);
            if (hand.eval()< 21) {
                return Action.HIT;
            } else {
                return Action.STAND;
            }
        }
    }

    private String name;
    private DecisionMaker decisionMaker;

    public Player(String name) {
//        this(name, new RiskyDecisionMaker());
        this(name, new SimpleDecisionMaker());
    }

    public Player(String name, DecisionMaker decisionMaker) {
        this.name = name;
        this.decisionMaker = decisionMaker;
//        System.out.println("Player " + name + " was created.");
    }

    public String getName() {
        return name;
    }

    public Action action(Game game) {
        return decisionMaker.decide(this, game);
    }

}
