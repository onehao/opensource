package opensources;

import java.util.Stack;

public class Test {
	public static void main(String[] args) {
		double result = 1.0d;
		result = 1.0 / 25;
		System.out.println(result);
		
		
		double result2 = 1.0d;
		double seed = 0.00001d;
		System.out.println(Math.pow(seed, 100000000.0d));
		result2 /= Math.pow(seed, 100000000.0d);
		System.out.println(result2 + "");
		
		System.out.println(Double.POSITIVE_INFINITY);
		
		
		Stack<String> st = new Stack<String>();
		st.push("aa");
		st.pop();
		
	}

}
