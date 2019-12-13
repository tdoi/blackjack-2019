package jp.topse.swdev.bigdata.blackjack.topse31044.past;

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
public class PlayerContext{
	private PastPlayer player;
	private List<Card> publicInfo;
	
	public PlayerContext() {
	}
	
	public PlayerContext(PastPlayer player, PastGame pg) {
		this.player = player;
		this.publicInfo = new ArrayList<>();
		this.publicInfo.add(pg.getToadette().first());
		this.publicInfo.add(pg.getRanger().first());
		this.publicInfo.add(pg.getWingDiver().first());
		this.publicInfo.add(pg.getAirRaider().first());
		this.publicInfo.add(pg.getFencer().first());
	}
	
	public PlayerContext(Player player, Game pg) {
		this.player = PastPlayer.convert(player, pg);
		this.publicInfo = new ArrayList<>();
		
		this.publicInfo.add(pg.getUpCard());
		
		pg.getPlayerHands().values().forEach(elm -> {
			this.publicInfo.add(elm.get(0));
		});
	}
	
	public PlayerContext(PlayerContext context) {
		PlayerContext pc = new PlayerContext();
		pc.player = new PastPlayer(context.player);
		pc.publicInfo = new ArrayList<Card>(context.publicInfo);
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

	/**
	 * @param publicInfo the publicInfo to set
	 */
	public void setPublicInfo(List<Card> publicInfo) {
		this.publicInfo = publicInfo;
	}
	
	public static int getValueOrDefault(Card card) {
		if (null == card) {
			return 0;
		}
		
		return card.getValue();
	}
	
	public double getThisFirst() {
		return getValueOrDefault(this.getPlayer().cardAt(0));
	}
	
	public double getThisSecond() {
		return getValueOrDefault(this.getPlayer().cardAt(1));
	}
	
	public double getThisThird() {
		return getValueOrDefault(this.getPlayer().cardAt(2));
	}
	
	public double getThisFourth() {
		return getValueOrDefault(this.getPlayer().cardAt(3));
	}
	
	public double getThisFifth() {
		return getValueOrDefault(this.getPlayer().cardAt(4));
	}
	
	public double getFirstPublic() {
		return getValueOrDefault(this.publicInfo.get(0));
	}
	
	public double getSecondPublic() {
		return getValueOrDefault(this.publicInfo.get(1));
	}
	
	public double getThirdPublic() {
		return getValueOrDefault(this.publicInfo.get(2));
	}
	
	public double getFourthPublic() {
		return getValueOrDefault(this.publicInfo.get(3));
	}
	
	public double getFifthPublic() {
		return getValueOrDefault(this.publicInfo.get(4));
	}
	
	public String getResultInName() {
		return this.player.getResult().name();
	}
	
	public PlayerContext supposeIfDrew(Card cd) {
		try {
			PlayerContext clone = new PlayerContext(this);
			clone.player.push(cd);
			return clone;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
}
