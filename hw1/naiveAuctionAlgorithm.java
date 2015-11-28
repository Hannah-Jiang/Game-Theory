package hw1;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class naiveAuctionAlgorithm {
	public static void main(String args[]){
		//loop from 1 to 256
		for(int i = 8; i <= 8; i++){
			Date startTime = new Date();
			int range = (int)Math.pow(10, 7);
			int repeat = 100;
			int[] margin = new int[repeat];
			for(int k = 0; k < repeat; k++){
				//System.out.println(k);
				int n = (int) Math.pow(2,i);
				int[][] value = new int[n][n];
				for(int r = 0; r < n; r++){
					for(int c = 0; c < n; c++){
						value[r][c] = (int) (Math.random()*range);
					}
				}
				List<Integer> agent = new ArrayList<Integer>();
				for(int j = 0; j < n; j++){
					agent.add(j);
				}
				int[] price = new int[n];
				HashMap<Integer, Integer> S = new HashMap<Integer, Integer>();
				while(agent.size() > 0){
					//System.out.print(agent.size() + " ");
					int a = agent.get(0);
					int max = -1;
					int max_index = -1;
					int sec = -2;
					for(int j = 0; j < n; j++){
						int vv = value[a][j] - price[j];
						if(vv > max){
							sec = max;
							max = vv;
							max_index = j;
						}else if(vv <= max && vv > sec){
							sec = vv;
						}
					}
					if(S.get(max_index) == null){
						S.put(max_index, a);
						agent.remove(0);
					}else{
						agent.add(S.get(max_index));
						S.put(max_index, a);
						agent.remove(0);
					}
					price[max_index] += max - sec == 0 ? 1 : max - sec;
				}
			
				Iterator<Integer> keys = S.keySet().iterator();
				while(keys.hasNext()){
					int obj = keys.next();
					int ag = S.get(obj);
					//System.out.println(ag + "->" + obj);
					margin[k] += value[ag][obj];
				}
			}// end of repeat
			
			//compute average margin
			long total = 0;
			for(int m = 0; m < repeat; m++){
				total += margin[m];
			}
			int ave = (int) (total/repeat/Math.pow(2, i));
			System.out.println("i = " + i + ", total margin  = " + ave);
			Date xTime = new Date();
			long xsortTime = xTime.getTime() - startTime.getTime();
			System.out.println("For m = " + range + ", the execute time is "
					+ xsortTime + " milliseconds.\n");
		}
	}
}
