package michael.leetcode.tree;

import java.util.Deque;
import java.util.LinkedList;

import michael.leetcode.basestructure.TreeNode;


/**
 * Invert a binary tree.
 * 
 *       4
	   /   \
	  2     7
	 / \   / \
	1   3 6   9
	to
	     4
	   /   \
	  7     2
	 / \   / \
	9   6 3   1
	Trivia: This problem
 * was inspired by this original tweet by Max Howell: Google: 90% of our
 * engineers use the software you wrote (Homebrew), but you can’t invert a
 * binary tree on a whiteboard so fuck off.
 * 
 * @author Administrator
 *
 */
public class InvertBinaryTree {

	public TreeNode invertTree(TreeNode root) {
		if (null == root) {
			return root;
		}
		TreeNode temp = root.left;
		root.left = root.right;
		root.right = temp;

		invertTree(root.left);
		invertTree(root.right);

		return root;
	}

	public TreeNode invertTree2(TreeNode root) {
		if (null == root) {
			return root;
		}
		Deque<TreeNode> queue = new LinkedList<TreeNode>();
		queue.push(root);
		TreeNode current;
		while (!queue.isEmpty()) {
			current = queue.poll();
			TreeNode temp = current.left;
			current.left = current.right;
			current.right = temp;
			if (current.left != null) {
				queue.push(current.left);
			}
			if (current.right != null) {
				queue.push(current.right);
			}
		}

		return root;
	}
}
