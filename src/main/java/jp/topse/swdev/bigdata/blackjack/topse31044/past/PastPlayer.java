package jp.topse.swdev.bigdata.blackjack.topse31044.past;

import jp.topse.swdev.bigdata.blackjack.Card;

public class PastPlayer {
	
	/** プレーヤー名 */
	private String name = "";
	
	/** 勝敗 */
	private Result result = Result.LOSE;
	
	/** 手札 */
	private Card[] tefuda = new Card[5];
	

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
	public Result getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void parseResult(String result) {
		try {
			this.result = Result.valueOf(result);
		} catch(Exception e) {
			this.result = null;
		}
	}
	
	public Card first() {
		return this.cardAt(0);
	}

	/**
	 * @return the tefuda
	 */
	public Card cardAt(int index) {
		return tefuda[index];
	}

	/**
	 * @param tefuda the tefuda to set
	 */
	public void parseCardNameAt(int index, String card) {
		try {
			this.tefuda[index] = Card.valueOf(card);
		} catch(Exception e) {
			this.tefuda[index] = null;
		}
	}
}
