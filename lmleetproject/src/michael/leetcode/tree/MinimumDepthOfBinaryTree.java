package michael.leetcode.tree;

import michael.leetcode.basestructure.TreeNode;

public class MinimumDepthOfBinaryTree {
	public int minDepth(TreeNode root) {
		if(root == null){
			return 0;
		}
		if(root.left == null){
			return getMinDepth(root.right) + 1;
		}
		if(root.right == null){
			return getMinDepth(root.left) + 1;
		}
		return getMinDepth(root);
	}
	
	public int getMinDepth(TreeNode root) {
		if(root == null){
			return 0;
		}
		if(root.left == null){
			return getMinDepth(root.right) + 1;
		}
		if(root.right == null){
			return getMinDepth(root.left) + 1;
		}
		return Math.min(getMinDepth(root.left) + 1, getMinDepth(root.right) + 1);
	}
	
	public static void main(String[] args) {
		TreeNode n1 = new TreeNode(1);
		TreeNode n2 = new TreeNode(2);
		n1.left = n2;
		
		System.out.println(new MinimumDepthOfBinaryTree().minDepth(n1));
	}
}
