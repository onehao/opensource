package linda.leetcode;

public class Single_Number {
	public int singleNumber(int[] A) {
		// int[] B = A;
		// for (int i = 0; i < B.length; i++) {
		// for (int j = 0; j < A.length; j++) {
		// if (i != j) {
		// if (B[i] == A[j]) {
		// break;
		// }
		// else if(j == A.length-1){
		// System.out.println(B[i]);
		// }
		// }
		// }
		// }
		// return 0;
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		int result = 0;
		for (int i = 0; i < 32; i++) {
			int count = 0;
            for(int j = 0;j < A.length;j++){
            	count += ((A[j]>>i)&1);
            }
            result += ((count % 3) << i);
		}
		System.out.println(result);
		return result;
	}
	
	//you can see who write what.

	public static void main(String[] args) {
		Single_Number sn = new Single_Number();
		// int[] AA = new int[] {5,7,5,4,7,4,3};
		int[] AA = new int[] {5,3,7,3,5,7,3,7,5,8};
//		int[] AA = new int[] { 3 };
		sn.singleNumber(AA);
	}
}
