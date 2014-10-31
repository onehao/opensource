package linda.leetcode;

import java.lang.Math;

public class Depth_BT {


	public int maxDepth(TreeNode root) {
		if(root == null) return 0;
		int l = maxDepth(root.left);
		int r = maxDepth(root.right);
		return Math.max(l,r)+1;
	    }
    public boolean isSameTree(TreeNode p, TreeNode q) {
    	if(p == null && q == null){
    		return true;
    	}else if(p != null && q != null){
    		if(p.val == q.val)
    		return isSameTree(p.left,q.left) && isSameTree(p.right,q.right);
    	}else{
    		return false;
    	}	
		return false;  	
    }

	public static void main(String[] args) {
	    Depth_BT md = new Depth_BT();
	    TreeNode tn1 = new TreeNode(20);
	    tn1.left = tn1.right = null;
	    TreeNode tn2 = new TreeNode(30);	    
	    tn2.left = tn2.right = null;	
	    TreeNode tn = new TreeNode(10);
	    tn.left = tn1;
	    tn.right = tn2;    
        md.maxDepth(tn);
	}
}
