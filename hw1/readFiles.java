package hw1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class readFiles {
	public static void main(String args[]){
		int repeat = 100;
		
		for(int n = 1; n <= 1; n++){
			int[] margin = new int[repeat];
			Date startTime = new Date();
		
		for(int i = 1; i <= repeat; i++){
			int ii = i%100 + 1;
		File file = new File("C:/Users/benb/Desktop/glpk-4.55/hw1/data/time/" + n + "/" + ii + ".dat");
		System.out.println(file);
		//System.out.println(file);
		int[][] value = new int[256][256];
        try {
            InputStreamReader read = new InputStreamReader(new FileInputStream(file), "GBK");
            BufferedReader reader = new BufferedReader(read);
            String line;
            int r = 0;
            while ((line = reader.readLine()) != null) {
            	if(r >= 3 && r <= 258){
            		String s = line;
            		s.trim();
            		String[] a = s.split(" ");
            		//System.out.println(r-3 + ", " + a.length);
            		for(int j = 2; j < a.length; j++){
            			value[r-3][j-2] = Integer.parseInt(a[j]);
            		}
            	}
                r++;
            }
            reader.close();
            read.close();
            
            List<Integer> agent = new ArrayList<Integer>();
			for(int j = 0; j < 256; j++){
				agent.add(j);
			}
			int[] price = new int[256];
			HashMap<Integer, Integer> S = new HashMap<Integer, Integer>();
			while(agent.size() > 0){
				//System.out.print(agent.size() + " ");
				int a = agent.get(0);
				int max = -1;
				int max_index = -1;
				int sec = -2;
				for(int j = 0; j < 256; j++){
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
				margin[i-1] += value[ag][obj];
			}
			
        } catch (Exception e) {
            e.printStackTrace();
        }
		}
		int total = 0;
		for(int m = 0; m < repeat; m++){
			total += margin[m];
		}
		int ave = (int) (total/repeat/256);
		Date xTime = new Date();
		long xsortTime = xTime.getTime() - startTime.getTime();
		System.out.println("For m = " + n + ", margin = " + ave + ",  the execute time is "
				+ xsortTime + " milliseconds.\n");
		
		/*for(int i = 0; i < margin.length; i++){
			System.out.println(margin[i]/256);
		}*/
		}
	}
}
