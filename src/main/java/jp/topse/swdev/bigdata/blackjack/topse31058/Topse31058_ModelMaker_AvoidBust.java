package jp.topse.swdev.bigdata.blackjack.topse31058;

import weka.core.*;
import weka.core.converters.ArffSaver;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.*;
import weka.gui.treevisualizer.*;

import java.io.*;
import java.util.*;

import javax.swing.JFrame;

import jp.topse.swdev.bigdata.blackjack.Card;
import jp.topse.swdev.bigdata.blackjack.Hand;


public class Topse31058_ModelMaker_AvoidBust {

    private static final String INPUT_PATH = "./data/2019.csv";
    private static final String OUTPUT_PATH = "./data/2019.arff";
    private static final String CLASSIFIER_VOID_BUST = "./models/TopSE31058/avoidBust.clfi";
	
	public static void main(String[] args) {
        ArrayList<String> actionValues = new ArrayList<String>();
        actionValues.add("STAND");
        actionValues.add("HIT");
        ArrayList<String> resultValues = new ArrayList<String>();
        resultValues.add("SAFE");
        resultValues.add("BUST");
        Instances data = null;
        
        ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		attributes.add(new Attribute("HandSumValue"));
		attributes.add(new Attribute("AceNum"));
		attributes.add(new Attribute("Bust", resultValues));
	
        data = new Instances("blackjack", attributes, 0);
        
        try {
        	BufferedReader reader = new BufferedReader(new FileReader(new File(INPUT_PATH)));
//            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] items = line.split(",", 0);
        		Hand tmpHand = new Hand();
        		int tmpAceNum = 0;
                for(int i = 0, j=0; i < items.length; i++) {
                	if(i == 0 || i % 7 == 6 || (i % 7 == 5 && i > 11)) {
                		if(tmpHand.getCount() > 0) {
                			tmpHand = new Hand();
                			tmpAceNum = 0;
                		}
                	} else {
            			int cardIndex = Card.valueOf(items[i]).getIndex();
            			if(tmpHand.getCount() < 2 || tmpHand.eval() <= 11) {
        					tmpHand.add(Card.indexOf(cardIndex));            				
                		} else {
                			if(cardIndex <= 0) {
	        					continue;
	        	            }
                			double[] values = new double[data.numAttributes()];
        					// "HandSumValue"
        					values[0] = tmpHand.eval();
                			if(cardIndex == 1) tmpAceNum++;
        					// "AceNum"
        					values[1] = tmpAceNum;
        					// "Bust"
        					tmpHand.add(Card.indexOf(cardIndex));
        					values[2] = tmpHand.eval() > 21 ? 1 : 0;
        					
        					// Thinning out cases of successful HIT because there are too many.
        					if(tmpHand.eval() <= 21) {
        						if(j>=2) {
        							j=0;
        						}else {
        							j++;
        							continue;
        						}
        					}
	        	            
        	                data.add(new DenseInstance(1.0,values));
                		}
                	}
                }
/*
                if(null == data) {
	            	for(int i = 0; i < items.length; i++) {
	                	if(i == 0) {
//	                		attributes.add(new Attribute("Dealer",true));
	                	} else if(i<=5) {
	                		attributes.add(new Attribute("Dealer-"+i,cardValues));
	                	} else {
	                		String playerName = "Player-" + (i+1)/7;
	                		int playerAttrIdx = (i+1)%7;
	                		
	                		if(playerAttrIdx==6) {
	                			attributes.add(new Attribute(playerName + "-Result",resultValues));
	                		} else if(playerAttrIdx==0) {
//	                			attributes.add(new Attribute(playerName,true));
	                		} else {
	                			attributes.add(new Attribute(playerName + "-" + playerAttrIdx,cardValues));
	                		}
	                	}
	            	}
	                data = new Instances("blackjack", attributes, 0);
	            }
*/
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
    	data.setClassIndex(2);
    	tree.setConfidenceFactor(0.1f);
    
        double[] testValues = new double[data.numAttributes()];
        testValues[0]= 14;
        testValues[1] = 1;
        testValues[2] = 0;
        Instances testInstances = new Instances("test", attributes, 0);
        testInstances.add(new DenseInstance(1.0,testValues));
        testInstances.setClassIndex(2);

        try {
            tree.buildClassifier(data);
            
            Evaluation eval = new Evaluation(data);
            eval.crossValidateModel(tree, data, 5, new Random(1));
            System.out.println(eval.toSummaryString());
            System.out.println(eval.toMatrixString());

            SerializationHelper.write(CLASSIFIER_VOID_BUST, tree);
            
            JFrame frame = new JFrame("Results");
            frame.setSize(800, 500);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            TreeVisualizer visualizer = new TreeVisualizer(null, tree.graph(), new PlaceNode2());
            frame.getContentPane().add(visualizer);
            frame.setVisible(true);            
            visualizer.fitToScreen();

            double result = eval.evaluateModelOnce(tree, testInstances.firstInstance());
            System.out.print(result);
        } catch (Exception e) {
        	e.printStackTrace();
        }
        
	}
}
