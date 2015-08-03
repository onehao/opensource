package michael.leetcode.linkedlist;

/**
 * Merge two sorted linked lists and return it as a new list. The new list
 * should be made by splicing together the nodes of the first two lists.
 * 
 * @author Michael Wan.
 *
 */
public class MergeTwoSortedList {
	public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
		if (l1 == null) {
			return l2;
		}
		if (l2 == null) {
			return l1;
		}
		ListNode result = new ListNode(0);
		ListNode temp = result;
		while (l1 != null && l2 != null) {
			if (l1.val < l2.val) {
				temp.next = l1;
				l1 = l1.next;

			} else {
				temp.next = l2;
				l2 = l2.next;
			}
			temp = temp.next;

		}
		temp.next = l1 != null ? l1 : l2;
		return result.next;
	}

	public static void main(String[] args) {

		ListNode node1 = new ListNode(1);
		ListNode node2 = new ListNode(2);

		ListNode recorderList = new MergeTwoSortedList().mergeTwoLists(node1,
				node2);
		printNode(recorderList);

	}

	private static void printNode(ListNode node) {
		while (node != null) {
			System.out.print(String.format("%s->", node.val));
			node = node.next;
		}
	}
}
