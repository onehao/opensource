package michael.hackerrank.algorithm.sorting;
import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class CountingSort1 {

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
		Scanner in = new Scanner(System.in);
		
		int n = in.nextInt();
		
		int[] counts = new int[100];
		for(int i = 0; i < n; i++){
			counts[in.nextInt()]++;
		}
		
		for(int i = 0; i < 100; i++){
			System.out.print(counts[i] + " ");
		}
    }
}