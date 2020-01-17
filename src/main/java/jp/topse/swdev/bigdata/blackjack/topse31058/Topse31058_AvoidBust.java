package jp.topse.swdev.bigdata.blackjack.topse31058;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import jp.topse.swdev.bigdata.blackjack.Action;
import jp.topse.swdev.bigdata.blackjack.Card;
import jp.topse.swdev.bigdata.blackjack.DecisionMaker;
import jp.topse.swdev.bigdata.blackjack.Game;
import jp.topse.swdev.bigdata.blackjack.Hand;
import jp.topse.swdev.bigdata.blackjack.Player;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.SerializationHelper;



public class Topse31058_AvoidBust implements DecisionMaker {
	
    private static final String CLASSIFIER_VOID_BUST = "./models/TopSE31058/avoidBust.clfi";

    private Classifier m_classifier = null;
	
	public Topse31058_AvoidBust() {
		super();
		try {
			m_classifier = (Classifier) SerializationHelper.read(CLASSIFIER_VOID_BUST);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	@Override
	public Action decide(Player player, Game game) {
    	Map<Player, Hand> playerHands = game.getPlayerHands();	    	
    	Hand hand = playerHands.get(player);
    	
        ArrayList<Attribute> attributes = new ArrayList<Attribute>();
        ArrayList<String> resultValues = new ArrayList<String>();
        resultValues.add("SAFE");
        resultValues.add("BUST");
		attributes.add(new Attribute("HandSumValue"));
		attributes.add(new Attribute("AceNum"));
		attributes.add(new Attribute("Bust", resultValues));
	
        Instances data = new Instances("blackjack", attributes, 0);

        int aceNum=0;
        int handCount = hand.getCount();
        for (int i=0;i<handCount;i++) {
        	if(hand.get(i).getIndex()== Card.ACE.getIndex()) aceNum++;
        }
        
        double[] testValues = new double[data.numAttributes()];
        testValues[0]= hand.eval();
        testValues[1] = aceNum;
        testValues[2] = 0;
        data.add(new DenseInstance(1.0,testValues));
        data.setClassIndex(2);
        Evaluation evalBust = null;
        double result = 0;
		try {
			evalBust = new Evaluation(data);
	        result = evalBust.evaluateModelOnce(m_classifier, data.firstInstance());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	if(result == 1) {
    		return Action.STAND;
    	}

    	return Action.HIT;
	}
	

}
