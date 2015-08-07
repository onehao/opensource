package michael.leetcode.linkedlist;

/**
 * Given a sorted linked list, delete all nodes that have duplicate numbers,
 * leaving only distinct numbers from the original list.
 * 
 * For example, Given 1->2->3->3->4->4->5, return 1->2->5. Given 1->1->1->2->3,
 * return 2->3.
 * 
 * @author Michael Wan.
 *
 */
public class RemoveDuplicateFromSortedList2 {
    public ListNode deleteDuplicates(ListNode head) {
        // do make sure to check boundaries.
        if (head == null || head.next == null) {
            return head;
        }
        ListNode result = new ListNode(0);
        ListNode resultHead = result;
        ListNode checkDup = head;
        boolean isRemoveCurrent = false;
        while (head.next != null) {
            if (head.val == head.next.val) {
                head = head.next;
                isRemoveCurrent = true;
                continue;
            }
            if (!isRemoveCurrent) {
                result.next = checkDup;
                checkDup = checkDup.next;

                // when 2 duplicate element in the end of the list, make sure
                // the previous element not append them.
                result.next.next = null;
            } else {
                checkDup = head.next;
                isRemoveCurrent = false;
                head = checkDup;
                continue;
            }

            result = result.next;
            isRemoveCurrent = false;
            head = checkDup;

        }

        // remember to check last element after loop, for linked list.
        if (!isRemoveCurrent) {
            result.next = head;
        }
        return resultHead.next;
    }
    
    public ListNode deleteDuplicates2(ListNode head) {
        // do make sure to check boundaries.
        if (head == null || head.next == null) {
            return head;
        }
        ListNode result = new ListNode(0);
        ListNode resultHead = result;
        ListNode checkDup = head;
        ListNode dupNode = null;
        while(checkDup != null && checkDup.next != null){
            //duplicate
            if(checkDup.val == checkDup.next.val){
                dupNode = checkDup;
                checkDup = checkDup.next;
                while(checkDup.next != null && dupNode.val == checkDup.next.val){
                    checkDup = checkDup.next;
                }
                result.next = checkDup.next;
                
            }else{
                result.next = checkDup;
                result = result.next;
            }
            checkDup = checkDup.next;
        }
        
        return resultHead.next;
    }

    public static void main(String[] args) {
        ListNode node1 = new ListNode(1);
        ListNode node2 = (node1.next = new ListNode(1));
        ListNode node3 = (node2.next = new ListNode(2));
        ListNode node4 = (node3.next = new ListNode(3));
        ListNode node5 = (node4.next = new ListNode(6));
        ListNode node6 = (node5.next = new ListNode(6));

        ListNode recorderList = new RemoveDuplicateFromSortedList2()
                .deleteDuplicates2(node1);
        printNode(recorderList);

    }

    private static void printNode(ListNode node) {
        while (node != null) {
            System.out.print(String.format("%s->", node.val));
            node = node.next;
        }
    }
}
