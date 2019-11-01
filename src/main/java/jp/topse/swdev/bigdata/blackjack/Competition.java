package jp.topse.swdev.bigdata.blackjack;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by doi on 2017/11/16.
 */
public class Competition {

    private final static int TRIAL_COUNT = 100;

    public static void main(String[] args) {
        Competition competition = new Competition();
        competition.game();
    }

    public void game() {
        for (int i = 1; i <= 3; ++i) {
            game(i);
        }
    }

    private void game(int index) {
        PrintWriter logger = null;
        try {
            logger = new PrintWriter(new BufferedWriter(new FileWriter("./logs/" + index + ".csv", false)));

            Deck deck = Deck.createDefault();
            Player[] players = new Player[]{
                new Player("Alice"),
                new Player("Bob"),
                new Player("Charlie"),
                new Player("David"),
            };
            Map<Player, Integer> pointsMap = eval(players, deck, logger);
            showResult("Game " + index, pointsMap);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            logger.close();
        }
    }

    private Map<Player, Integer> eval(Player[] players, Deck deck, PrintWriter logger) {
        Map<Player, Integer> pointsMap = new HashMap<Player, Integer>();
        for (Player player : players) {
            pointsMap.put(player, 0);
        }

        Permutations<Player> permutations = new Permutations<Player>(players);
        while (permutations.hasNext()) {
            Player[] list = permutations.next();
            for (int i = 0; i < TRIAL_COUNT; ++i) {
                Map<Player, Result.Type> standings = doOneGame(list, deck, logger);
                for (Player player : players) {
                    if (standings.get(player) == Result.Type.WIN) {
                        int point = pointsMap.get(player);
                        pointsMap.put(player, point + 1);
                    }
                }

            }
        }

        return pointsMap;
    }

    private Map<Player, Result.Type> doOneGame(Player[] players, Deck deck, PrintWriter logger) {
        deck.reset();
        Game game = new Game(deck);

        for (Player player : players) {
            game.join(player);
        }
        game.setup();
        game.start();

        Result result = game.result();
        logger.println(result.toString());
        return result.getStandings();
    }

    private void showResult(String name, Map<Player, Integer> pointsMap) {
        System.out.println("Results of " + name);
        pointsMap.entrySet().stream().forEach((entry) -> {
            System.out.println(entry.getKey().getName() + " : " + entry.getValue());
        });
    }
}
