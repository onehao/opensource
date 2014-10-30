package hackerrank;
/* Head ends here */
import java.util.*;
public class QuickSort {
       
   static void quickSort(int[] ar) {
		if (ar.length == 0)
			return;
		int pivot = ar[0];
        int n = ar.length;
		ArrayList<Integer> list1 = new ArrayList<Integer>();
		ArrayList<Integer> list2 = new ArrayList<Integer>();
		
		
		int k = 0, j =0;
		for (int i = 1; i < ar.length; i++) {
			if (ar[i] > pivot)
				list1.add(ar[i]);
			else
				list2.add(ar[i]);
		}
		
		int[] left = toArray(list2);
		int[] right = toArray(list1);
		
        int index = 0;
		quickSort(left);
        if(left.length > 0);
        for(int c : left)
        {
            ar[index++] = c;
        }
        ar[index++] = pivot;
		quickSort(right);
		if(right.length > 0);
        for(int c : right)
        {
            ar[index++] = c;
        }
        if( n > 1)
           printArray(ar); 
	}
	
	static int[] toArray(ArrayList<Integer> list)
	{
		int[] array = new int[list.size()];
		int k = 0;
		for(int i : list)
		{
			array[k++] = i;
		}
		return array;
	}

/* Tail starts here */
 
 static void printArray(int[] ar) {
         for(int n: ar){
            System.out.print(n+" ");
         }
           System.out.println("");
      }
       
      public static void main(String[] args) {
           Scanner in = new Scanner(System.in);
           int n = in.nextInt();
           int[] ar = new int[n];
           for(int i=0;i<n;i++){
              ar[i]=in.nextInt(); 
           }
           quickSort(ar);
       }    
   }
