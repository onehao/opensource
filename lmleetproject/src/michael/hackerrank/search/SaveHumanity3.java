package hackerrank.search;

import java.util.Scanner;

public class SaveHumanity3 {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        String[] os = new String[n];
        String pDNA;
        String vDNA;
        for(int i=0;i<n;i++){
           pDNA = in.next();
           vDNA = in.next();
           os[i] = checkDNA(pDNA, vDNA); 
           pDNA = null;
           vDNA = null;
        }
        for(int i = 0; i < n; i++)
        {
    		if(os[i].length() > 0)
    			System.out.print(os[i].trim());
        	System.out.println();
        }
	}

	private static String checkDNA(String pDNA, String vDNA ) {
		String matchString = "";
		int length = pDNA.length() - vDNA.length() + 1;
		int hashCode = vDNA.hashCode();
		for(int i = 0; i <length; i++)
		{
			int temp = vDNA.length();
			String pTemp = pDNA.substring(i, i + temp);
			int misMatch = 0;
			if ((pTemp.hashCode() != hashCode)){
				for(int j = 0; j < temp; j++)
				{
					if(pTemp.charAt(j) != vDNA.charAt(j))
						misMatch++;
					if(misMatch>1)
						break;
				}
			}
			if(misMatch < 2)
				matchString += i + " ";
		}

		return matchString;
	}
	
	private static int misMatch(String p, String v, int hashCode)
	{
		if(p.hashCode() == hashCode)
			return 0;
		int misMatch = 0;
		int temp = v.length();
		
		for(int j = 0; j < temp; j++)
		{
			if(p.charAt(j) != v.charAt(j))
				misMatch++;
			if(misMatch>1)
				break;
		}
		return misMatch;
	}
	
}