package michael.hackerrank.search;

import java.util.ArrayList;
import java.util.Scanner;

public class SaveHumanity {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int[] os = new int[n];
        ArrayList<CasePair> list = new ArrayList<CasePair>();
        for(int i=0;i<n;i++){
           String pDNA = in.next();
           String vDNA = in.next();
           //in.next();
           list.add(new CasePair(pDNA, vDNA));
           
        }
        for(CasePair entry : list)
        {
        	checkDNA(entry.pDNA, entry.vDNA);
        }
        
	}

	private static void checkDNA(String pDNA, String vDNA) {
		// TODO Auto-generated method stub
		if(pDNA.length() < vDNA.length())
		{
			System.out.println();
			return;
		}
		
		for(int i = 0; i <= pDNA.length() - vDNA.length(); i++)
		{
			int match = 0;
			for(int j = 0; j < vDNA.length(); j++)
			{
				if(pDNA.charAt(j + i) == vDNA.charAt(j))
					match++;
			}
			if(match > vDNA.length() - 2)
				System.out.print(i + " ");
		}
		System.out.println();
	}
	
	
}

class CasePair
{
	String pDNA;
	String vDNA;
	
	CasePair(String pDNA, String vDNA)
	{
		this.pDNA = pDNA;
		this.vDNA = vDNA;
	}
}
