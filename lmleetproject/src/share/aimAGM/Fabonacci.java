package share.aimAGM;

/**
 * 
 * @author wanhao01
 *
 */
public class Fabonacci {

	private long[] dic = new long[100];

	public long fn(int n) {

		if (n < 0)
			return -1;

		if (n <= 1)
			return n;
		long result = 0;
		long stepOne = 1;
		long stepTwo = 0;
		for (int i = 2; i <= n; i++) {
			result = stepOne + stepTwo;
			stepTwo = stepOne;
			stepOne = result;

		}

		return result;
	}

	public long fn2(int n) {

		if (n < 0)
			return -1;

		if (n <= 1)
			return n;
		if (dic[n] > 0) {
			return dic[n];
		}
		long result = 0;
		long stepOne = 1;
		long stepTwo = 0;
		for (int i = 2; i <= n; i++) {
			result = stepOne + stepTwo;
			stepTwo = stepOne;
			stepOne = result;
		}
		dic[n] = result;
		return result;
	}

	public static void main(String[] args) {
		Fabonacci f = new Fabonacci();
		long start = System.currentTimeMillis();

		// when iteration get larger, fn performance better even if fn2 use
		// cache.
		int iteration = 100000000;

		for (int i = 0; i < iteration; i++) {
			f.fn(90);
		}
		long end = System.currentTimeMillis();

		System.out.println(end - start + " ms ");

		start = end;
		for (int i = 0; i < iteration; i++) {
			f.fn2(90);
		}

		System.out.println((System.currentTimeMillis() - start) + " ms ");
	}
}

class MyIntList {

}
