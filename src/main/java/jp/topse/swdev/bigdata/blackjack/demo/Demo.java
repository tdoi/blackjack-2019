package jp.topse.swdev.bigdata.blackjack.demo;

import jp.topse.swdev.bigdata.blackjack.DecisionMaker;
import jp.topse.swdev.bigdata.blackjack.Deck;
import jp.topse.swdev.bigdata.blackjack.Game;
import jp.topse.swdev.bigdata.blackjack.Player;
import jp.topse.swdev.bigdata.blackjack.Result;
import jp.topse.swdev.bigdata.blackjack.topse31044.Topse31044;
import jp.topse.swdev.bigdata.blackjack.*;

/**
 * Created by doi on 2017/09/28.
 */
public class Demo {

    public static void main(String[] args) {
    	Player[] players = new Player[] {
				new Player("Aice"),
				new Player("Bob"),
				new Player("Charlie"),
//				new Player("Dave"),
//				new Player("Ellen"),
//				new Player("Frank"),
				new Player("Toadette", new Topse31044()),
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
//        Deck deck = Deck.createDefault();
//        Deck deck = Deck.createTest1Deck();
//        Deck deck = Deck.createTest2Deck();
//        Deck deck = Deck.createTest3Deck();
//        Deck deck = Deck.createTest4Deck();
        Deck deck = Deck.createTest5Deck();

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
