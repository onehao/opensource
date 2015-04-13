package michael.hackerrank.algorithm.warmup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class IntroToTutorialChallenges {

	public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int target = in.nextInt();
        ArrayList<Integer> list = new ArrayList<Integer>();
        for(int i = 0; i < n; i++){
            list.add(in.nextInt());
        }
        
        int index = getTargetIndex(list,target);
        
        System.out.println(index);
        
        in.close();
    }
    
    private static int getTargetIndex(ArrayList<Integer> list, int target){
    	return Collections.binarySearch(list,target);
    }

}
