package michael.hackerrank.search;

import java.util.ArrayList;
import java.util.Scanner;

public class SaveHumanity2 {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int[][] os = new int[n][];
        
        for(int i=0;i<n;i++){
           String pDNA = in.next();
           String vDNA = in.next();
           os[i] = checkDNA(pDNA, vDNA); 
        }
        for(int i = 0; i < n; i++)
        {
        	for(int j = 0; null != os[i] && j < os[i].length; j++)
        	{
        		if(os[i][j] >= 0)
        			System.out.print(os[i][j] + " ");
        	}
        	System.out.println();
        }
	}

	private static int[] checkDNA(String pDNA, String vDNA ) {
		if(pDNA.length() < vDNA.length())
			return null;
		
		int[] array = new int[pDNA.length() - vDNA.length() + 1];
		
		for(int i = 0; i < array.length; i++)
		{
			array[i] = -1;
			int match = 0;
			//boolean b = false;
			int temp = vDNA.length();
			for(int j = 0; j < temp; j++)
			{
//				if(pDNA.charAt(j + i) == vDNA.charAt(j))
//					match++;
				if(pDNA.charAt(j + i) != vDNA.charAt(j))
					match++;
				if(match>1)
					break;
					
			}
			if(match < 2)
				array[i] = i;
		}
		return array;
	}
	
}