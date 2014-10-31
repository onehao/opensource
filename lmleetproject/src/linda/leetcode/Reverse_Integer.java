package linda.leetcode;

public class Reverse_Integer {
	public int reverse(int x) {
		int result = 0;
		while (x != 0) {
			result = result*10 + (x % 10);
			x /= 10;
			;
		}
		System.out.print(result);
		return result;
	}

	public static void main(String[] args) {
		Reverse_Integer ri = new Reverse_Integer();
		ri.reverse(123);
	}

}
