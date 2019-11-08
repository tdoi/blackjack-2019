package jp.topse.swdev.bigdata.blackjack.topse31044;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Csv2Arff {
	public static void main(String[] args) throws IOException {
		File file = new File("H:/git/blackjack-2019/data/2019.csv");
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String line;
		for ( int cnt = 0; (line = br.readLine()) != null; cnt++) {
			PastGame pg = PastGame.parse(line);
		    
	        System.out.println(cnt + ":" + pg);
		}
		br.close();
	}
}
