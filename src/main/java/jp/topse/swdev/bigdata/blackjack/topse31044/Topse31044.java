package jp.topse.swdev.bigdata.blackjack.topse31044;

import java.io.FileNotFoundException;
import java.io.IOException;

import jp.topse.swdev.bigdata.blackjack.topse31044.past.Csv2Arff;

public class Topse31044 {
	public static void main(String[] args) {
		try {
			Csv2Arff.spawnArff();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
