package hackerrank.string;

import java.util.Scanner;

public class StringSimilarity {

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        Scanner in = new Scanner(System.in);
    int n1 = in.nextInt();
        int[] os = new int[n1];
        StringBuilder target;
        for (int i = 0; i < n1; i++)
        {
            target = new StringBuilder(in.next());
            os[i] = getSubStringSimilarity(target);
            target = null;
            
        }
        for(int i = 0; i < n1; i++)
        {
            System.out.println(os[i]);
        }
    }
    
    static int getSubStringSimilarity(StringBuilder target)
    {
        int n = target.length();
        int similarity = n;
        for(int i = 1; i < target.length(); i++)
        {
            int index = 0;
            for(int j = 0; j < target.length() - i; j++)
            {
                if(target.charAt(j) == target.charAt(i+j))
                  index++;
                else
                    break;
            }
            similarity += index;
        }
        return similarity;
    }
}