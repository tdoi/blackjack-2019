package jp.topse.swdev.bigdata.blackjack.topse31044.past;

import jp.topse.swdev.bigdata.blackjack.Card;
import jp.topse.swdev.bigdata.blackjack.Game;
import jp.topse.swdev.bigdata.blackjack.Hand;
import jp.topse.swdev.bigdata.blackjack.Player;
import jp.topse.swdev.bigdata.blackjack.Result.Type;

public class PastPlayer {
	
	/**x プレーヤー名 */
	private String name = "";
	
	/**x 勝敗 */
	private Type result = Type.LOSE;
	
	/**x 手札 */
	private Card[] tefuda = new Card[5];
	
	public PastPlayer() {
	}
	
	public PastPlayer(PastPlayer arg) {
		PastPlayer pp = new PastPlayer();
		pp.name = arg.name;
		pp.result = arg.result;
		pp.tefuda = arg.tefuda.clone();
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
		
		Hand hd = gm.getPlayerHands().get(pl);
		
		for (int lp = 0; lp < hd.getCount(); lp++) {
			pp.tefuda[lp] = hd.get(lp);
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
		return this.cardAt(0);
	}

	/**
	 * 手札の何番目かを取得
	 * @param tefuda 何番目か
	 * @return カード
	 */
	public Card cardAt(int index) {
		return tefuda[index];
	}

	/**
	 * カード名から列挙型を解析し、指定の位置にセット
	 * @param index 位置
	 * @param card カード名
	 */
	public void parseCardNameAt(int index, String card) {
		try {
			this.tefuda[index] = Card.valueOf(card);
		} catch(Exception e) {
			this.tefuda[index] = null;
		}
	}
	
	/**
	 * 空いてる手札にカードを突っ込む。すでに満タンなら何もしない。
	 * @param c
	 * @return 
	 */
	public boolean push(Card cd) {
		for(int lp =0; lp < this.tefuda.length; lp++) {
			if (null != this.tefuda[lp]) {
				continue;
			}
			
			this.tefuda[lp] = cd;
			return true;
		}
		
		return false;
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
}
