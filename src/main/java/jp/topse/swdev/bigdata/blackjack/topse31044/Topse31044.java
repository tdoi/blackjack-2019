package jp.topse.swdev.bigdata.blackjack.topse31044;


import jp.topse.swdev.bigdata.blackjack.topse31044.past.Arff2Model;
import jp.topse.swdev.bigdata.blackjack.topse31044.past.Arff2Model.Models;
import jp.topse.swdev.bigdata.blackjack.topse31044.past.Csv2Arff;

/**
 * ブラックジャック
  *  コール・レイズ判断ロジック
 * @author topse31044
 *
 */
public class Topse31044 {
	public static void main(String[] args) {
		try {
			// ==================================
			// CSVからARFFへ
			// ==================================
			Csv2Arff arff = new Csv2Arff();
			arff.parse("H:/git/blackjack-2019/data/2019.csv");
//			arff.save("H:/git/blackjack-2019/src/main/java/jp/topse/swdev/bigdata/blackjack/topse31044/past/2019.arff");

			// ==================================
			// ARFFからモデルへ
			// ==================================
			Arff2Model model = new Arff2Model(Models.J_48);
			model.build(arff.getArff());
			model.save("H:/git/blackjack-2019/src/main/java/jp/topse/swdev/bigdata/blackjack/topse31044/past/2019.model");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
