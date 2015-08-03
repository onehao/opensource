package michael.leetcode.linkedlist;

/**
 * Given a sorted linked list, delete all duplicates such that each element
 * appear only once.
 * 
 * For example, Given 1->1->2, return 1->2. Given 1->1->2->3->3, return 1->2->3.
 * 
 * @author Michael Wan.
 *
 */
public class RemoveDuplicateFromSortedList {
	public ListNode deleteDuplicates(ListNode head) {
		// do make sure to check boundaries.
		if(head == null || head.next == null){
			return head;
		}
		ListNode result = head;
		while(result.next != null){
			if(result.val == result.next.val){
				result.next = result.next.next;
				continue;
			}
			result = result.next;
		}
		
		return head;
	}
	
	public static void main(String[] args) {
		ListNode node1 = new ListNode(1);
		ListNode node2 = (node1.next = new ListNode(1));
		ListNode node3 = (node2.next = new ListNode(2));
		ListNode node4 = (node3.next = new ListNode(2));
		ListNode node5 = (node4.next = new ListNode(2));
		ListNode node6 = (node5.next = new ListNode(6));

		ListNode recorderList = new RemoveDuplicateFromSortedList().deleteDuplicates(node1);
		printNode(recorderList);

	}

	private static void printNode(ListNode node) {
		while (node != null) {
			System.out.print(String.format("%s->", node.val));
			node = node.next;
		}
	}
}
