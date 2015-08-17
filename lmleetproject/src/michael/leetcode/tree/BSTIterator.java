package michael.leetcode.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import michael.leetcode.basestructure.TreeNode;

/**
 * Definition for binary tree public class TreeNode { int val; TreeNode left;
 * TreeNode right; TreeNode(int x) { val = x; } }
 */

public class BSTIterator {
	List<Integer> inorderSequ = new ArrayList<Integer>();
	Iterator<Integer> bstIterator;

	public BSTIterator(TreeNode root) {
		addInorderNodeValue(inorderSequ, root);
		bstIterator = inorderSequ.iterator();
	}

	private void addInorderNodeValue(List<Integer> result, TreeNode node) {
		if (node == null) {
			return;
		}
		addInorderNodeValue(result, node.left);
		result.add(node.val);
		addInorderNodeValue(result, node.right);
	}

	/** @return whether we have a next smallest number */
	public boolean hasNext() {
		return bstIterator.hasNext();
	}

	/** @return the next smallest number */
	public int next() {
		return bstIterator.next();
	}
}

/**
 * Your BSTIterator will be called like this: BSTIterator i = new
 * BSTIterator(root); while (i.hasNext()) v[f()] = i.next();
 */
