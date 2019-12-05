package jp.topse.swdev.bigdata.blackjack.topse31044.past;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PastGame {	
	/** ディーラー(Dealer) */
	private PastPlayer toadette;
	
	/** アリス(Aice) */
	private PastPlayer ranger;

	/** ボブ(Bob) */
	private PastPlayer wingDiver;
	
	/** チャーリー(Charlie) */
	private PastPlayer airRaider;
	
	/** デーブ(Dave) */
	private PastPlayer fencer;
	
	/**
	 * CSVを解析して構造化
	 * @param line
	 * @return
	 */
	public static PastGame parse(String line) {
		PastGame pg = new PastGame();
		
		String[] el = line.split(",");
		
		// ======================================
		// ディーラー
		// ======================================
	    pg.toadette = new PastPlayer();
	    pg.toadette.setName(el[0]);
	    pg.toadette.parseCardNameAt(0, el[1]);
	    pg.toadette.parseCardNameAt(1, el[2]);
	    pg.toadette.parseCardNameAt(2, el[3]);
	    pg.toadette.parseCardNameAt(3, el[4]);
	    pg.toadette.parseCardNameAt(4, el[5]);

		// ======================================
		// プレイヤー
		// ======================================
	    List<PastPlayer> bufPl = new ArrayList<>();
	    for (int lp = 0; lp < 4; lp++) {
	    	int offset = 6 + lp * 7;
	    	PastPlayer pl  = new PastPlayer();
	    	pl.setName(el[offset]);
	    	pl.parseCardNameAt(0, el[offset + 1]);
	    	pl.parseCardNameAt(1, el[offset + 2]);
	    	pl.parseCardNameAt(2, el[offset + 3]);
	    	pl.parseCardNameAt(3, el[offset + 4]);
	    	pl.parseCardNameAt(4, el[offset + 5]);
	    	pl.parseResult(el[offset + 6]);
	    	bufPl.add(pl);
	    }
	    
	    pg.ranger = bufPl.get(0);
	    pg.wingDiver = bufPl.get(1);
	    pg.airRaider = bufPl.get(2);
	    pg.fencer = bufPl.get(3);
	    
	    return pg;
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
	
	public List<PastPlayerContext> getPlayerContexts(){
		return Arrays.asList(
				new PastPlayerContext(this.ranger, this),
				new PastPlayerContext(this.wingDiver, this),
				new PastPlayerContext(this.airRaider, this),
				new PastPlayerContext(this.fencer, this));
	}
}
