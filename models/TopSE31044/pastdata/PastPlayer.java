package jp.topse.swdev.bigdata.blackjack.topse31044.pastdata;

import java.util.ArrayList;
import java.util.List;

import jp.topse.swdev.bigdata.blackjack.Card;
import jp.topse.swdev.bigdata.blackjack.Game;
import jp.topse.swdev.bigdata.blackjack.Hand;
import jp.topse.swdev.bigdata.blackjack.Player;
import jp.topse.swdev.bigdata.blackjack.Result.Type;
import jp.topse.swdev.bigdata.blackjack.topse31044.Topse31044Utils;

public class PastPlayer implements Cloneable{

	/**x プレーヤー名 */
	private String name = "";

	/**x 勝敗 */
	private Type result = Type.LOSE;

	/**x 手札 */
	private Hand hand = new Hand();

	public PastPlayer() {
	}

	/**
	 *
	 * @param pl
	 * @param gm
	 * @return
	 */
	public static PastPlayer convert(Player pl, Game gm) {
		PastPlayer pp = new PastPlayer();

		pp.name = pl.getName();

		Hand hnd = gm.getPlayerHands().get(pl);

		for (int lp = 0; lp < hnd.getCount(); lp++) {
			pp.hand.add(hnd.get(lp));
		}
		return pp;
	}

	/**
	 * 結果から列挙型を解析し、セット
	 * @param result 結果
	 */
	public void parseResult(String result) {
		try {
			this.result = Type.valueOf(result);
		} catch(Exception e) {
			this.result = null;
		}
	}

	/**
	 * 手札の1枚目を取得
	 * @return カード
	 */
	public Card first() {
		return this.cardAtOrDefault(0);
	}

	/**
	 * 手札の最後のカードを取得
	 * @return カード
	 */
	public Card last() {
		int idx = this.hand.getCount() - 1;
		return this.cardAtOrDefault(idx);
	}

	/**
	 * 手札の何番目かを取得
	 * @param hand 何番目か
	 * @return カード
	 */
	public Card cardAtOrDefault(int index) {
		if(this.hand.getCount() <= index) {
			return null;
		}

		return this.hand.get(index);
	}

	/**
	 * カード名から列挙型を解析し、追加
	 * 空白、または解析に失敗したら無視。
	 * @param index 位置
	 * @param card カード名
	 */
	public void addOrAbort(String card) {
		if (Topse31044Utils.isNullOrWhiteSpace(card)) {
			return;
		}

		try {
			this.hand.add(Card.valueOf(card));
		} finally {
		}
	}

	/**
	 * 空いてる手札にカードを突っ込む。
	 * @param c
	 * @return
	 */
	public void add(Card card) {
		this.hand.add(card);
	}


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the result
	 */
	public Type getResult() {
		return result;
	}

	public Hand getTefuda() {
		return hand;
	}

	public List<Card> getCards() {
		List<Card> arr = new ArrayList<>();

		for (int lp = 0; lp < this.hand.getCount(); lp++) {
			arr.add(this.getTefuda().get(lp));
		}

		return arr;
	}

	public boolean isBust() {
		return this.hand.eval() > 21;
	}
}
