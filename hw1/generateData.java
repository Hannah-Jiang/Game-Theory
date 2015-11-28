package hw1;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class generateData {
	public static void main(String args[]) throws IOException{
		
		for(int i = 8; i <= 8; i++){
			int range = (int)Math.pow(10,7);
			String root = "C:/Users/benb/Desktop/glpk-4.55/hw1/data/time/" + 7 + "/";
			int n = (int)Math.pow(2, i);
			for(int k = 1; k <= 100; k++){
				String fileName = k + ".dat";
			try {
				FileOutputStream out = new FileOutputStream(root+fileName);
				System.out.println(root+fileName);
					byte[] buff=new byte[]{};
					String data = "data; \n";
					buff=data.getBytes();
					out.write(buff,0,buff.length);
					
					String para = "param n := " + n + "; \n";
					buff=para.getBytes();
					out.write(buff,0,buff.length);
					
					String head = "param c: ";
					for(int j = 1; j <= n; j++){
						head += " " + j;
					}
					head += " := \n";
					buff=head.getBytes();
					out.write(buff,0,buff.length);
					
					for(int r = 1; r <= n; r++){
						String s = r + " ";
						for(int c = 0; c < n; c++){
							s += " " + (int) (Math.random()*range);
						}
						s += "\n";
						buff=s.getBytes();
						out.write(buff,0,buff.length);
					}
					
					String s = "; \n end;";
					buff=s.getBytes();
					out.write(buff,0,buff.length);
				
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			}
			
		}
	}
}
