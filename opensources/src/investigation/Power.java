package investigation;

public class Power {

	/**
	 * if power is odd then result is (base ^ (power / 2)) ^ 2 * base if power
	 * is even then result is (base ^ (power / 2)) ^ 2
	 * 
	 * @param base
	 * @param power
	 * @return
	 * @throws Exception
	 */
	public static double getPower(double base, int power) throws Exception {
		double result = 0d;
		if (power == 0) {
			return 1d;
		}
		if (power == 1) {
			return base;
		}

		if (power > 1) {
			result = getPower(base, power >> 1);
			result *= result;

			// if power is odd. when the remainder is 1 then the base is odd.
			if ((power & 0x1) == 1) {
				result *= base;
			}
		} else {
			if (equals(base, 0.0)) {
				// by convention we have 3 ways to deal with exception.
				// 1. return specific value.
				// 2. using global result flag.
				// 3. using fail fast way to throw an exception direction.
				// we use #3 in our code.
				throw new Exception(
						"the base shouldn't be 0 when the power is negative.");
			} else {
				return 1 / getPower(base, -power);
				// return 1/getPower(base, Math.abs(power));
			}

		}

		return result;
	}

	/**
	 * We set the precision magic at 1.0E-7, and if the base is less than the
	 * magic, we consider the base as 0.0
	 * 
	 * @param base
	 * @param d
	 * @return
	 */
	private static boolean equals(double base, double d) {
		if (base - 0.0 < 0.0000001 && base - 0.0 > -0.0000001)
			return true;
		return false;
	}

	public static void main(String[] args) {
		try {
			double result1 = getPower(0.00000000001, -10);
			System.out.println(result1);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		try {
			double result2 = getPower(20, 3);
			System.out.println(result2);
			double result3 = getPower(-10, -5);
			System.out.println(result3);
		} catch (Exception e) {

		}
	}
}
