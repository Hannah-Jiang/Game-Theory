package hw2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Arrays;

public class calculateScore {
	public static void main(String args[]){
		File file = new File("C:/Users/benb/Dropbox/WashU/2015spring/Multi-Agent Systems/hw3/sushi3/sushi3b.5000.10.order");
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		try{
			InputStreamReader read = new InputStreamReader(new FileInputStream(file), "GBK");
            BufferedReader reader = new BufferedReader(read);
            String line;
            while ((line = reader.readLine()) != null) {
            	String s = line;
            	String[] a = s.split(" ");
            	for(int i = 2; i < a.length; i++){
            		int key = Integer.parseInt(a[i]);
            		int score = 11-i;
            		if(map.containsKey(key)){
            			score += map.get(key);
            			map.put(key, score);
            		}else{
            			map.put(key, score);
            		}
            	}
            }
            reader.close();
            read.close();
		}catch (Exception e){
			e.printStackTrace();
		}
		HashMap<Integer, Integer> map2= new HashMap<Integer, Integer>();
		int[] finalScore = new int[100];
		for(int i = 0; i < 100; i++){
			finalScore[i] = map.get(i);
			map2.put(finalScore[i], i);
		}
		
		Arrays.sort(finalScore);
		
		for(int i = 99; i >= 0; i--){
			System.out.println(map2.get(finalScore[i]) + " " + finalScore[i]);
		}
	}
}
