package jp.topse.swdev.bigdata.blackjack.topse31044.pastdata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jp.topse.swdev.bigdata.blackjack.Card;
import jp.topse.swdev.bigdata.blackjack.Result.Type;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.SparseInstance;
import weka.core.converters.ArffSaver;

/**
 * CSVからARFFにするためのクラス
 * @author topse31044
 *
 */
public class Csv2Arff {

	private final static String[] RESULT_TYPES = {
			Type.LOSE.name(), Type.DRAW.name(), Type.WIN.name()};

	private final static String[] CARDS = {
			Card.ACE.name(), Card.TWO.name(), Card.THREE.name(),
			Card.FOUR.name(), Card.FIVE.name(), Card.SIX.name(),
			Card.SEVEN.name(), Card.EIGHT.name(), Card.NINE.name(),
			Card.TEN.name(), Card.JACK.name(), Card.QUEEN.name(),
			Card.KING.name()};


	private final static String[] BUSTING_STATES = {
			Boolean.FALSE.toString(), Boolean.TRUE.toString()};

	/** ARFFデータ */
	private Instances arff;

	public void parse(String filePath) {
		// =====================
		// ファイル読み込み
		// =====================
		File file = new File(filePath);
		try(FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);){

			// =====================
			// 過去の結果を構造化
			// =====================
			Stream<PastGame> rawList = br.lines().map(
					elm -> PastGame.parse(elm));
			List<PastGame> pastGames  = rawList.collect(Collectors.toList());

//			// 高速化のための間引き
//			pastGames = pastGames.subList(0, 4000);

			// =====================
			//  構造化したものをARFFに変換
			// =====================
			Instances[] data = new Instances[1];
			pastGames.forEach(elm ->
				elm.getPlayerContexts().forEach(elm2 ->
					data[0] = Csv2Arff.addOrSpawnIsBustInstances(data[0], elm2))
			);

			this.arff = data[0];
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	/**
//	 *
//	 * @param arg
//	 * @param pc
//	 * @return
//	 */
//	public static Instances addOrSpawnNextTsumoGuesser(Instances arg, PlayerContext pc) {
//		Instances data = arg;
//
//		// ================================
//		// ARFFのフォーマット初期化
//		// ================================
//		if (null == data) {
//			ArrayList<Attribute> attr = new ArrayList<>();
//			attr.add(new Attribute("count"));
//			attr.add(new Attribute("my_first"));
//			attr.add(new Attribute("my_second"));
//			attr.add(new Attribute("my_third"));
//			attr.add(new Attribute("my_forth"));
//			attr.add(new Attribute("my_fifth"));
//			attr.add(new Attribute("dealer"));
//			attr.add(new Attribute("ace_count"));
//			attr.add(new Attribute("two_count"));
//			attr.add(new Attribute("three_count"));
//			attr.add(new Attribute("four_count"));
//			attr.add(new Attribute("five_count"));
//			attr.add(new Attribute("six_count"));
//			attr.add(new Attribute("seven_count"));
//			attr.add(new Attribute("eight_count"));
//			attr.add(new Attribute("nine_count"));
//			attr.add(new Attribute("ten_count"));
//			attr.add(new Attribute("jack_count"));
//			attr.add(new Attribute("queen_count"));
//			attr.add(new Attribute("king_count"));
//			attr.add(newAttributes("next_tsumo", CARDS));
////			attr.add(newAttributes("results", RESULT_TYPES));
//			data = new Instances("data1", attr, 0);
//		}
//
//		// =======================================
//		// ARFFにデータをぶち込む
//		// =======================================
//		ArrayList<Integer> values = new ArrayList<>();
//
//		// プレーヤーの手札
//		values.add(pc.getPlayer().getTefuda().getCount());
//		values.add(pc.getPlayerCardIndex(0));
//		values.add(pc.getPlayerCardIndex(1));
//		values.add(pc.getPlayerCardIndex(2));
//		values.add(pc.getPlayerCardIndex(3));
//		values.add(pc.getPlayerCardIndex(4));
//		values.add(pc.getDealerFirstIndex());
//
//		// 公開情報
//		pc.getStats().forEach((card, count)->{
//			values.add(count.intValue());
//		});
//
//		// 評価したい情報
//		String lastCardName = pc.getPlayer().last().name();
//		values.add(data.attribute(values.size()).indexOfValue(lastCardName)); // 次にツモるカード
//		//		values.add(data.attribute(values.size()).indexOfValue(pc.getResultInName())); // 勝敗
//
//		// =====================
//		// ARFFに変換
//		// =====================
//		double[] a = values.stream().mapToDouble(elm -> elm).toArray();
//		 //  評価基準
//		data.setClassIndex(a.length - 1);
//		data.add(new SparseInstance(1.0, a));
//
//		return data;
//	}
//
	/**
	 * カードをツモったらバストするかどうか予測するインスタンスを作成する
	 * @param arg
	 * @param pc
	 * @return
	 */
	public static Instances addOrSpawnIsBustInstances(Instances arg, PlayerContext pc) {
		Instances data = arg;

		// ================================
		// ARFFのフォーマット初期化
		// ================================
		if (null == data) {
			ArrayList<Attribute> attr = new ArrayList<>();
			attr.add(new Attribute("my_first"));
			attr.add(new Attribute("my_second"));
			attr.add(new Attribute("my_third"));
			attr.add(new Attribute("my_forth"));
			attr.add(new Attribute("ace_count"));
			attr.add(new Attribute("two_count"));
			attr.add(new Attribute("three_count"));
			attr.add(new Attribute("four_count"));
			attr.add(new Attribute("five_count"));
			attr.add(new Attribute("six_count"));
			attr.add(new Attribute("seven_count"));
			attr.add(new Attribute("eight_count"));
			attr.add(new Attribute("nine_count"));
			attr.add(new Attribute("ten_count"));
			attr.add(new Attribute("jack_count"));
			attr.add(new Attribute("queen_count"));
			attr.add(new Attribute("king_count"));
			attr.add(newAttributes("is_bust", BUSTING_STATES));
//			attr.add(newAttributes("next_tsumo", CARDS));
//			attr.add(newAttributes("results", RESULT_TYPES));
			data = new Instances("data1", attr, 0);
		}

		// =======================================
		// ARFFにデータをぶち込む
		// =======================================
		ArrayList<Integer> values = new ArrayList<>();

		// ●自分の手札
		// 2枚以下ではバストしないので、学習対象から外す。
		int cnt = pc.getPlayer().getTefuda().getCount();
		if(cnt <= 2) return data;

		values.add(pc.getPlayerCardIndex(0));
		values.add(pc.getPlayerCardIndex(1));

		// 今さっき引いてきたカードは伏せておく（学習対象）
		values.add(cnt == 3 ? 0 : pc.getPlayerCardIndex(2));
		values.add(cnt == 4 ? 0 : pc.getPlayerCardIndex(3));

		// 5枚目が学習対象になることはないので、対象から除外
		//values.add(cnt < 6 ? 0 : pc.getPlayerCardIndex(4));

		// ●公開情報
		pc.getStats().forEach((card, count)->{
			int num = count.intValue();

			// 予測したいカードを統計から外す。
			if (card == pc.getPlayer().last()) {
				num--;
			}
			values.add(num);
		});

		// ●評価したい情報
		// バストかどうか
		String isbust = Boolean.toString(pc.getPlayer().isBust());
		values.add(data.attribute(values.size()).indexOfValue(isbust));

		// 次にツモるカード
//		String lastCardName = pc.getPlayer().last().name();
//		values.add(data.attribute(values.size()).indexOfValue(lastCardName));

		// 勝敗
//		values.add(data.attribute(values.size()).indexOfValue(pc.getResultInName()));

		// =====================
		// ARFFに変換
		// =====================
		double[] a = values.stream().mapToDouble(elm -> elm).toArray();
		 //  評価基準
		data.setClassIndex(a.length - 1);
		data.add(new SparseInstance(1.0, a));

		return data;
	}

	/**
	 * スタンドしたら勝つかどうか予測するインスタンスを作成する
	 * @param arg
	 * @param pc
	 * @return
	 */
	public static Instances addOrSpawnStandToWinInstances(Instances arg, PlayerContext pc) {
		Instances data = arg;

		// ================================
		// ARFFのフォーマット初期化
		// ================================
		if (null == data) {
			ArrayList<Attribute> attr = new ArrayList<>();
			attr.add(new Attribute("my_first"));
			attr.add(new Attribute("my_second"));
			attr.add(new Attribute("my_third"));
			attr.add(new Attribute("my_forth"));
			attr.add(new Attribute("dealer"));
			attr.add(new Attribute("ace_count"));
			attr.add(new Attribute("two_count"));
			attr.add(new Attribute("three_count"));
			attr.add(new Attribute("four_count"));
			attr.add(new Attribute("five_count"));
			attr.add(new Attribute("six_count"));
			attr.add(new Attribute("seven_count"));
			attr.add(new Attribute("eight_count"));
			attr.add(new Attribute("nine_count"));
			attr.add(new Attribute("ten_count"));
			attr.add(new Attribute("jack_count"));
			attr.add(new Attribute("queen_count"));
			attr.add(new Attribute("king_count"));
//			attr.add(newAttributes("is_bust", BUSTING_STATES));
//			attr.add(newAttributes("next_tsumo", CARDS));
			attr.add(newAttributes("results", RESULT_TYPES));
			data = new Instances("data1", attr, 0);
		}

		// =======================================
		// ARFFにデータをぶち込む
		// =======================================
		ArrayList<Integer> values = new ArrayList<>();

		// ●自分の手札
		// 5枚以上では必ずスタンドするので、学習対象から外す。
		int cnt = pc.getPlayer().getTefuda().getCount();
		if(cnt >= 5) return data;

		values.add(pc.getPlayerCardIndex(0));
		values.add(pc.getPlayerCardIndex(1));

		// 今さっき引いてきたカードは伏せておく（学習対象）
		values.add(cnt == 3 ? 0 : pc.getPlayerCardIndex(2));
		values.add(cnt == 4 ? 0 : pc.getPlayerCardIndex(3));

		// 5枚目が学習対象になることはないので、対象から除外
		//values.add(cnt < 6 ? 0 : pc.getPlayerCardIndex(4));

		// ●ディーラーの1枚目
		values.add(pc.getDealerFirstIndex());

		// ●公開情報
		pc.getStats().forEach((card, count)->{
			int num = count.intValue();

			// 予測したいカードを統計から外す。
			if (card == pc.getPlayer().last()) {
				num--;
			}

			values.add(num);
		});

		// ●評価したい情報
//		// バストかどうか
//		String isbust = Boolean.toString(pc.getPlayer().isBust());
//		values.add(data.attribute(values.size()).indexOfValue(isbust));

		// 次にツモるカード
//		String lastCardName = pc.getPlayer().last().name();
//		values.add(data.attribute(values.size()).indexOfValue(lastCardName));

		// 勝敗
		values.add(data.attribute(values.size()).indexOfValue(pc.getResultInName()));

		// =====================
		// ARFFに変換
		// =====================
		double[] a = values.stream().mapToDouble(elm -> elm).toArray();
		 //  評価基準
		data.setClassIndex(a.length - 1);
		data.add(new SparseInstance(1.0, a));

		return data;
	}

	/**
	 * ARFFデータ取得
	 * @return ARFFデータ
	 */
	public Instances getArff() {
		return this.arff;
	}

	/**
	 * ARFFデータを保存する
	 * @param filePath ファイルパス
	 * @throws IOException
	 */
	public void save(String filePath) throws IOException {
	    ArffSaver arffSaver = new ArffSaver();
	    arffSaver.setInstances(this.arff);
	    arffSaver.setFile(new File(filePath));
	    arffSaver.writeBatch();
	}

	/**
	 * new Attribute(String, String...)のショートカット
	 * @param name　属性名
	 * @param elements 要素の列挙
	 * @return
	 */
	private static Attribute newAttributes(String name, String... elements) {
		List<String> fv = new ArrayList<>();

		for (String elm : elements) {
			fv.add(elm);
		}

		Attribute attr = new Attribute(name, fv);
		return attr;
	}

}
