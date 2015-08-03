package michael.leetcode.linkedlist;

/**
 * You are given two linked lists representing two non-negative numbers. The
 * digits are stored in reverse order and each of their nodes contain a single
 * digit. Add the two numbers and return it as a linked list.
 * 
 * Input: (2 -> 4 -> 3) + (5 -> 6 -> 4) Output: 7 -> 0 -> 8
 * 
 * @author Michael Wan.
 *
 */
public class AddTwoNumbers {
	public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
		// check null conditino.
		if (l1 == null) {
			return l2;
		}
		if (l2 == null) {
			return l1;
		}

		ListNode result = new ListNode((l1.val + l2.val) % 10);
		int carry = (l1.val + l2.val) > 9 ? 1 : 0;
		ListNode head = result;
		while (l1.next != null & l2.next != null) {
			l1 = l1.next;
			l2 = l2.next;
			result.next = new ListNode((l1.val + l2.val + carry) % 10);
			carry = (l1.val + l2.val + carry) > 9 ? 1 : 0;
			result = result.next;
		}
		if (carry == 0) {
			result.next = l1.next == null ? l2.next : l1.next;
		} else {
			addToEnd(result, l1.next == null ? l2.next : l1.next);
		}

		return head;

	}

	/**
	 * 
	 * @param result
	 *            the result list.
	 * @param node
	 *            the node that is not null;
	 */
	private void addToEnd(ListNode result, ListNode node) {
		int carry = 1;
		while (node != null && carry == 1) {
			result.next = new ListNode((node.val + 1) % 10);
			carry = (node.val + 1) > 9 ? 1 : 0;
			node = node.next;
			// remember when assign value, move to the next
			result = result.next;
		}
		if (node != null) {
			result.next = node;
		} else {
			if (carry == 1) { // this condition is double check that both node
								// == null and carry == 0 is true
				result.next = new ListNode(1);
			}
		}
	}

	public static void main(String[] args) {
		ListNode A1 = new ListNode(9);
		ListNode A2 = new ListNode(9);
		ListNode A3 = new ListNode(3);
		A1.next = A2;
		// A2.next = A3;

		ListNode B1 = new ListNode(1);
		// ListNode B2 = new ListNode(6);
		// ListNode B3 = new ListNode(4);
		// B1.next = B2;
		// B2.next = B3;

		ListNode result = new AddTwoNumbers().addTwoNumbers(A1, B1);
		printNode(result);
	}

	private static void printNode(ListNode node) {
		while (node != null) {
			System.out.print(String.format("%s->", node.val));
			node = node.next;
		}
	}
}
