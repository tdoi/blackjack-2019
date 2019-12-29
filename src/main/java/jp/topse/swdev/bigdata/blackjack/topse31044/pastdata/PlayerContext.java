package jp.topse.swdev.bigdata.blackjack.topse31044.pastdata;

import java.util.ArrayList;
import java.util.List;

import jp.topse.swdev.bigdata.blackjack.Card;
import jp.topse.swdev.bigdata.blackjack.Game;
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

		boolean[] future = {true};
		pg.getPlayers().forEach(elm -> {

			// 他プレーヤーの1枚目
			this.publicInfo.add(elm.first());

			// 自分より前のプレイやーはすべて見れる
			if (future[0]) {
				return;
			}
			if (player == elm) {
				future[0] = true;
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
	public int getPlayerCardIndex(int index) {
		Card cd = this.getPlayer().getTefuda().get(index);

		return null == cd ? 0 : cd.getIndex();
	}

	public int getDealerFirstIndex() {
		return dealerFirst.getIndex();
	}
}