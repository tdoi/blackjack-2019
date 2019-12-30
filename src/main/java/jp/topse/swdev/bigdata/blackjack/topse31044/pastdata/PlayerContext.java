package jp.topse.swdev.bigdata.blackjack.topse31044.pastdata;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import jp.topse.swdev.bigdata.blackjack.Card;
import jp.topse.swdev.bigdata.blackjack.Game;
import jp.topse.swdev.bigdata.blackjack.Hand;
import jp.topse.swdev.bigdata.blackjack.Player;

/**
 * あるプレイヤーから見える場の状況。自分の手札と、各プレイヤーの1枚目と、勝敗の結果。
 * @author topse31044
 *
 */
public class PlayerContext implements Cloneable{
	private PastPlayer player;
	private Card dealerFirst;
	private ArrayList<Card> publicInfo = new ArrayList<>();

	public PlayerContext() {
	}

	public PlayerContext(PastPlayer player, PastGame pg) {
		this.player = player;

		// ディーラーの1枚目
		this.dealerFirst = pg.getDealer().first();

		boolean[] afterFromMe = {false};
		pg.getPlayers().forEach(elm -> {
			// 自分より後のプレイヤーは最初の1枚だけ
			if (afterFromMe[0]) {
				this.publicInfo.add(elm.first());
				return;
			}

			// 自分より前のプレイヤーは全てみれる。
			if (player == elm) {
				afterFromMe[0] = true;
			}
			elm.getCards().forEach(this.publicInfo::add);
		});
	}

	public PlayerContext(Player player, Game pg) {
		this.player = PastPlayer.convert(player, pg);
		this.publicInfo = new ArrayList<>();

		this.publicInfo.add(pg.getUpCard());

		pg.getPlayerHands().values().forEach(elm -> {
			this.publicInfo.add(elm.get(0));
		});
	}

	/**
	 * @return the player
	 */
	public PastPlayer getPlayer() {
		return player;
	}

	/**
	 * @param player the player to set
	 */
	public void setPlayer(PastPlayer player) {
		this.player = player;
	}

	/**
	 * @return the publicInfo
	 */
	public List<Card> getPublicInfo() {
		return publicInfo;
	}

	public static int getValueOrDefault(Card card) {
		if (null == card) {
			return 0;
		}

		return card.getValue();
	}

	public String getResultInName() {
		return this.player.getResult().name();
	}

	/**
	 * @return the player
	 */
	public int getPlayerCardIndex(int pos) {
		Hand hn = this.getPlayer().getTefuda();

		if (pos >= hn.getCount()) {
			return 0;
		}

		return hn.get(pos).getIndex();
	}

	public int getDealerFirstIndex() {
		return dealerFirst.getIndex();
	}

	public int handCount() {
		return player.getTefuda().getCount();
	}


	@Override
	public String toString() {
		return this.player.getName() + ":"
				+ this.player.getResult() + "/"
				+ "count=" + this.player.getTefuda().getCount() + "/"
				+ "last=" + this.player.last() + "/"
				+ "hand=" + this.player.getCards() + "/"
				+ "dealer=" + this.dealerFirst + "/"
				+ "stats=" + this.getStats();
	}

	public PlayerContext supposeIfDrew(Card card) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	/**
	 * 公開情報カードの統計。
	 * @return
	 */
	public LinkedHashMap<Card, Long> getStats() {
		List<Card> plcd = this.getPublicInfo();

		LinkedHashMap<Card, Long> stats = new LinkedHashMap<>();
		for(Card cd : Card.values()) {
			stats.put(cd,
					plcd.stream().filter(elm -> elm == cd).count()
					);
		}

		return stats;
	}
}