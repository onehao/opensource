package michael.hackerrank.algorithm.warmup;

import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class LoveLetterMystery {

	    public static void main(String[] args) {
	        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
	        Scanner in = new Scanner(System.in);
	        int T = in.nextInt();
	        String test;
	        for(int i = 0; i < T; i++){
	            test = in.next();
	            System.out.println(calculatePalindromes(test));
	        }
	    }
	    
	    private static int calculatePalindromes(String test){
	        char[] values = test.toCharArray();
	        if(test.length() == 0 || test.length() == 1)
	        return 0;
	        int count = 0;
	        for(int i = 0; i < test.length() >> 1; i++){
	            count += Math.abs(values[i] - values[test.length() - i - 1]);
	        }
	        return count;
	    }
	}