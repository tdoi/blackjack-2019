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
	 * 戦略…カードを引かない・カードを引いたら1がでた・2が出た・・・を仮定し、
	 * それを機械学習モデルにぶち込んで、かつパターンのみを抽出し、ウェイトをもとに確立計算して
	 * 50%以上だったらレイズする。
	 */
	@Override
	public Action decide(Player player, Game game) {
		int currentNum = game.getPlayerHands().get(player).eval();

		// =========================
		//x 悩むまでもないロジック
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
			Classifier aiModelResult = (Classifier) SerializationHelper.read("H:/git/blackjack-2019/src/main/java/jp/topse/swdev/bigdata/blackjack/topse31044/past/topse31044_2019.model");

			//x ひかなかったことを仮定して機械学習モデルにブチ込む
			//x 勝利なら2、ドローなら1、それ以外は0とし、これの13倍をAとする
			Instances predictArff = Csv2Arff.addToInstancesOrSpawn(null, context);

			int nonDraw = (int) new Evaluation(predictArff).evaluateModelOnce(aiModelResult, predictArff.firstInstance());
			int nonDrawWeighted = nonDraw * 13;
			
			double sum = 0.0;
			
			for(Weight elm : Weight.values()) {
				PlayerContext supposing = context.supposeIfDrew(elm.getCard());
				Instances supposingArff = Csv2Arff.addToInstancesOrSpawn(null, supposing);

				//x 勝利なら2、ドローなら1、それ以外は0とし、その数値とそのカードのウェイトをかける
				double result = (double) new Evaluation(supposingArff).evaluateModelOnce(aiModelResult, supposingArff.firstInstance());
				//x 計算結果を集積し、Bとする
				sum += result * elm.getWeight();
			}
			
			System.out.println(nonDrawWeighted);
			System.out.println("VS");
			System.out.println(sum);
			
			//A > B ならスタンド
			//x それ以外なら引く
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Type guess() {

//	        int predictedValue = Integer.MAX_VALUE;
//	        try {
//	            if (aiModelResult == null) {
//	                aiModelResult = (Classifier) SerializationHelper.read(CLASSIFIER_MODEL_RESULT);
//	            }
//	            List<DataModelResult> predictData = new ArrayList<>();
//	            predictData.add(new DataModelResult(dealerUpCard, hand1, hand2, hand3, hand4, hand5, Result.Type.WIN)); // 結果の引数はダミー
//	            Instances predictArff = dataResult2arff(predictData);
//	            predictedValue = (int) new Evaluation(predictArff).evaluateModelOnce(aiModelResult, predictArff.firstInstance());
//	        } catch (Exception e) {
//	            e.printStackTrace();
//	        }
//	        return (predictedValue == Integer.MAX_VALUE) ? null : CLASS_VALUES_RESULT[predictedValue];
		return null;
	}
}
