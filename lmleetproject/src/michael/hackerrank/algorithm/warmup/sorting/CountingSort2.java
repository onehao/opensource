package michael.hackerrank.algorithm.warmup.sorting;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class CountingSort2 {

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        ArrayList<Integer> list = new ArrayList<Integer>();
        for(int i = 0; i < n; i++){
            list.add(in.nextInt());
        }
        
        Collections.sort(list);
        for(int v : list){
            System.out.print(v + " ");
        }
        in.close();
    }
}