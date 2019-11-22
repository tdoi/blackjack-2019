package jp.topse.swdev.bigdata.blackjack.topse31044;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.topse.swdev.bigdata.blackjack.Deck;
import jp.topse.swdev.bigdata.blackjack.Game;

public class PastGame {	
	private PastPlayer toadette;
	
	private PastPlayer ranger;
	
	private PastPlayer wingDiver;
	
	private PastPlayer airRaider;
	
	private PastPlayer fencer;
	
	public List<PastPlayer> getStormTeam() {
		return Arrays.asList(ranger, wingDiver, airRaider, fencer);
	}
	
	public PastPlayer stormTeam(int index) {
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
	
	public void stormTeam(int index, PastPlayer pl) {
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
	    pg.toadette = new PastPlayer(columns[0]);
	    pg.toadette.addTefuda(columns[1], columns[2], columns[3], columns[4], columns[5]);

	    for (int lp = 0; lp < 4; lp++) {
	    	int base = 6 + lp * 7;
	    	PastPlayer pl  = new PastPlayer(columns[base]);
	    	pl.addTefuda(columns[base+1], columns[base+2], columns[base+3], columns[base+4], columns[base+5]);
	    	pl.parseWinner(columns[base+6]);
	    	
	    	pg.stormTeam(lp, pl);
	    }
	    
	    return pg;
	}
	
	
	public String toString() {
		return this.toadette + "/" + this.ranger + "/" + this.wingDiver + "/" + this.airRaider + "/" + this.fencer;
	}
	
	public List<String> getKokaiJoho(){
		return Arrays.asList(
				this.toadette.getKokai().get(0),
				this.ranger.getKokai().get(0),
				this.wingDiver.getKokai().get(0),
				this.airRaider.getKokai().get(0),
				this.fencer.getKokai().get(0));
	}

	/**
	 * @return the toadette
	 */
	public PastPlayer getToadette() {
		return toadette;
	}

	/**
	 * @param toadette the toadette to set
	 */
	public void setToadette(PastPlayer toadette) {
		this.toadette = toadette;
	}

	/**
	 * @return the ranger
	 */
	public PastPlayer getRanger() {
		return ranger;
	}

	/**
	 * @param ranger the ranger to set
	 */
	public void setRanger(PastPlayer ranger) {
		this.ranger = ranger;
	}

	/**
	 * @return the wingDiver
	 */
	public PastPlayer getWingDiver() {
		return wingDiver;
	}

	/**
	 * @param wingDiver the wingDiver to set
	 */
	public void setWingDiver(PastPlayer wingDiver) {
		this.wingDiver = wingDiver;
	}

	/**
	 * @return the airRaider
	 */
	public PastPlayer getAirRaider() {
		return airRaider;
	}

	/**
	 * @param airRaider the airRaider to set
	 */
	public void setAirRaider(PastPlayer airRaider) {
		this.airRaider = airRaider;
	}

	/**
	 * @return the fencer
	 */
	public PastPlayer getFencer() {
		return fencer;
	}

	/**
	 * @param fencer the fencer to set
	 */
	public void setFencer(PastPlayer fencer) {
		this.fencer = fencer;
	}
}
