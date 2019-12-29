package jp.topse.swdev.bigdata.blackjack.topse31044.past;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jp.topse.swdev.bigdata.blackjack.Card;
import jp.topse.swdev.bigdata.blackjack.Game;
import jp.topse.swdev.bigdata.blackjack.Hand;
import jp.topse.swdev.bigdata.blackjack.Player;
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
	
	/** ARFFデータ */
	private Instances arff;

	public void parse(String filePath) {
		//x=====================
		//x  ファイル読み込み
		//x=====================
		File file = new File(filePath);
		try(FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);){
			
			//x=====================
			//x 過去の結果を構造化
			//x ※4人対戦を、それぞれ分割
			//x=====================
			Stream<PastGame> rawList = br.lines().map(
					elm -> PastGame.parse(elm));
			List<PastGame> pastGames  = rawList.collect(Collectors.toList());
			
	
			//x=====================
			//x 構造化したものをARFFに変換
			//x=====================
			Instances data = null;
			pastGames.forEach(elm ->
				elm.getPlayerContexts().forEach(elm2 -> Csv2Arff.addToInstancesOrSpawn(data, elm2))
			);	
			this.arff = data;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 
	 * @param arg
	 * @param pc
	 * @return
	 */
	public static Instances addToInstancesOrSpawn(Instances arg, PlayerContext pc) {
		Instances data = arg;
		if (null == data) {
			ArrayList<Attribute> attr = new ArrayList<>();
			attr.add(new Attribute("my_first"));
			attr.add(new Attribute("my_second"));
			attr.add(new Attribute("my_third"));
			attr.add(new Attribute("my_forth"));
			attr.add(new Attribute("my_fifth"));
			attr.add(new Attribute("dealer_open"));
			attr.add(new Attribute("first_open"));
			attr.add(new Attribute("second_open"));
			attr.add(new Attribute("third_open"));
			attr.add(new Attribute("fourth_open"));
			attr.add(newAttributes("results", Type.LOSE.name(), Type.DRAW.name(), Type.WIN.name()));
			data = new Instances("data1", attr, 0); 
		}	
		
		double[] values = new double[data.numAttributes()];
		values[0] = pc.getThisFirst();
		values[1] = pc.getThisSecond();
		values[2] = pc.getThisThird();
		values[3] = pc.getThisFourth();
		values[4] = pc.getThisFifth();
		values[5] = pc.getFirstPublic();
		values[6] = pc.getSecondPublic();
		values[7] = pc.getThirdPublic();
		values[8] = pc.getFourthPublic();
		values[9] = pc.getFifthPublic();
		values[10] = data.attribute(10).indexOfValue(pc.getResultInName());
		data.setClassIndex(10); //x 評価基準
		data.add(new SparseInstance(1.0, values));	
		
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
