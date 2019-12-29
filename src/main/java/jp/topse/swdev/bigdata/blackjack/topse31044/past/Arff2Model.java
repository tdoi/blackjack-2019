package jp.topse.swdev.bigdata.blackjack.topse31044.past;


import java.util.function.Supplier;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMO;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.SerializationHelper;

/**
 * ARFFを機械学習モデルファイルに変換するためのクラス。
 * @author topse31044
 *
 */
public class Arff2Model {
	/** モデルタイプ */
	private Models modelType;
	/** モデル */
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
	 * 機械学習モデルタイプ列挙。モデル生成するためのラムダ式を保持。
	 * @author topse31044
	 *
	 */
	public enum Models{
		/** 詳細不明 */
		NAYVE_BAYES(()->new NaiveBayes()),
		/** J48決定木(?) */
		J_48(()->new J48(), "-U"),
		/** 詳細不明 */
		RANDOM_FOREST(()->new RandomForest()),
		/** 詳細不明 */
		LOGISTIC(()->new Logistic()),
		/** 詳細不明 */
		MULTILAYER_PERCEPTRON(()->new MultilayerPerceptron(), "-L", "0.5", "-M", "0.1"),
		/** SMOreg */
		S_M_O(()->new SMO());

		/** モデル生成するためのラムダ式 */
		private Supplier<AbstractClassifier> spawner;
		/** モデル生成のパラメーター */
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
	 * ARFFを機械学習モデルに適用する
	 * @throws Exception
	 */
	public void build(Instances arff) throws Exception {
		AbstractClassifier model = this.modelType.getModel();

		model.buildClassifier(arff);

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
}
