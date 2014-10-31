package linda.leetcode;


/* Definition for singly-linked list.*/
class ListNode {
     int val;
     ListNode next;
     ListNode(int x) {
         val = x;
         next = null;
     }
 }

public class Linked_List_Cycle {
    public boolean hasCycle(ListNode head) {
    	Object slow = new Object();
    	Object fast = new Object();
    	slow = head;
    	fast = head;
    	if (( slow == fast )); 
    	{
    	slow = head.next;
    	fast = head.next.next;
    	}
		return false;
        
    }
}