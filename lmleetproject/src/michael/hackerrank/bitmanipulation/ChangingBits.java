package michael.hackerrank.bitmanipulation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ChangingBits {

	/**
	 * @param args
	 * @throws IOException 
	 */
	@SuppressWarnings({ "resource", "unused" })
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		{
	        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution */
	        int N;
	        int Q;
	        StringBuilder result;

	        long time = System.currentTimeMillis();;

	        int a = '0' ^ '1';
	        a = '1' ^ '1';
	        a = '1' ^ '0';
	        a = '0' ^ '0';

	        System.out.println((System.currentTimeMillis() - time) / 1000);
	        a = '0' + '1';
	        a = '1' + '1';
	        a = '1' + '0';
	        a = '0' + '0';

	        System.out.println((System.currentTimeMillis() - time) / 1000);
	        
	        BufferedReader streamReader = new BufferedReader(new FileReader("F:\\self study\\Dropbox\\Projects\\Algorithm\\BitManipulation_ChangingBits\\input#3.txt"));

	            String[] nq = streamReader.readLine().split(" ");
	            
	            N = Integer.parseInt(nq[0]);
	            Q = Integer.parseInt(nq[1]);

	        char[] A = streamReader.readLine().toCharArray();
	        char[] B = streamReader.readLine().toCharArray();
	        
	        A = reverse(A);
	        B = reverse(B);
	        int index;
	        char value;

	        result = new StringBuilder();

	        for (int i = 0; i < Q; i++)
	        {
	            String[] operations = streamReader.readLine().split(" ");
	            if (operations[0].equals("set_a"))
	            {

	                index = Integer.parseInt(operations[1]);
	                value = operations[2].charAt(0);
	                //value = Convert.ToChar(operations[2]);
	                A[index] = value;
	                //A = A & nMinusEx2[index];
	                //A = A + ex2[index] * value;
	            }
	            else if (operations[0].equals("set_b"))
	            {
	            	index = Integer.parseInt(operations[1]);
	                value = operations[2].charAt(0);
	                //value = Convert.ToChar(operations[2]);
	                B[index] = value;
	                //B = B & nMinusEx2[index];
	                //B = B + ex2[index] * value;
	            }
	            else// get_c 5
	            {
	            	index = Integer.parseInt(operations[1]);
	                int C = adds(A, B, index, N); //�Ż�
	                result.append(C);
	                //result.Append(((A + B) & ex2[index]) >> index);
	            }
	            
	        }
	        System.out.println((System.currentTimeMillis() - time) / 1000);
	        System.out.println(result);
	        System.out.println(result.toString());

	        //int a = 2;
	        //int b = 6;
	        //int c = 5;

	        //int d = ex2[1];
	        //System.out.println((a & b) >> 1);
	        //System.out.println((a & c) >> 1);
	        System.in.read();
	        }
	    }
	
		private static char[] reverse (char[] chars)
		{
			char[] result = new char[chars.length];
			
			int n = chars.length;
			
			for(int i = 0; i < n-1; i++)
			{
				result[i] = chars[n-1-i];
			}
			
			return result;
		}

	    public static int adds(char[] ch1, char[] ch2, int length, int N)
	    {
	        int result;

	        char temp1;

	        if (length > N - 1)
	        {
	            if ((ch1[length - 1] ^ ch2[length - 1]) > 0)
	            {
	                temp1 = '0';
	            }
	            else
	            {
	                //return 1;
	                if (ch1[length - 1] == '1')
	                    return 1;
	                else
	                    return 0;
	            }
	        }
	        else
	        {
	            if((ch1[length] ^ ch2[length]) > 0)
	                temp1 = '1';
	            else
	                temp1 = '0';
	        }

	        char temp = '0';

	        for (int i = length - 1; i >= 0 ; i--)
	        {
	            if (!((ch1[i] ^ ch2[i]) == 1))//if(ch1[i].Equals(ch2[i]))//
	            {
	                if (ch1[i]=='1')
	                    return '1' ^ temp1;
	                    //temp = '1';
	                break;
	            }
	            
	        }
	        result = temp1 ^ temp;
	        return result;
	    }
	}