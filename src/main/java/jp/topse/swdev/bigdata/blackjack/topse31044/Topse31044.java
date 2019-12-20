package jp.topse.swdev.bigdata.blackjack.topse31044;


import java.util.Arrays;
import java.util.Date;

import jp.topse.swdev.bigdata.blackjack.Action;
import jp.topse.swdev.bigdata.blackjack.Card;
import jp.topse.swdev.bigdata.blackjack.DecisionMaker;
import jp.topse.swdev.bigdata.blackjack.Game;
import jp.topse.swdev.bigdata.blackjack.Player;
import jp.topse.swdev.bigdata.blackjack.Result.Type;
import jp.topse.swdev.bigdata.blackjack.topse31044.past.Arff2Model;
import jp.topse.swdev.bigdata.blackjack.topse31044.past.Arff2Model.Models;
import jp.topse.swdev.bigdata.blackjack.topse31044.past.Csv2Arff;
import jp.topse.swdev.bigdata.blackjack.topse31044.past.PastGame;
import jp.topse.swdev.bigdata.blackjack.topse31044.past.PlayerContext;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.SparseInstance;

/**
 * ブラックジャック
  *  コール・レイズ判断ロジック
 * @author topse31044
 *
 */
public class Topse31044 implements DecisionMaker {
	
	private static Classifier MODEL;
	static {
		try {
			MODEL = (Classifier) SerializationHelper.read(
					"H:/git/blackjack-2019/src/main/java/jp/topse/swdev/bigdata/blackjack/topse31044/past/topse31044_2019.model");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	private enum Weight{
		ACE(Card.ACE, 62008),
		TWO(Card.TWO, 94047),
		THREE(Card.THREE, 94770),
		FOUR(Card.FOUR, 94416),
		FIVE(Card.FIVE, 93877),
		SIX(Card.SIX, 94639),
		SEVEN(Card.SEVEN, 94470),
		EIGHT(Card.EIGHT, 62222),
		NINE(Card.NINE, 61768),
		TEN(Card.TEN, 61953),
		JACK(Card.JACK, 61828),
		QUEEN(Card.QUEEN, 61789),
		KING(Card.KING, 62213);
		
		private int num;
		private Card card;
		
		private Weight(Card cd, int num) {
			this.card = cd;
			this.num = num;
		}
		
		private double getNum() {
			return this.num;
		}
		
		private static double getSum() {
			return Arrays.stream(Weight.values())
					.map(elm -> elm.getNum())
					.reduce((sum, elm) -> sum + elm)
					.get();
		}
		public Card getCard() {
			return this.card;
		}
		
		
		public double getWeight() {
			return this.getNum() / getSum();
		}
		
	}
	
	
	public static void main(String[] args) {
		try {
			spawn();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * CSVをmodelに変換
	 * @throws Exception 
	 */
	private static void spawn() throws Exception {
//		System.out.println(new Date());
//		// ==================================
//		// CSVからARFFへ
//		// ==================================
//		System.out.println("CSVをARFFへ返還");
//		Csv2Arff arff = new Csv2Arff();
//		arff.parse("H:/git/blackjack-2019/data/2019.csv");
//		System.out.println("CSV解析OK");
////		arff.save("H:/git/blackjack-2019/src/main/java/jp/topse/swdev/bigdata/blackjack/topse31044/past/topse31044_2019.arff");
//		System.out.println("CSVをARFFへ返還OK");

//		// ==================================
//		// ARFFからモデルへ
//		// ==================================
//		System.out.println("ARFFをモデルへ返還");
//		long time = System.currentTimeMillis();
//		Arff2Model model = new Arff2Model(Models.J_48);
//		model.build(arff.getArff());
//		System.out.println(System.currentTimeMillis() - time + "ms");
//		System.out.println("モデルのビルドOK");
//		model.save("H:/git/blackjack-2019/src/main/java/jp/topse/swdev/bigdata/blackjack/topse31044/past/topse31044_2019.model");
//		System.out.println("ARFFをモデルへ返還OK");
		
	}

	/**
	 * 
	 */
	@Override
	public Action decide(Player player, Game game) {
		int currentNum = game.getPlayerHands().get(player).eval();

		// =========================
		// 悩むまでもないロジック
		// =========================
		// 11以下ならカード引いてもバーストする恐れがないので引く。
		if (11 >= currentNum) {
			return Action.HIT;
		}
		
		// 21以上ならカード引いたら絶対バーストするので引かない。
		if (21 <= currentNum) {
			return Action.STAND;
		}

		// =========================
		// 12～20が悩みどころ
		// =========================
		try {
			PlayerContext context = new PlayerContext(player, game);
			

			// ひかなかったことを仮定して機械学習モデルにブチ込む
			Instances predictArff = Csv2Arff.addToInstancesOrSpawn(null, context);

			int nonDraw = (int) new Evaluation(predictArff).evaluateModelOnce(MODEL, predictArff.firstInstance());
			Type nonDrawResult = Type.values()[nonDraw];

			// ひかなかった結果勝ちならコール、負けならレイズ
			switch (nonDrawResult) {
			case WIN:
				p("NON-TSUMO: " + Action.STAND);
				return Action.STAND;
			case LOSE:
				p("NON-TSUMO: " + Action.HIT);
				return Action.HIT;
			default:
				break;
			}
			
			// ひかなかった場合ドローなら、さらに1～13それぞれツモった場合を仮定して計算する	
			double sum = 0.0;
			
			for(Weight elm : Weight.values()) {
				PlayerContext supposing = context.supposeIfDrew(elm.getCard());
				Instances supposingArff = Csv2Arff.addToInstancesOrSpawn(null, supposing);

				//x 勝利なら1、ドローなら0.5、それ以外は0とし、その数値とそのカードのウェイトをかける
				double result = (double) new Evaluation(supposingArff).evaluateModelOnce(MODEL, supposingArff.firstInstance());
				sum += result * elm.getWeight();
			}
			p(sum * 50  + "%");

			// 勝つ可能性が低いならコール、高いならレイズ
			Action act = Type.DRAW.ordinal() > sum ? Action.STAND : Action.HIT;
			p("TSUMO: " + act);
			
			return act;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void p(String strings) {
		p(strings, true);
	}
	
	public void p(String strings, boolean ln) {
		if(true) {return;}
		
		if (ln) {
			System.out.println(strings);
		}else {
			System.out.print(strings);
		}
	}
}
