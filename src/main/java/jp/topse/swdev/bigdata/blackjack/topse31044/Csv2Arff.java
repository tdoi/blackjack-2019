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

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

public class Csv2Arff {
	public static void main(String[] args) throws IOException {
		File file = new File("H:/git/blackjack-2019/data/2019.csv");
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		
		// =====================
		// 過去の結果を構造化
		// =====================
		List<PastGame> pastGames  = br.lines().map(
				elm -> PastGame.parse(elm)).collect(Collectors.toList());
		
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
				
				
			});
			cnt[0]++;
		});	
	
		
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
    
	private FastVector getAttributes() {
        FastVector attributes = new FastVector();
        attributes.addElement(new Attribute("my_first"));
        attributes.addElement(new Attribute("my_second"));
        attributes.addElement(new Attribute("my_third"));
        attributes.addElement(new Attribute("my_forth"));
        attributes.addElement(new Attribute("my_fifth"));
        attributes.addElement(new Attribute("dealer_open"));
        attributes.addElement(new Attribute("first_open"));
        attributes.addElement(new Attribute("second_open"));
        attributes.addElement(new Attribute("third_open"));
        attributes.addElement(new Attribute("fourth_open"));
        attributes.addElement(newVectorAttr("class", "won", "draw", "lost"));
    	
        return attributes;
    }
	
	/**
	 * 
	 * @param name
	 * @param elements
	 * @return
	 */
	private static Attribute newVectorAttr(String name, String... elements) {
		FastVector fv = new FastVector();

		for (String elm : elements) {
			fv.addElement(elm);
		}

		Attribute attr = new Attribute(name, fv);
		return attr;
	}
}
