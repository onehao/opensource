package linda.leetcode;

public class numTrees {
public int numTrees(int n) {
		if(n == 0 || n == 1)
			return 1;
		else
		{   
			int sum=0;
			for (int root=1; root<=n; root++){
				sum += numTrees(root-1)*numTrees(n-root);
			}
//			System.out.println(sum);
			return sum;
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		numTrees nt = new numTrees();
		nt.numTrees(3);

	}

}
