package jp.topse.swdev.bigdata.blackjack.topse31058;

import java.awt.List;
import java.io.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import jp.topse.swdev.bigdata.blackjack.Action;
import jp.topse.swdev.bigdata.blackjack.Card;
import jp.topse.swdev.bigdata.blackjack.DecisionMaker;
import jp.topse.swdev.bigdata.blackjack.Game;
import jp.topse.swdev.bigdata.blackjack.Hand;
import jp.topse.swdev.bigdata.blackjack.Player;

public class Topse31058_PredictDeck implements DecisionMaker {

	private static final String INPUT_PATH = "./data/2019.csv";

    private int[] m_MaxNum = {0,0,0,0,0,0,0,0,0,0,0,0,0};

	public Topse31058_PredictDeck() {
        BufferedReader reader = null;
        
    	try {
			reader = new BufferedReader(new FileReader(new File(INPUT_PATH)));
            String line;
            while ((line = reader.readLine()) != null) {
                int[] tmpNum = {0,0,0,0,0,0,0,0,0,0,0,0,0};
            	
                String[] items = line.split(",", 0);
    			for(int i = 0; i < items.length; i++) {
    				Card tmpCard= null;
    				try {
    					tmpCard = Card.valueOf(items[i]);
    				} catch(IllegalArgumentException e) {
    					continue;
    				}
    				int cardIndex = tmpCard.getIndex();
    				tmpNum[cardIndex - 1]++;
    			}
    			for(int i = 0; i < tmpNum.length; i++) {
    				if(tmpNum[i] > m_MaxNum[i]) m_MaxNum[i]=tmpNum[i];
    			}
            }
            reader.close();
    	} catch(IOException ie) {
    		ie.printStackTrace();
    	}
	}

	@Override
	public Action decide(Player player, Game game) {
		int[] tmpDeck = m_MaxNum.clone();
		tmpDeck[game.getUpCard().getIndex()-1]--;
		
		Map<Player,Hand> playerHands = game.getPlayerHands();
		Iterator<Hand> playerHandsCol = playerHands.values().iterator();
		while(playerHandsCol.hasNext()) {
			Hand tmpHand = playerHandsCol.next();
			for(int i = 0; i < tmpHand.getCount(); i++) {
				tmpDeck[tmpHand.get(i).getIndex()-1]--;
			}
		}
		
		int myHandEval = game.getPlayerHands().get(player).eval();
		int safe = 0;
		int bust = 0;
		
		for(int i = 0; i < tmpDeck.length; i++) {
			int remine = tmpDeck[i] >= 0 ? tmpDeck[i] : 0;
			int value = i < 10 ? i + 1 : 10;
			if(myHandEval + value > 21) {
				bust = bust + remine;
			}else {
				safe = safe + remine;
			}
		}
		
		if(safe>0.59*(safe+bust)) return Action.HIT;
		
		return Action.STAND;
	}
}
