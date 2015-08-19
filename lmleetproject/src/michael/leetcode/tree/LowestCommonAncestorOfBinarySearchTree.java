package michael.leetcode.tree;

import michael.leetcode.basestructure.TreeNode;

public class LowestCommonAncestorOfBinarySearchTree {
	public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
		if(root == null){
			return null;
		}
		if(root == p || root == q){
			return root;
		}
		if(p.val == q.val){
			return p;
		}
		
		if(p.val < q.val){
			if(p.val < root.val && root.val < q.val){
				return root;
			}else if(root.val < p.val){
				return lowestCommonAncestor(root.right, p, q);
			}else{
				return lowestCommonAncestor(root.left, p, q);
			}
		}else{
			if(p.val > root.val && root.val > q.val){
				return root;
			}else if(root.val > p.val){
				return lowestCommonAncestor(root.left, p, q);
			}else{
				return lowestCommonAncestor(root.right, p, q);
			}
		}
	}
}
