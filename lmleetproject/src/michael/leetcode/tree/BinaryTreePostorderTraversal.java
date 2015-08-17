package michael.leetcode.tree;

import java.util.ArrayList;
import java.util.List;

import michael.leetcode.basestructure.TreeNode;

/**
 * Given a binary tree, return the postorder traversal of its nodes' values.

	For example:
	Given binary tree {1,#,2,3},
	   1
	    \
	     2
	    /
	   3
	return [3,2,1].
	
	Note: Recursive solution is trivial, could you do it iteratively?
 * @author Michael Wan.
 *
 */
public class BinaryTreePostorderTraversal {
	private List<Integer> result = new ArrayList<Integer>();

	public List<Integer> postorderTraversal(TreeNode root) {
		addPostorderNodeValue(result, root);
		return result;
	}

	private void addPostorderNodeValue(List<Integer> result, TreeNode node) {
		if (node == null) {
			return;
		}
		addPostorderNodeValue(result, node.left);
		addPostorderNodeValue(result, node.right);

		result.add(node.val);
	}
}
