package michael.leetcode.tree;

import java.util.Stack;

import michael.leetcode.basestructure.TreeNode;

public class FlattenBinaryTreeToLinkedList {
	public void flatten(TreeNode root) {

		if (null == root) {
			return;
		}
		TreeNode result = root;
		addNode(result, root);
		
	}

	private void addNode(TreeNode current, TreeNode node) {
		//must check boundaries.
		if(null == node){
			return ;
		}
		TreeNode right = node.right;
		if(node.left != null){
			current.right = node.left;
			current.left = null;
			current = current.right;
			
			addNode(current, node.left);
			
		}
		
		if(right != null){
			current.right = right;
			current.left = null;
			current = current.right;
			addNode(current, right);
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
		n1.left = n2;
		n2.left = n3;
		new FlattenBinaryTreeToLinkedList().flatten(n1);
		
		BSTIterator iterator = new BSTIterator(n1);
		while(iterator.hasNext()){
			System.out.print(iterator.next() + " -> ");
		}
	}

}
