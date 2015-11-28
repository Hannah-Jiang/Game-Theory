package hw2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class eliminate2 {
	public static void main(String args[]){
		File file = new File("C:/Users/benb/Dropbox/WashU/2015spring/Multi-Agent Systems/hw3/sushi3/sushi3b.5000.10.order");
        int[][] rankArray = new int[5000][10];
        int[] maxArray = new int[100];
		try{
			InputStreamReader read = new InputStreamReader(new FileInputStream(file), "GBK");
            BufferedReader reader = new BufferedReader(read);
            String line;
            int r = 0;
            while ((line = reader.readLine()) != null) {
            	String s = line;
            	String[] a = s.split(" ");
            	Set<Integer> set = new HashSet<Integer>();
            	int index = 11;
            	for(int i = 11; i >= 2; i--){
            		int key = Integer.parseInt(a[i]);
            		if(set.contains(key)){
            			continue;
            		}else{
            			rankArray[r][index-2] = key;
            			index--;
            			set.add(key);
            		}
            	}
            	for(int i = 0; i < index; i++){
            		rankArray[r][i] = -1;
            	}
            	r++;
            }
            reader.close();
            read.close();
		}catch (Exception e){
			e.printStackTrace();
		}
		
		int index = 0;
		boolean flg = true;
		while(flg){
			int[] list = getList(rankArray,0);
			int max = getMax(list);
			if(max == -1){
				break;
			}
			maxArray[index++] = max;
			remove(rankArray,max);
		}
		
		for(int i = 0; i < maxArray.length; i++){
			System.out.println(maxArray[i]);
		}
	}
	
	public static int[] getList(int[][] rankArray, int col){
		int[] result = new int[5000];
		for(int i = 0; i < 5000; i++){
			result[i] = rankArray[i][col];
		}
		return result;
	}
	
	public static int getMax(int[] list){
		int max = -1;
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		for(int i = 0; i < 5000; i++){
			if(list[i] == -1){
				continue;
			}
			if(map.containsKey(list[i])){
				int count = map.get(list[i])+1;
				map.put(list[i], count);
			}else{
				map.put(list[i], 1);
			}
		}
		
		Iterator<Integer> iter = map.keySet().iterator();
		int maxCount = 0;
		while(iter.hasNext()){
			int key = iter.next();
			int count = map.get(key);
			if(count > maxCount){
				maxCount = count;
				max = key;
			}
		}
		return max;
	}
	
	public static void remove(int[][] rankArray, int max){
		for(int i = 0; i < 5000; i++){
			int index = 0;
			for(int j = 0; j < 10; j++){
				if(rankArray[i][j] == -1){
					break;
				}
				if(rankArray[i][j] == max){
					continue;
				}else{
					rankArray[i][index++] = rankArray[i][j];
				}
			}
			for(int j = index; j < 10; j++){
				rankArray[i][j] = -1;
			}
		}
	}
}
