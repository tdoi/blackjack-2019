package jp.topse.swdev.bigdata.blackjack.topse31044.past;

import java.util.ArrayList;
import java.util.List;

import jp.topse.swdev.bigdata.blackjack.Card;
import jp.topse.swdev.bigdata.blackjack.Game;
import jp.topse.swdev.bigdata.blackjack.Hand;
import jp.topse.swdev.bigdata.blackjack.Player;
import jp.topse.swdev.bigdata.blackjack.Result.Type;

public class PastPlayer implements Cloneable{
	
	/**x プレーヤー名 */
	private String name = "";
	
	/**x 勝敗 */
	private Type result = Type.LOSE;
	
	/**x 手札 */
	private ArrayList<Card> tefuda = new ArrayList<>();
	
	public PastPlayer() {
	}
	
	@SuppressWarnings("unchecked")
	public PastPlayer(PastPlayer arg) {
		PastPlayer pp = new PastPlayer();
		pp.name = arg.name;
		pp.result = arg.result;
		pp.tefuda = (ArrayList<Card>) arg.tefuda.clone();
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
			pp.tefuda.add(hd.get(lp));
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
	 * 手札の何番目かを取得
	 * @param tefuda 何番目か
	 * @return カード
	 */
	public Card cardAtOrDefault(int index) {
		if(this.tefuda.size() <= index) {
			return null;
		}
		
		return this.tefuda.get(index);
	}

	/**
	 * カード名から列挙型を解析し、追加
	 * @param index 位置
	 * @param card カード名
	 */
	public void parseAndAdd(String card) {
		try {
			this.tefuda.add(Card.valueOf(card));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 空いてる手札にカードを突っ込む。
	 * @param c
	 * @return 
	 */
	public void add(Card card) {
		this.tefuda.add(card);
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
	
	@SuppressWarnings("unchecked")
	public Object clone() {
		PastPlayer clone = new PastPlayer();
		clone.name = this.name;
		clone.result = this.result;
		clone.tefuda = (ArrayList<Card>) this.tefuda.clone();
		return clone;
	}
}
