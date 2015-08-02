package michael.leetcode.linkedlist;

import java.util.Comparator;
import java.util.PriorityQueue;

public class MergeKSortedList {
	public ListNode mergeKLists(ListNode[] lists) {
		if (lists.length == 0) {
			return null;
		}
		PriorityQueue<ListNode> queue = new PriorityQueue<ListNode>(
				lists.length, new Comparator<ListNode>() {
					@Override
					public int compare(ListNode o1, ListNode o2) {
						if (o1.val > o2.val) {
							return 1;
						} else {
							return -1;
						}
					}
				});
		for (ListNode list : lists) {
			if (list != null) {
				queue.add(list);
			}
		}
		if(queue.isEmpty()){
		    return null;
		}
		ListNode result = queue.peek();;
		ListNode list = result;
		ListNode current = getNext(queue);
		while (!queue.isEmpty()) {
			if (null != current) {
				list.next = current;
				current = getNext(queue);
				list = list.next;
			}else{
				queue.remove();
			}
		}

		return result;
	}
	
	public ListNode mergeKLists2(ListNode[] lists) {
		if (lists.length == 0) {
			return null;
		}
		PriorityQueue<ListNode> queue = new PriorityQueue<ListNode>(
				lists.length, new Comparator<ListNode>() {
					@Override
					public int compare(ListNode o1, ListNode o2) {
						if (o1.val < o2.val) {
							return 1;
						} else {
							return -1;
						}
					}
				});
		for (ListNode list : lists) {
			if (list != null) {
				queue.add(list);
			}
		}
		ListNode result = queue.poll();
		if(result.next != null){
			queue.add(result.next);
		}
		ListNode list = result;
		ListNode current;
		while (!queue.isEmpty()) {
			current = queue.poll();
			list.next = current;
			if(current.next != null){
				queue.add(current.next);
			}
			
			list = list.next; 
		}

		return result;
	}

	private ListNode getNext(PriorityQueue<ListNode> queue) {
		ListNode current = queue.poll();
		if(current.next != null){
			queue.add(current.next);
		}
		if(queue.isEmpty()){
			return null;
		}else{
			return queue.peek();
		}
	}

	public static void main(String[] args) {
		ListNode result;
		ListNode node1 = new ListNode(5);
		ListNode node2 = new ListNode(2);
		ListNode node3 = new ListNode(0);
		
		node1.next = node2;
		node2.next = node3;
		result =  new MergeKSortedList().mergeKLists2(new ListNode[]{node1});
//		while(result != null){
//			System.out.println(result.val);
//			result = result.next;
//		}
//		
		ListNode node4 = new ListNode(3);
		ListNode node5 = new ListNode(1);
		node4.next = node5;
		
		result =  new MergeKSortedList().mergeKLists2(new ListNode[]{node1, node4});
		while(result != null){
			System.out.println(result.val);
			result = result.next;
		}
//		
//		result =  new MergeKSortedList().mergeKLists2(new ListNode[]{null});
//		while(result != null){
//			System.out.println(result.val);
//			result = result.next;
//		}
		ListNode node6 = new ListNode(3);
		result =  new MergeKSortedList().mergeKLists2(new ListNode[]{node6});
		while(result != null){
			System.out.println(result.val);
			result = result.next;
		}
	}
}

class ListNode {
	int val;
	ListNode next;

	ListNode(int x) {
		val = x;
	}
}
