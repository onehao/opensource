/**
 * 
 */
package michael.leetcode.tree;

import java.util.ArrayList;
import java.util.List;

import michael.leetcode.basestructure.TreeNode;

/**
 * Given a binary tree, return the preorder traversal of its nodes' values.

	For example:
	Given binary tree {1,#,2,3},
	   1
	    \
	     2
	    /
	   3
	return [1,2,3].
	
	Note: Recursive solution is trivial, could you do it iteratively?
 * @author Michael Wan
 *
 */
public class BinaryTreePreorderTraversal {
	private List<Integer> result = new ArrayList<Integer>();

	public List<Integer> preorderTraversal(TreeNode root) {
		addPreorderNodeValue(result, root);
		return result;
	}
	
	private void addPreorderNodeValue(List<Integer> result, TreeNode node){
		if(null == node){
			return;
		}
		result.add(node.val);
		
		addPreorderNodeValue(result, node.left);
		addPreorderNodeValue(result, node.right);
	}
}
