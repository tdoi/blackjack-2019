package jp.topse.swdev.bigdata.blackjack.topse31058;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeVisualizer;
import weka.classifiers.trees.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;


public class Topse31058_ModelMaker {

    static final String INPUT_PATH = "./data/2019.csv";
    static final String OUTPUT_PATH = "./data/2019.arff";
	
	public static void main(String[] args) {
        ArrayList<Attribute> attributes = new ArrayList<Attribute>();
        ArrayList<String> cardValues = new ArrayList<String>();
        cardValues.add("STAND");
        cardValues.add("ACE");
        cardValues.add("TWO");
        cardValues.add("THREE");
        cardValues.add("FOUR");
        cardValues.add("FIVE");
        cardValues.add("SIX");
        cardValues.add("SEVEN");
        cardValues.add("EIGHT");
        cardValues.add("NINE");
        cardValues.add("TEN");
        cardValues.add("JACK");
        cardValues.add("QUEEN");
        cardValues.add("KING");
        ArrayList<String> resultValues = new ArrayList<String>();
        resultValues.add("LOSE");
        resultValues.add("WIN");
        Instances data = null;
		
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(INPUT_PATH)));
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] items = line.split(",", 0);
                if(null == data) {
                	for(int i = 0; i < items.length; i++) {
                    	if(i == 0) {
//                    		attributes.add(new Attribute("Dealer",true));
                    	} else if(i<=5) {
                    		attributes.add(new Attribute("Dealer-"+i,cardValues));
                    	} else {
                    		String playerName = "Player-" + (i+1)/7;
                    		int playerAttrIdx = (i+1)%7;
                    		
                    		if(playerAttrIdx==6) {
                    			attributes.add(new Attribute(playerName + "-Result",resultValues));
                    		} else if(playerAttrIdx==0) {
//                    			attributes.add(new Attribute(playerName,true));
                    		} else {
                    			attributes.add(new Attribute(playerName + "-" + playerAttrIdx,cardValues));
                    		}
                    	}
                	}
                    data = new Instances("blackjack", attributes, 0);
                }
                double[] values = new double[data.numAttributes()];
                for(int i = 0, j=0; i < items.length; i++) {
                	if(i == 0 || i % 7 == 6) {
//                		values[i] = data.attribute(i).addStringValue(items[i]);
                	} else {
                        double tmp_value = data.attribute(j).indexOfValue(items[i]);
                		if(tmp_value>=0) {
                			values[j] = tmp_value;
                		} else {
                			values[j] = 0;
                		}
                		j++;
                	}
                }
                data.add(new DenseInstance(1.0,values));
            }
            reader.close();

        } catch (IOException e) {
            System.out.println(e);
        }

        try {
            ArffSaver arffSaver = new ArffSaver();
            arffSaver.setInstances(data);
            arffSaver.setFile(new File(OUTPUT_PATH));
            arffSaver.writeBatch();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        J48 tree = new J48();
        try {
        	data.setClassIndex(10);
//        	data
        	tree.buildClassifier(data);
        	
            TreeVisualizer visualizer = new TreeVisualizer(null, tree.graph(), new PlaceNode2());
            
            JFrame frame = new JFrame("Results");
            frame.setSize(1600, 1000);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            frame.getContentPane().add(visualizer);
            frame.setVisible(true);
            visualizer.fitToScreen();        	
        } catch (Exception e) {
        	e.printStackTrace();
        }
        
        
        
	}
}
