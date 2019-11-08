package jp.topse.swdev.bigdata.blackjack.topse31044;

import java.util.ArrayList;
import java.util.List;

public class Player {
	/** プレーヤー名 */
	private String name;
	
	/** 勝敗 */
	private int winner;
	
	/** 手札 */
	private List<String> tefuda;
	
	/**
	 * コンストラクター
	 * @param string
	 */
	public Player(String name) {
		this.name = name;
		this.tefuda = new ArrayList<String>();
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
	 * @return the winner
	 */
	public int getWinner() {
		return winner;
	}

	/**
	 * @param winner the winner to set
	 */
	public void setWinner(int winner) {
		this.winner = winner;
	}

	/**
	 * @param winner the winner to set
	 */
	public void parseWinner(String winner) {
		if (null == winner) {
			this.winner = -1;
			return;
		} 
		String upWinner = winner.toUpperCase();
		
		if ("WIN".equals(upWinner)) {
			this.winner = 1;
		} else if ("LOSE".equals(upWinner)) {
			this.winner = -1;
		} else {
			this.winner = 0;
		}
	}

	/**
	 * @return the tefuda
	 */
	public List<String> getTefuda() {
		return tefuda;
	}

	/**
	 * @param tefuda the tefuda to set
	 */
	public void setTefuda(List<String> tefuda) {
		this.tefuda = tefuda;
	}

	/**
	 * @param tefuda the tefuda to set
	 */
	public void addTefuda(String... cards) {
		for(String card : cards) {
			this.tefuda.add(card);
		}
	}
	
	public int getSum() {
		int sum = 0;
		
		for (String card : this.tefuda) {
			if ( null == card ) {
				continue;
			}
			
			switch (card) {
			case "ACE":
				sum += 1; break;
			case "TWO":
				sum += 2; break;
			case "THREE":
				sum += 3; break;
			case "FOUR":
				sum += 4; break;
			case "FIVE":
				sum += 5; break;
			case "SIX":
				sum += 6; break;
			case "SEVEN":
				sum += 7; break;
			case "EIGHT":
				sum += 8; break;
			case "NINE":
				sum += 9; break;
			case "TEN": // fall through
			case "JACK": // fall through
			case "QUEEN": // fall through
			case "KING": // fall through
				sum += 10; break;
			default:break;
				
			}
		}
		
		if (this.tefuda.contains("ACE") && sum < 12) {
			sum += 10;
		}
		
		return sum;
	}
	
	@Override
	public String toString() {
		return "【" + (this.winner == 1 ? "★" : this.winner == 0 ? "☆" : "") + this.name + "(" + this.getSum() + ")】" + this.tefuda.toString(); 
	}
}
