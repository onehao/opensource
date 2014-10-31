package linda.leetcode;

public class Reverse_Words {
	public String reverseWords(String s) {
		if(s == null || s.length() == 0)
		{
			return "";
		}
		String[] old = s.split(" "); // split string and store in string[]
		StringBuilder sb = new StringBuilder();
		for (int i = old.length - 1; i >= 0; i--) {
			if(old[i] == ""){
				sb.append(old[i]).append(" ");
			}		
		}
//		System.out.println(sb.substring(0,sb.length()-1));
		return sb.length() == 0 ? "" : sb.substring(0, sb.length()-1);
	}

	public static void main(String[] args) {
		Reverse_Words rw = new Reverse_Words();
		rw.reverseWords("");
		rw.reverseWords("this sky is blue");
	}
}
