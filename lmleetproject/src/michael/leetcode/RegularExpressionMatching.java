package michael.leetcode;

public class RegularExpressionMatching {
	public static boolean isMatch(String s, String p) {
		// Start typing your Java solution below
		// DO NOT write main() function
		int i = 0, j = 0;
		if (s.equals(p))
			return true;
		if (s.length() > 0 && p.length() == 0)
			return false;
		if(p.equals(".") && s.length() == 1)
			return true;

		char[] charS = s.toCharArray();
		char[] charP = p.toCharArray();
		if (p.length() > 1) {
			if (charP[j + 1] == '*') 
			{

				if(s.length() == 0)
					return isMatch("", p.length() == 2 ? "" :p.substring(2));
				int k;
				for(k = 0; k < s.length() && (p.charAt(0) == '.' || charS[k] == charP[j]); k++)
				{
						if(isMatch(s.substring(k), p.length() == 2? "" : p.substring(2)))
							return true;				}
				return isMatch(s.substring(k), p.length() == 2 ? "" : p.substring(2));
			}
			else if (s.length() > 0
					&& (charS[i] == charP[j] || charP[j] == '.')) {
				return isMatch(s.length() > 1 ? s.substring(1): "", p.substring(1)); 
				
			}
		}
		return false;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.out.println(isMatch("a", "ab*a"));
	}

}
