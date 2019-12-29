package jp.topse.swdev.bigdata.blackjack.topse31044.pastdata;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PastGame {
	/**x ディーラー(Dealer) */
	private PastPlayer dealer;

	/**x プレーヤーリスト */
	private ArrayList<PastPlayer> players;

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
	    pg.dealer = new PastPlayer();
	    pg.dealer.setName(el[0]);
	    pg.dealer.parseAndAdd(el[1]);
	    pg.dealer.parseAndAdd(el[2]);
	    pg.dealer.parseAndAdd(el[3]);
	    pg.dealer.parseAndAdd(el[4]);
	    pg.dealer.parseAndAdd(el[5]);

		// ======================================
		// プレイヤー
		// ======================================
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
	    	pg.players.add(pl);
	    }

	    return pg;
	}

	/**
	 * 各々のプレイヤーから見える状況を取得
	 * @return
	 */
	public List<PlayerContext> getPlayerContexts(){
		return this.getPlayers().stream().map(elm ->
			new PlayerContext(elm, this)).collect(Collectors.toList());
	}

	public PastPlayer getDealer() {
		return dealer;
	}

	public void setDealer(PastPlayer dealer) {
		this.dealer = dealer;
	}

	public ArrayList<PastPlayer> getPlayers() {
		return players;
	}

}
