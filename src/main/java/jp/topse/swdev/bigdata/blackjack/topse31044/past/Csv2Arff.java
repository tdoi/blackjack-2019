package jp.topse.swdev.bigdata.blackjack.topse31044.past;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jp.topse.swdev.bigdata.blackjack.Card;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SparseInstance;
import weka.core.converters.ArffSaver;

public class Csv2Arff {

	public static void spawnArff() throws IOException {
		// =====================
		// ファイル読み込み
		// =====================
		File file = new File("H:/git/blackjack-2019/data/2019.csv");
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		
		// =====================
		// 過去の結果を構造化
		// ※4人対戦を、それぞれ分割
		// =====================
		Stream<PastGame> rawList = br.lines().map(
				elm -> PastGame.parse(elm));
		List<PastGame> pastGames  = rawList.collect(Collectors.toList());
		

		// =====================
		// 構造化したものをARFFに変換
		// =====================
		Instances data = new Instances("data1", Csv2Arff.getAttributes(), 0);
		pastGames.forEach(elm -> {						
			elm.getPlayerContexts().forEach(elm2 -> {				
				double[] values = new double[data.numAttributes()];
				values[0] = elm2.getThisFirst();
				values[1] = elm2.getThisSecond();
				values[2] = elm2.getThisThird();
				values[3] = elm2.getThisFourth();
				values[4] = elm2.getThisFifth();
				values[5] = elm2.getFirstPublic();
				values[6] = elm2.getSecondPublic();
				values[7] = elm2.getThirdPublic();
				values[8] = elm2.getFourthPublic();
				values[9] = elm2.getFifthPublic();
				values[10] = data.attribute(10).indexOfValue(elm2.getResultInName());
				data.add(new SparseInstance(1.0, values));
				
			});
		});	
		
		try {
		    ArffSaver arffSaver = new ArffSaver();
		    arffSaver.setInstances(data);
		    arffSaver.setFile(new File("H:/git/blackjack-2019/src/main/java/jp/topse/swdev/bigdata/blackjack/topse31044/past/2019.arff"));
		    arffSaver.writeBatch();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		br.close();
	}

    
	private static ArrayList<Attribute> getAttributes() {
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
		attr.add(newAttributes("results", "WIN", "DRAW", "LOSE"));
    	
        return attr;
    }
	
	/**
	 * 
	 * @param name
	 * @param elements
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
	
	public static boolean isNullOrEmpty(String str) {
		return null == str || 0 == str.length();
	}
}
