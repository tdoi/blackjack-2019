package jp.topse.swdev.bigdata.blackjack.topse31044;


import java.util.Arrays;
import java.util.Date;

import jp.topse.swdev.bigdata.blackjack.Action;
import jp.topse.swdev.bigdata.blackjack.Card;
import jp.topse.swdev.bigdata.blackjack.DecisionMaker;
import jp.topse.swdev.bigdata.blackjack.Game;
import jp.topse.swdev.bigdata.blackjack.Player;
import jp.topse.swdev.bigdata.blackjack.Result.Type;
import jp.topse.swdev.bigdata.blackjack.topse31044.pastdata.Arff2Model;
import jp.topse.swdev.bigdata.blackjack.topse31044.pastdata.Arff2Model.Models;
import jp.topse.swdev.bigdata.blackjack.topse31044.pastdata.Csv2Arff;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.SerializationHelper;

/**
 * ブラックジャック
  *  コール・レイズ判断ロジック
 * @author topse31044
 *
 */
public class Topse31044 implements DecisionMaker {

	private static final String BASE_PATH =
			"C:/Program Files/eclipse/workspace/blackjack-2019/src/main/java/jp/topse/swdev/bigdata/blackjack/topse31044/pastdata/";
//			"H:/git/blackjack-2019/src/main/java/jp/topse/swdev/bigdata/blackjack/topse31044/pastdata/";

	private static Classifier MODEL;

	public static void main(String[] args) {
		try {
			spawn();
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



	/**
	 * CSVをmodelに変換
	 * @throws Exception
	 */
	private static void spawn() throws Exception {
		System.out.println(new Date());
		// ==================================
		// CSVからARFFへ
		// ==================================
		System.out.println("CSVをARFFへ返還");
		Csv2Arff arff = new Csv2Arff();
		arff.parse("H:\\git\\blackjack-2019\\data\\2019.csv");
		System.out.println("CSV解析OK");
		arff.save("H:\\git\\blackjack-2019\\src\\main\\java\\jp\\topse\\swdev\\bigdata\\blackjack\\topse31044\\pastdata\\topse31044_2019_hit_to_bust.arff");
		System.out.println("CSVをARFFへ返還OK");

		// ==================================
		// ARFFからモデルへ
		// ==================================
		System.out.println("ARFFをモデルへ返還");
		long time = System.currentTimeMillis();
		Arff2Model model = new Arff2Model(Models.J_48);
		model.build(arff.getArff());
		System.out.println(System.currentTimeMillis() - time + "ms");
		System.out.println("モデルのビルドOK");
//		model.toVisual();

		model.save("H:\\git\\blackjack-2019\\src\\main\\java\\jp\\topse\\swdev\\bigdata\\blackjack\\topse31044\\pastdata\\topse31044_2019_hit_to_bust.model");
		System.out.println("ARFFをモデルへ返還OK");

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
		return this.logic_v1(player, game);
	}

	/**
	 * 判断ロジックv1
	 * @param player
	 * @param game
	 * @return
	 */
	public Action logic_v2(Player player, Game game) {
		// ==================================
		// このまま引かなければ勝てるのかどうか判断
		// ==================================
		// 現在の枚数が2枚か、3枚か、4枚かで使うモデルを分ける

		// 勝てるのならばスタンドを返す
		// 負けるのならば次のロジックへ

		// ==================================
		// 引いてバーストしないか判断
		// ==================================
		// 現在の枚数が2枚か、3枚か、4枚かで使うモデルを分ける

		// バーストするならヒットを返す
		// バーストしないならスタンドを返す

		return null;
	}

	/**
	 * 判断ロジックv1
	 * @param player
	 * @param game
	 * @return
	 */
	public Action logic_v1(Player player, Game game) {
		if (null == Topse31044.MODEL) {
			try {
				Topse31044.MODEL = (Classifier) SerializationHelper.read(
						"C:\\Program Files\\eclipse\\workspace\\blackjack-2019\\src\\main\\java\\jp\\topse\\swdev\\bigdata\\blackjack\\topse31044\\past\\topse31044_2019.model");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			// =============================
			// このまま引かなければ勝てるのか判断
			// =============================
			jp.topse.swdev.bigdata.blackjack.topse31044.past.PlayerContext context
				= new jp.topse.swdev.bigdata.blackjack.topse31044.past.PlayerContext(player, game);
			Instances predictArff = jp.topse.swdev.bigdata.blackjack.topse31044.past.Csv2Arff.addToInstancesOrSpawn(null, context);

			// 現在の状況を機械学習にブチ込む
			int nonDraw = (int) new Evaluation(predictArff).evaluateModelOnce(MODEL, predictArff.firstInstance());
			Type nonDrawResult = Type.values()[nonDraw];

			// ひかなかった結果勝ちならスタンド、負けならカードを自摸る
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

			// =============================
			// ドローの場合、1～13をそれぞれ自摸った場合を仮定し勝敗を予測し、
			// カードの出現確率でそれらを重みづけをして加算する
			// =============================
			double sum = 0.0;

			for(Weight elm : Weight.values()) {
				jp.topse.swdev.bigdata.blackjack.topse31044.past.PlayerContext supposing
					= context.supposeIfDrew(elm.getCard());
				Instances supposingArff = jp.topse.swdev.bigdata.blackjack.topse31044.past.Csv2Arff.addToInstancesOrSpawn(null, supposing);

				//勝利なら1、ドローなら0.5、それ以外は0とし、その数値とそのカードのウェイトをかける
				double result = (double) new Evaluation(supposingArff).evaluateModelOnce(MODEL, supposingArff.firstInstance());
				sum += result * elm.getWeight();
			}
			p(sum * 50  + "%");

			// 勝つ可能性が低いならスタンド、高いなら自模る
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
