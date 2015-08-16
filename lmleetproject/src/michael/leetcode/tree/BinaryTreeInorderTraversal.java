package michael.leetcode.tree;

import java.util.ArrayList;
import java.util.List;

import michael.leetcode.basestructure.TreeNode;

/**
 * Given a binary tree, return the inorder traversal of its nodes' values.

	For example:
	Given binary tree {1,#,2,3},
	   1
	    \
	     2
	    /
	   3
	return [1,3,2].
	
	Note: Recursive solution is trivial, could you do it iteratively?
	
	confused what "{1,#,2,3}" means? > read more on how binary tree is serialized on OJ.
 * @author Michael Wan.
 *
 */
public class BinaryTreeInorderTraversal {
	private List<Integer> result = new ArrayList<Integer>();

	public List<Integer> inorderTraversal(TreeNode root) {
		addInorderNodeValue(result, root);
		return result;
	}

	private void addInorderNodeValue(List<Integer> result, TreeNode node) {
		if (node == null) {
			return;
		}
		addInorderNodeValue(result, node.left);
		result.add(node.val);
		addInorderNodeValue(result, node.right);
	}
}
