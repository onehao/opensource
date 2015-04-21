package michael.hackerrank.algorithm.sorting;
import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class QuickSortInPlace {

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
		Scanner in = new Scanner(System.in);
		int n = in.nextInt();
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i = 0; i < n; i++){
			list.add(in.nextInt());
		}
		quickSortInPlace(list, n-1, 0);
    }
	
	private static void quickSortInPlace(ArrayList<Integer> list, int pivotIndex, int startIndex){
		if(pivotIndex <= startIndex) return;
		int pivot = list.get(pivotIndex);
		int i = startIndex, j = startIndex;
		while(j < pivotIndex){
			if(list.get(j) < pivot){
				swap(list, i, j);
				j++;
				i++;
				continue;
			}
			j++;
		}
		
		swap(list, i, pivotIndex);
		
		for(int v : list){
			System.out.print(v + " ");
		}
		System.out.println();
		
		quickSortInPlace(list, i - 1, startIndex);
		quickSortInPlace(list, pivotIndex, i + 1);
		
	}
	
	private static void swap(ArrayList<Integer> list, int i, int j){
		int temp = list.get(i);
		list.set(i, list.get(j));
		list.set(j, temp);
	}
}