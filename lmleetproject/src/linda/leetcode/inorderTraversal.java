package linda.leetcode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Definition for binary tree
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * }
 */
public class inorderTraversal {
	public List<Integer> inorder(TreeNode root) {
		List<Integer> order = new ArrayList<Integer>();
		TreeNode tn = root;
		Stack<TreeNode> st = new Stack<TreeNode>();
		if(tn == null) return order;
        while(!st.isEmpty()||tn!=null){
            if(tn!=null){
                st.push(tn); 
                tn=tn.left;
            }else{
                tn=st.pop();
                order.add(tn.val);
                tn=tn.right;
            }
        }
        System.out.println(order);
		return order;
	}
	public static void main(String[] args) {
	    inorderTraversal it = new inorderTraversal();
	    TreeNode leftnode = new TreeNode(2);
        leftnode.left = leftnode.right = null;
	    
        TreeNode rightnode = new TreeNode(3);
        rightnode.left = rightnode.right = null;
        
        TreeNode mynode = new TreeNode(1);

        mynode.left = leftnode;
        mynode.right = rightnode;

        it.inorder(mynode);
	}

}
