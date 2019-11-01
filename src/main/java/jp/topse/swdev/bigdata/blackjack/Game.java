package jp.topse.swdev.bigdata.blackjack;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by doi on 2017/09/27.
 */
public class Game {

    private Deck deck;

    private Hand dealerHand = new Hand();
    private List<Player> players = new LinkedList<Player>();
    private Map<Player, Hand> playerHands = new HashMap<Player, Hand>();

    private boolean isInitialized;
    private boolean isFinished;

    public Game(Deck deck) {
        isInitialized = false;
        isFinished = false;
        this.deck = deck;
    }

    public void join(Player player) {
        if (isInitialized) {
            throw new RuntimeException("Game is already setup.");
        }

        players.add(player);
        playerHands.put(player, new Hand());
    }

    public int getNumberOfPlayers() {
        return playerHands.size();
    }

    public void setup() {
        if (isInitialized) {
            throw new RuntimeException("Game is already setup.");
        }

        dealerHand.add(deck.nextCard());
        dealerHand.add(deck.nextCard());

        playerHands.entrySet()
                .forEach(entry -> {
                    entry.getValue().add(deck.nextCard());
                    entry.getValue().add(deck.nextCard());
                });

        isInitialized = true;
    }

    public Hand getDealerHand() {
        if (!isFinished) {
            throw new RuntimeException("Game is not finished.");
        }
        return dealerHand;
    }

    public Card getUpCard() {
        if (!isInitialized) {
            throw new RuntimeException("Game is not setup.");
        }

        return dealerHand.get(0);
    }

    public Map<Player,Hand> getPlayerHands() {
        if (!isInitialized) {
            throw new RuntimeException("Game is not setup.");
        }
        return playerHands;
    }

    public void start() {
        players.stream().forEach(player -> {
            while (true) {
                Action action = player.action(this);
                if (action == Action.STAND) {
                    break;
                } else if (action == Action.HIT) {
                    playerHands.get(player).add(deck.nextCard());
                }
                if (playerHands.get(player).eval() >= 21) {
                    break;
                }
            }
        });

        while (dealerHand.eval() < 17) {
            dealerHand.add(deck.nextCard());
        }

        isFinished =true;
    }

    public Result result() {
        if (!isFinished) {
            throw new RuntimeException("Game is not finished.");
        }

        return new Result(dealerHand, players, playerHands);
    }
}
