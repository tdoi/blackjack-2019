package jp.topse.swdev.bigdata.blackjack.topse31058;

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

public class Topse31058_OpposeDealer implements DecisionMaker {

    private static final String CLASSIFIER_OPPOSE_DEALER = "./models/TopSE31058/OpposeDealer.clfi";

    private Classifier m_classifier = null;

    public Topse31058_OpposeDealer() {
		super();
		try {
			m_classifier = (Classifier) SerializationHelper.read(CLASSIFIER_OPPOSE_DEALER);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    @Override
	public Action decide(Player player, Game game) {
		Card dealerCard = game.getUpCard();

    	Map<Player, Hand> playerHands = game.getPlayerHands();	    	
    	Hand hand = playerHands.get(player);

        ArrayList<String> resultValues = new ArrayList<String>();
        resultValues.add("LOSE");
        resultValues.add("DRAW");
        resultValues.add("WIN");
        ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		attributes.add(new Attribute("DealerHand"));
		attributes.add(new Attribute("PlayerSum"));
		attributes.add(new Attribute("Result", resultValues));
	
        Instances data = new Instances("blackjack", attributes, 0);

        double[] testValues = new double[data.numAttributes()];
        testValues[0] = game.getUpCard().getValue();
        testValues[1] = playerHands.get(player).eval();
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
    	
    	if(result > 0) return Action.STAND;
    		

    	
    	return Action.HIT;
	}

}
