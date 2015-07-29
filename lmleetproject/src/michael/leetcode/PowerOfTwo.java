package michael.leetcode;

/**
 * Given an integer, write a function to determine if it is a power of two.
 * 
 * @author Michael Wan.
 *
 */
public class PowerOfTwo {
	public boolean isPowerOfTwo(int n) {
		int seed = 1;
		for (int i = 1; i < 32; i++) {
			if ((seed ^ n) == 0) {
				return true;
			}
			seed = seed << 1;
		}
		return false;
	}

//	public boolean isPowerOfTwo2(int n) {
//		int seed = 1;
//		for (int i = 1; i < 32; i++) {
//			if ((2147483647 ^ (seed ^ n)) == seed) {
//				return true;
//			}
//			seed = seed << 1;
//		}
//		return false;
//	}

	public static void main(String[] args) {

		System.out.println(3 ^ 3);

		System.out.println(Integer.MAX_VALUE);

		PowerOfTwo p = new PowerOfTwo();
		System.out.println(p.isPowerOfTwo(4));
		System.out.println(p.isPowerOfTwo(0));
		System.out.println(p.isPowerOfTwo(7));
		System.out.println(p.isPowerOfTwo(16));
		System.out.println(p.isPowerOfTwo(111));
		System.out.println(p.isPowerOfTwo(128));
	}
}
