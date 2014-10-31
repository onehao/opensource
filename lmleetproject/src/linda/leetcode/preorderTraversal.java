package linda.leetcode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class preorderTraversal {
    public List<Integer> preorder(TreeNode root){
    	List<Integer> order = new ArrayList<Integer>();
    	if(root == null) return order;
    	  	
    	Stack<TreeNode> st = new Stack<TreeNode>();

    	st.push(root);
    	
    	while(!st.empty()){
        	TreeNode tn = st.pop();
    	

    	
    		order.add(tn.val);
    		if(tn.right != null){
    			st.push(tn.right);
    		}
    		if(tn.left != null){
    			st.push(tn.left);
    		}
    	}
    	System.out.println(order);
		return order;
    }
	public static void main(String[] args) {
	    preorderTraversal pt = new preorderTraversal();
	    TreeNode leftnode = new TreeNode(4);
        leftnode.left = leftnode.right = null;
	    
        TreeNode rightnode = new TreeNode(3);
        rightnode.left = rightnode.right = null;
        
        TreeNode mynode = new TreeNode(1);

        mynode.left = leftnode;
        mynode.right = rightnode;

        pt.preorder(mynode);
	}
}
