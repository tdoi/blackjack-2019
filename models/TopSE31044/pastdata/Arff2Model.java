package jp.topse.swdev.bigdata.blackjack.topse31044.pastdata;


import java.util.Random;
import java.util.function.Supplier;

import javax.swing.JFrame;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMO;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeVisualizer;

/**
 * ARFFを機械学習モデルファイルに変換するためのクラス。
 * @author topse31044
 *
 */
public class Arff2Model {
	/**x モデルタイプ */
	private Models modelType;
	/**x モデル */
	private AbstractClassifier model;

	/**
	 * デフォルトコンストラクター　J48
	 */
	public Arff2Model() {
		this(Models.J_48);
	}

	/**
	 * コンストラクター
	 * @param type モデルタイプ
	 */
	public Arff2Model(Models type) {
		this.modelType = type;
	}

	/**
	 * 機械学習モデルタイプ列挙
	 * @author topse31044
	 *
	 */
	public enum Models{
		/**x 詳細不明 */
		NAYVE_BAYES(()->new NaiveBayes()),
		/** J48決定木(?) */
		J_48(()->new J48(), "-U"),
		/**x 詳細不明 */
		RANDOM_FOREST(()->new RandomForest()),
		/**x 詳細不明 */
		LOGISTIC(()->new Logistic()),
		/**x 詳細不明 */
		MULTILAYER_PERCEPTRON(()->new MultilayerPerceptron(), "-L", "0.5", "-M", "0.1"),
		/** SMOreg */
		S_M_O(()->new SMO());

		/**x モデル生成するためのラムダ式 */
		private Supplier<AbstractClassifier> spawner;
		/**x モデル生成のパラメーター */
		private String[] params;

		/**
		 * コンストラクター
		 * @param spawner　モデル生成するラムダ式
		 * @param params モデル生成のパラメーター
		 */
		private Models(Supplier<AbstractClassifier> spawner, String... params) {
			this.spawner = spawner;
			this.params = params;
		}

		/**
		 * モデル生成する
		 * @return 機械学習モデル
		 * @throws Exception
		 */
		public AbstractClassifier getModel() throws Exception {
			AbstractClassifier model = this.spawner.get();
			if (null != this.params) {
				model.setOptions(this.params);
			}
			return model;
		}
	}

	/**
	 *
	 * @param arff
	 * @throws Exception
	 */
	public void build(Instances arff) throws Exception {
		build(arff, false);
	}

	/**
	 * ARFFを機械学習モデルに適用する
	 * @param arff
	 * @param test 精度をテストする
	 * @throws Exception
	 */
	public void build(Instances arff, boolean test) throws Exception {
		AbstractClassifier model = this.modelType.getModel();

		model.buildClassifier(arff);

		if (test) {
	        Evaluation eval = new Evaluation(arff);
	        eval.crossValidateModel(model,  arff, 2, new Random(1));
			System.out.println(eval.toSummaryString());
		}

        this.model = model;
	}

	/**
	 * モデルファイルを保存する
	 * @param filepath ファイルパス
	 */
	public void save(String filepath) {
        try {
            SerializationHelper.write(filepath, this.model);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

    public void toVisual() {
    	if (!(this.model instanceof J48)) {
    		throw new RuntimeException("J48限定の機能です!");
    	}

    	J48 j48 = (J48)this.model;

        try {
            TreeVisualizer visualizer = new TreeVisualizer(null, j48.graph(), new PlaceNode2());

            JFrame frame = new JFrame("Results");
            frame.setSize(2400, 2000);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            frame.getContentPane().add(visualizer);
            frame.setVisible(true);
            visualizer.fitToScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
