package jp.topse.swdev.bigdata.blackjack.demo;

import jp.topse.swdev.bigdata.blackjack.Deck;
import jp.topse.swdev.bigdata.blackjack.Game;
import jp.topse.swdev.bigdata.blackjack.Player;
import jp.topse.swdev.bigdata.blackjack.Result;
import jp.topse.swdev.bigdata.blackjack.topse31044.Topse31044;
import jp.topse.swdev.bigdata.blackjack.topse31058.Topse31058_AvoidBust;

/**
 * Created by doi on 2017/09/28.
 */
public class Demo {

    public static void main(String[] args) {
    	Player[] players = new Player[] {
    	        new Player("Alice"),
                new Player("Bob"),
				new Player("TopSE31044", new Topse31044()),
				new Player("TopSE31058", new Topse31058_AvoidBust()),
		};
		Demo demo = new Demo(players);
		demo.eval();
    }
    private Player[] players = null;

    public Demo(Player[] players) {
        this.players = players;
    }

    private void eval() {
    	for (int i = 0; i < 100000; ++i) {
    		doOneGame(players);
    	}
    }

    private void doOneGame(Player[] players) {
        Deck deck = Deck.createDeck2019();

        Game game = new Game(deck);

        for (Player player : players) {
            game.join(player);
        }

        game.setup();

        game.start();

        Result result = game.result();

        System.out.println(result.toString());
    }
}
