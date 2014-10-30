package hackerrank;

import java.util.ArrayList;

public class QuickSort1 {
	static void partition(int[] ar) {
        if(ar.length == 0) return;
        int pivot = ar[0];
        ArrayList<Integer> list1 = new ArrayList<Integer>();
        ArrayList<Integer> list2 = new ArrayList<Integer>();
        
        for(int i = 1; i < ar.length; i++)
        {
            if(ar[i] > pivot) list2.add(ar[i]);
            else list1.add(ar[i]);
        }
        int i = 0;
        for(int value : list1)
        {
            ar[i++] = value;
        }
        
        ar[i] = pivot;
        
        for(int value : list2)
        {
            ar[++i] = value;
        }
 }  
	
	public static void main(String[] args)
	{
		int[] ar = new int[]{4, 5, 3, 7, 2};
		
		partition(ar);
		
		for(int i = 0; i < ar.length; i++)
		{
			System.out.print(ar[i] + " ");
		}
	}
}
