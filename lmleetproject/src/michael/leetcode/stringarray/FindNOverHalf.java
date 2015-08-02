package michael.leetcode.stringarray;

/**
 * Find the number in the array, that the occurrence is more than a half.
 * @author Michael Wan.
 *
 */
public class FindNOverHalf {

	public static void main(String[] args) {
		int[] nums = new int[] { 1, 2, 3, 2, 2, 2, 5, 4, 2 };
		System.out.println(findN(nums));
	}

	public static int findN(int[] nums) {
		int seed = nums[0];
		int count = 0;
		for (int num : nums) {
			if (count == 0) {
				seed = num;
				count++;
			}

			if (seed == num) {
				count++;
			} else {
				count--;
			}
		}
		return seed;
	}

}
