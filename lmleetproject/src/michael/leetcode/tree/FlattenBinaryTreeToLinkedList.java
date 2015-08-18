package michael.leetcode.tree;

import java.util.Stack;

import michael.leetcode.basestructure.TreeNode;

public class FlattenBinaryTreeToLinkedList {

	public void flatten(TreeNode root) {
		if (null == root) {
			return;
		}
		current = root;
		addNode(root);
	}
	
	private TreeNode current;

	private void addNode(TreeNode node) {
		//must check boundaries.
		if(null == node){
			return ;
		}
		TreeNode right = node.right;
		TreeNode left = node.left;
		if(node.left != null){
			current.right = left;
			current.left = null;
			current = current.right;
			addNode(left);
		}
		
		if(right != null){
			current.right = right;
			current.left = null;
			current = current.right;
			addNode(right);
		}
	}
	
	public void flatten2(TreeNode root) {
        Stack<TreeNode> stack = new Stack<TreeNode>();
        TreeNode p = root;
 
        while(p != null || !stack.empty()){
 
            if(p.right != null){
                stack.push(p.right);
            }
 
            if(p.left != null){
                p.right = p.left;
                p.left = null;
            }else if(!stack.empty()){
                TreeNode temp = stack.pop();
                p.right=temp;
            }
 
            p = p.right;
        }
    }
	
	public static void main(String[] args) {
		TreeNode n1 = new TreeNode(1);
		TreeNode n2 = new TreeNode(2);
		TreeNode n3 = new TreeNode(3);
		TreeNode n4 = new TreeNode(4);
		TreeNode n5 = new TreeNode(5);
		n1.left = n2;
		n2.left = n3;
		n2.right = n4;
		n3.left = n5;
		new FlattenBinaryTreeToLinkedList().flatten(n1);
		
		BSTIterator iterator = new BSTIterator(n1);
		while(iterator.hasNext()){
			System.out.print(iterator.next() + " -> ");
		}
	}

}
