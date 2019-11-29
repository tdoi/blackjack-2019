package jp.topse.swdev.bigdata.blackjack.topse31044;

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
	final int[] ALICE_CONTEXT = {1, 7, 14, 21, 28, 7, 8, 9, 10, 11};
	final int ALICE_RESULT = 12;
	final int[] BOB_CONTEXT = {1, 7, 14, 21, 28, 14, 15, 16, 17, 18};
	final int BOB_RESULT = 29;
	final int[] CHARLIE_CONTEXT = {1, 7, 14, 21, 28, 21, 22, 23, 24, 25};
	final int CHARLIE_RESULT = 26;
	final int[] DAVE_CONTEXT = {1, 7, 14, 21, 28, 27, 28, 29, 30, 31};
	final int DAVE_RESULT = 32;
	
	public static void main(String[] args) throws IOException {
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
		
		
		Instances data = new Instances("data1", Csv2Arff.getAttributes(), 0);
		final int[] cnt = new int[1];
		pastGames.forEach(elm -> {
			elm.getStormTeam().forEach(elm2 -> {
//				System.out.print(cnt[0] + ",");
//				System.out.print(elm2.getName() +",");
//				System.out.print("**RESULT** =>,");
//				System.out.print(elm2.getWinnerView() +",");
//				System.out.print("**HAND** =>,");
//				System.out.print( String.join(",",elm2.getTefuda()) + ",");
//				System.out.print("**KOKAI_JOHO** =>,");
//				System.out.println(String.join(",",elm.getKokaiJoho()));
				List<Integer> dbl = new ArrayList<>();
				dbl.addAll(elm2.getTefudaAsIndex());
				dbl.addAll(elm.getKokaiJohoAsIndex());
				dbl.add(data.attribute(dbl.size()).indexOfValue(elm2.getWinnerView()));
				
				
				double[] values = new double[data.numAttributes()];

				for(int lp = 0; lp < values.length; lp++) {
					values[lp] = dbl.get(lp);
				}
				
				data.add(new SparseInstance(1.0, values));
				
			});
			cnt[0]++;
		});	
		
		try {
		    ArffSaver arffSaver = new ArffSaver();
		    arffSaver.setInstances(data);
		    arffSaver.setFile(new File("H:/git/blackjack-2019/src/main/java/jp/topse/swdev/bigdata/blackjack/topse31044/2019.arff"));
		    arffSaver.writeBatch();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		br.close();
	}
	
//	
//    protected void create() {
//
//        Instances data = new Instances("data1", this.getAttributes(), 0);
//
//        try {
//            BufferedReader reader = new BufferedReader(new FileReader(new File(this.getInputPath())));
//            reader.readLine();
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] items = line.split(",", 0);
//                double[] values = new double[data.numAttributes()];
////                for (int i = 1; i <= 10; ++i) {
////                    values[i - 1] = Double.parseDouble(items[i]);
////                }
////                values[10] = (int) Double.parseDouble(items[11]);
//              for (int i = 0; i < values.length; ++i) {
//	              values[i] = Double.parseDouble(items[i + (this.ignoresFirst() ? 1 : 0)]);
//	          }
//                data.add(new Instance(1.0, values));
//            }
//            reader.close();
//
//        } catch (IOException e) {
//            System.out.println(e);
//        }
//
//        try {
//            ArffSaver arffSaver = new ArffSaver();
//            arffSaver.setInstances(data);
//            arffSaver.setFile(new File(this.getOutputPath()));
//            arffSaver.writeBatch();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    
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
		attr.add(newAttributes("results", "won", "draw", "lost"));
    	
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
