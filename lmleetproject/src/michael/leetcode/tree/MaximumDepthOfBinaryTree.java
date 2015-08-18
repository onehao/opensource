package michael.leetcode.tree;

import michael.leetcode.basestructure.TreeNode;

public class MaximumDepthOfBinaryTree {
	public int maxDepth(TreeNode root) {
		if(root == null){
			return 0;
		}
		return Math.max(maxDepth(root.left) + 1, maxDepth(root.right) + 1);
	}
	
	public static void main(String[] args) {
		TreeNode n1 = new TreeNode(1);
		TreeNode n2 = new TreeNode(2);
		n1.left = n2;
		
		System.out.println(new MaximumDepthOfBinaryTree().maxDepth(n1));
	}
}
