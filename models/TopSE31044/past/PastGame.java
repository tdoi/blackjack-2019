package jp.topse.swdev.bigdata.blackjack.topse31044.past;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PastGame {	
	/**x ディーラー(Dealer) */
	private PastPlayer toadette;
	
	/**x アリス(Aice) */
	private PastPlayer ranger;

	/**x ボブ(Bob) */
	private PastPlayer wingDiver;
	
	/**x チャーリー(Charlie) */
	private PastPlayer airRaider;
	
	/**x デーブ(Dave) */
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
		//x ディーラー
		// ======================================
	    pg.toadette = new PastPlayer();
	    pg.toadette.setName(el[0]);
	    pg.toadette.parseAndAdd(el[1]);
	    pg.toadette.parseAndAdd(el[2]);
	    pg.toadette.parseAndAdd(el[3]);
	    pg.toadette.parseAndAdd(el[4]);
	    pg.toadette.parseAndAdd(el[5]);

		// ======================================
		//x プレイヤー
		// ======================================
	    List<PastPlayer> bufPl = new ArrayList<>();
	    for (int lp = 0; lp < 4; lp++) {
	    	int offset = 6 + lp * 7;
	    	PastPlayer pl  = new PastPlayer();
	    	pl.setName(el[offset]);
	    	pl.parseAndAdd(el[offset + 1]);
	    	pl.parseAndAdd(el[offset + 2]);
	    	pl.parseAndAdd(el[offset + 3]);
	    	pl.parseAndAdd(el[offset + 4]);
	    	pl.parseAndAdd(el[offset + 5]);
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
	 * 各々のプレイヤーから見える状況を取得
	 * @return
	 */
	public List<PlayerContext> getPlayerContexts(){
		return Arrays.asList(
				new PlayerContext(this.ranger, this),
				new PlayerContext(this.wingDiver, this),
				new PlayerContext(this.airRaider, this),
				new PlayerContext(this.fencer, this));
	}

	/**
	 * @return the toadette
	 */
	public PastPlayer getToadette() {
		return toadette;
	}

	/**
	 * @return the ranger
	 */
	public PastPlayer getRanger() {
		return ranger;
	}

	/**
	 * @return the wingDiver
	 */
	public PastPlayer getWingDiver() {
		return wingDiver;
	}

	/**
	 * @return the airRaider
	 */
	public PastPlayer getAirRaider() {
		return airRaider;
	}

	/**
	 * @return the fencer
	 */
	public PastPlayer getFencer() {
		return fencer;
	}


}
