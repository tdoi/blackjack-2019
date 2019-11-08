package jp.topse.swdev.bigdata.blackjack.topse31044;

import jp.topse.swdev.bigdata.blackjack.Deck;
import jp.topse.swdev.bigdata.blackjack.Game;

public class PastGame {	
	private Player toadette;
	
	private Player ranger;
	
	private Player wingDiver;
	
	private Player airRaider;
	
	private Player fencer;
	
	public Player getStormTeam(int index, Player pl) {
		switch(index) {
		case 0 :
			return ranger;
		case 1 :
			return wingDiver;
		case 2 :
			return airRaider;
		case 3 :
			return fencer;
		}
		return null;
	}
	
	public void setStormTeam(int index, Player pl) {
		switch(index) {
		case 0 :
			ranger = pl; return;
		case 1 :
			wingDiver = pl; return;
		case 2 :
			airRaider = pl; return;
		case 3 :
			fencer = pl; return;
		}
	}
	
	public static PastGame parse(String line) {
		PastGame pg = new PastGame();
		
		String[] columns = line.split(",");
	    pg.toadette = new Player(columns[0]);
	    pg.toadette.addTefuda(columns[1], columns[2], columns[3], columns[4], columns[5]);

	    for (int lp = 0; lp < 4; lp++) {
	    	int base = 6 + lp * 7;
	    	Player pl  = new Player(columns[base]);
	    	pl.addTefuda(columns[base+1], columns[base+2], columns[base+3], columns[base+4], columns[base+5]);
	    	pl.parseWinner(columns[base+6]);
	    	
	    	pg.setStormTeam(lp, pl);
	    }
	    
	    return pg;
	}
	
	
	public String toString() {
		return this.toadette + "/" + this.ranger + "/" + this.wingDiver + "/" + this.airRaider + "/" + this.fencer;
	}
}
