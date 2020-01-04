package jp.topse.swdev.bigdata.blackjack.topse31058;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

import jp.topse.swdev.bigdata.blackjack.Card;
import jp.topse.swdev.bigdata.blackjack.Hand;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ArffSaver;
import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeVisualizer;

public class Topse31058_ModelMaker_OpposeDealer {

    private static final String INPUT_PATH = "./data/2019.csv";
    private static final String OUTPUT_PATH = "./data/2019_OpposeDealer.arff";
    private static final String CLASSIFIER_OPPOSE_DEALER = "./model/OpposeDealer.clfi";

    public static void main(String[] arg) {
        ArrayList<String> resultValues = new ArrayList<String>();
        resultValues.add("LOSE");
        resultValues.add("DRAW");
        resultValues.add("WIN");
        ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		attributes.add(new Attribute("DealerHand"));
		attributes.add(new Attribute("PlayerSum"));
		attributes.add(new Attribute("Result", resultValues));
	
        Instances data = new Instances("blackjack", attributes, 0);
        BufferedReader reader = null;
        
    	try {
			reader = new BufferedReader(new FileReader(new File(INPUT_PATH)));
            String line;
            while ((line = reader.readLine()) != null) {
                Hand dealerHand = new Hand();
        		Hand tmpHand = null;

                String[] items = line.split(",", 0);
    			for(int i = 0, j=0; i < items.length; i++) {
                	if(i == 0) {						// dealer name
                		continue;
                	} else if(0 <= i && i <=5) {		// dealer hand
                		if(items[i].length() != 0) dealerHand.add(Card.valueOf(items[i]));
                	} else if(i % 7 == 6 ) {			// player name (new player)
                		tmpHand = new Hand();
                	} else if(i % 7 == 5 && i > 11){	// player result
            			double[] values = new double[data.numAttributes()];
                		values[0] = dealerHand.get(0).getValue();
                		values[1] = tmpHand.eval();
                		values[2] = resultValues.indexOf(items[i]);
                		data.add(new DenseInstance(1.0, values));
                	} else {							// player hand
                		if(items[i].length() != 0) tmpHand.add(Card.valueOf(items[i]));
                	}
            	}
            }
            reader.close();
		} catch (IOException e) {
			e.printStackTrace();
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
    

        try {
            tree.buildClassifier(data);
            
            Evaluation eval = new Evaluation(data);
            eval.crossValidateModel(tree, data, 5, new Random(1));
            System.out.println(eval.toSummaryString());
            System.out.println(eval.toMatrixString());

            SerializationHelper.write(CLASSIFIER_OPPOSE_DEALER, tree);
            
            JFrame frame = new JFrame("Results");
            frame.setSize(800, 500);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            TreeVisualizer visualizer = new TreeVisualizer(null, tree.graph(), new PlaceNode2());
            frame.getContentPane().add(visualizer);
            frame.setVisible(true);
            visualizer.fitToScreen();
        } catch (Exception e) {
        	e.printStackTrace();
        }

    }
}
