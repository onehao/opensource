package michael.hackerrank.bitmanipulation;

import java.math.BigInteger;
import java.util.Scanner;

public class ChangingBits2 {

	@SuppressWarnings({ "resource", "unused" })
    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
	        int N;
	        int Q;
	        StringBuilder result;

	        
	        Scanner in = new Scanner(System.in);
	        //BufferedReader streamReader = new BufferedReader(new FileReader("F:\\self study\\Dropbox\\Projects\\Algorithm\\BitManipulation_ChangingBits\\input#3.txt"));

	        N = in.nextInt();
	        Q = in.nextInt();
	        in.nextLine();
	        BigInteger A = new BigInteger(in.nextLine(), 2);
	        BigInteger B = new BigInteger(in.nextLine(), 2);
	        
	        int index;
	        String value;

	        result = new StringBuilder();

	        for (int i = 0; i < Q; i++)
	        {
	            //String[] operations = in.next().split(" ");
	            String operations = in.next();
	            if (operations.equals("set_a"))
	            {

//	                index = Integer.parseInt(operations[1]);
//	                value = operations[2].charAt(0);
	                index = in.nextInt();
	                value = in.next();
	                //value = Convert.ToChar(operations[2]);
	                A = setBit(A, value, index);
	                //A = A & nMinusEx2[index];
	                //A = A + ex2[index] * value;
	            }
	            else if (operations.equals("set_b"))
	            {
	            	index = in.nextInt();
	                value = in.next();
	                //value = Convert.ToChar(operations[2]);
	                B = setBit(B, value, index);
	                //B = B & nMinusEx2[index];
	                //B = B + ex2[index] * value;
	            }
	            else// get_c 5
	            {
	            	index = in.nextInt();
	                //value = in.next().charAt(0);
	                BigInteger C = A.add(B); //optimize
	                System.out.print(C.testBit(index) ? '1' : '0');
	                //result.Append(((A + B) & ex2[index]) >> index);
	            }
	            in.nextLine();
	        }
	        System.out.println(result);



	    }

	private static BigInteger setBit(BigInteger bi, String v, int index)
	{
		if(v.equals("0"))
			return bi.clearBit(index);
		else
			return bi.setBit(index);
	}

	   
}