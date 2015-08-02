package michael.leetcode.stringarray;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Find the kth largest element in an unsorted array. Note that it is the kth
 * largest element in the sorted order, not the kth distinct element.
 * 
 * For example, Given [3,2,1,5,6,4] and k = 2, return 5.
 * 
 * Note: You may assume k is always valid, 1 ≤ k ≤ array's length.
 * 
 * @author Michael Wan
 *
 */
public class KthLargest {
	// define a min heap.
	private PriorityQueue<Integer> heap = null;

	/**
	 * Using a priority queue to resolve the problem, time is O(nlogK).
	 * 
	 * @param nums
	 * @param k
	 * @return
	 */
	public int findKthLargest(int[] nums, int k) {
		if (nums.length < k) {
			return -1;
		}
		heap = new PriorityQueue<Integer>(k, new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				// TODO Auto-generated method stub
				return o1 - o2;
			}
		});

		for (int num : nums) {
			// store in a min heap with max size K.
			if (heap.size() < k) {
				heap.add(num);
			} else {
				// replace the head of the min heap, if the present element is
				// larger than the head.
				if (heap.peek() < num) {
					heap.remove();
					heap.add(num);
				}
			}
		}

		return heap.peek();
	}

	private int[] results = new int[100000];

	/**
	 * Depending on the context, if the number are in a available range, we use
	 * array to resolve the problem, and time is O(n).
	 * 
	 * @param nums
	 * @param k
	 * @return
	 */
	public int findKthLargest2(int[] nums, int k) {
		for (int num : nums) {
			results[results.length - num - 1] += 1;
		}
		int result = 0;

		for (int i = 0; i < results.length; i++) {
			result += results[i];
			if (result >= k) {
				return results.length - i - 1;
			}
		}
		return -1;
	}

	// find the max element among the three<start, middle, end>.
	// and put the middle element to start.
	private void findPivot(int[] nums, int start, int end) {
		int max = nums[start];
		int middle = nums[start + (end - start) >>> 1];
		int min = nums[end];
		if (max < min) {
			swap(nums, start, end);
		}
		if (middle > nums[start]) {
			// start is the middler number.
		} else if (middle < nums[end]) {
			swap(nums, start, end);
		} else {
			swap(nums, start, start + (end - start) >>> 1);
		}
	}

	private void swap(int[] nums, int a1, int a2) {
		int temp = nums[a1];
		nums[a1] = nums[a2];
		nums[a2] = temp;
	}

	public int findKthLargest3(int[] nums, int k) {
		return findKthLargest3(nums, 0, nums.length - 1, k);
	}

	/**
	 * Using the Quick selection to solve the problem, average time is O(n).
	 * 
	 * @param nums
	 * @param start
	 * @param end
	 * @param k
	 * @return
	 */
	private int findKthLargest3(int[] nums, int start, int end, int k) {
		if (start >= end) {
			return nums[end];
		}
		findPivot(nums, start, end);
		int left = start + 1;
		int right = start + 1;
		for (; right <= end; right++) {
			if (nums[right] <= nums[start]) {
				continue;
			} else {
				swap(nums, left, right);
				// left = left < end ? left + 1 : left;
				left++;
			}
		}
		swap(nums, start, left - 1);
		if (left < k) {
			return findKthLargest3(nums, left, end, k);
		} else if (left > k) {
			return findKthLargest3(nums, start, left - 1, k);
		} else {
			return nums[left - 1];
		}
	}

	// define a tree.
	private TreeSet<Integer> tree = null;

	/**
	 * Using red-black tree(based on tree set) to resolve the problem.
	 * 
	 * @param nums
	 * @param k
	 * @return
	 */
	public int findKthLargest4(int[] nums, int k) {
		tree = new TreeSet<Integer>(new Comparator<Integer>(){
			@Override
			public int compare(Integer o1, Integer o2) {
				if(o1 > o2){
					return 1;
				}else{
					return -1;
				}
			}
		});
		
		for (int num : nums) {
			// store in a R-B tree with max size K.
			if (tree.size() < k) {
				tree.add(num);
			} else {
				// replace the head of the R-B tree, if the present element is
				// larger than the head.
				if (tree.first() < num) {
					tree.pollFirst();
					tree.add(num);
				}
			}
		}

		return tree.first();
	}
	
	//define a skip list.
	private ConcurrentSkipListSet<Integer> skipList = null;
	
	/**
	 * Using skip list to resolve the problem.
	 * 
	 * @param nums
	 * @param k
	 * @return
	 */
	public int findKthLargest5(int[] nums, int k) {
		skipList = new ConcurrentSkipListSet<Integer>(new Comparator<Integer>(){
			@Override
			public int compare(Integer o1, Integer o2) {
				if(o1 > o2){
					return 1;
				}else{
					return -1;
				}
			}
		});
		
		for (int num : nums) {
			// store in a skip list with max size K.
			if (skipList.size() < k) {
				skipList.add(num);
			} else {
				// replace the head of the skip list, if the present element is
				// larger than the head.
				if (skipList.first() < num) {
					skipList.pollFirst();
					skipList.add(num);
				}
			}
		}

		return skipList.first();
	}

	public static void main(String[] args) {
		int[] nums = new int[] { 3, 2, 1, 5, 6, 4, 7, 10, 8, 9, 22 };
		System.out.println(new KthLargest().findKthLargest(nums, 2));
		System.out.println(new KthLargest().findKthLargest2(nums, 2));
		System.out.println(new KthLargest().findKthLargest3(nums, 2));
		System.out.println(new KthLargest().findKthLargest4(nums, 2));
		System.out.println(new KthLargest().findKthLargest5(nums, 2));

		int[] nums2 = new int[] { 1, 2 };
		System.out.println(new KthLargest().findKthLargest3(nums2, 1));
	}
}