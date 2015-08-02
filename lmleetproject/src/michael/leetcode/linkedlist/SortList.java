package michael.leetcode.linkedlist;

/**
 * Given a singly linked list L: L0→L1→ ... →Ln-1→Ln, reorder it to:
 * L0→Ln→L1→Ln-1→L2→Ln-2→...
 * 
 * For example, given {1,2,3,4}, reorder it to {1,4,2,3}. You must do this
 * in-place without altering the nodes' values.
 * 
 * @author Michael Wan.
 *
 */
public class SortList {

	public void reorderList(ListNode head) {
		if (head == null || head.next == null) {
			return;
		}

		ListNode middle = getThreadhold(head);

		ListNode pre = head.next;
		ListNode suf = reverse(middle);
		ListNode result = head;
		while (suf != null) {
			result.next = suf;
			suf = suf.next;
			result = result.next;
			result.next = pre;
			result = pre;
			pre = (pre == null ? null : pre.next);

		}
	}

	public static ListNode reorderList2(ListNode head) {
		if (head == null || head.next == null) {
			return head;
		}

		ListNode middle = getThreadhold(head);

		ListNode pre = head.next;
		ListNode suf = reverse(middle);
		ListNode result = head;
		while (suf != null) {
			result.next = suf;
			suf = suf.next;
			result = result.next;
			result.next = pre;
			result = pre;
			pre = (pre == null ? null : pre.next);

		}

		return head;

	}

	private static ListNode reverse(ListNode middle) {
		ListNode head = null;
		ListNode next = middle;
		ListNode temp;
		while (next != null) {
			temp = next.next;
			next.next = head;
			head = next;
			next = temp;
		}
		return head;
	}

	private static ListNode getThreadhold(ListNode head) {
		ListNode fast = head;
		ListNode slow = head;
		ListNode middle = null;

		while (fast.next != null && fast.next.next != null) {
			slow = slow.next;
			fast = fast.next.next;
		}
		middle = slow.next;
		slow.next = null;
		return middle;
	}

	public static void main(String[] args) {
		ListNode node1 = new ListNode(1);
		ListNode node2 = (node1.next = new ListNode(2));
		ListNode node3 = (node2.next = new ListNode(3));
		ListNode node4 = (node3.next = new ListNode(4));
		ListNode node5 = (node4.next = new ListNode(5));
		ListNode node6 = (node5.next = new ListNode(6));

		ListNode recorderList = reorderList2(node1);
		printNode(recorderList);
		System.out.println();
		ListNode node21 = new ListNode(1);
		ListNode node22 = (node21.next = new ListNode(2));
		ListNode node23 = (node22.next = new ListNode(3));
		ListNode node24 = (node23.next = new ListNode(4));
		ListNode node25 = (node24.next = new ListNode(5));
		ListNode node26 = (node25.next = new ListNode(6));

		new SortList().reorderList(node21);
		printNode(node21);
	}

	private static void printNode(ListNode node) {
		while (node != null) {
			System.out.print(String.format("%s->", node.val));
			node = node.next;
		}
	}
}

