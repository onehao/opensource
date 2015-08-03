package michael.leetcode.linkedlist;

/**
 * Given a list, rotate the list to the right by k places, where k is
 * non-negative.
 * 
 * For example: Given 1->2->3->4->5->NULL and k = 2, return 4->5->1->2->3->NULL.
 * 
 * @author Michael Wan.
 *
 */
public class RotateList {
	public ListNode rotateRight(ListNode head, int k) {
		if (head == null || head.next == null) {
			return head;
		}
		ListNode last = head;
		int size = 1;
		while (last.next != null) {
			last = last.next;
			size++;
		}
		k = k % size;
		k = size - k;
		// boundary check !!!!!!
		if (k == size || k == 0) {
			return head;
		}

		ListNode result = head;
		for (int i = 1; i < k; i++) {
			result = result.next;
		}

		ListNode resultHead = result.next;
		result.next = null;
		last.next = head;

		return resultHead;
	}

	public static void main(String[] args) {
		ListNode node1 = new ListNode(1);
		ListNode node2 = (node1.next = new ListNode(1));
		ListNode node3 = (node2.next = new ListNode(2));
		ListNode node4 = (node3.next = new ListNode(3));
		ListNode node5 = (node4.next = new ListNode(1));
		ListNode node6 = (node5.next = new ListNode(2));

		ListNode recorderList = new RotateList().rotateRight(node5, 1);
		printNode(recorderList);

	}

	private static void printNode(ListNode node) {
		while (node != null) {
			System.out.print(String.format("%s->", node.val));
			node = node.next;
		}
	}
}
