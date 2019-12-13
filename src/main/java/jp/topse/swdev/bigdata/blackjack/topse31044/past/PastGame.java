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
	    pg.toadette.parseCardNameAt(0, el[1]);
	    pg.toadette.parseCardNameAt(1, el[2]);
	    pg.toadette.parseCardNameAt(2, el[3]);
	    pg.toadette.parseCardNameAt(3, el[4]);
	    pg.toadette.parseCardNameAt(4, el[5]);

		// ======================================
		//x プレイヤー
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
