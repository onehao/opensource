package michael.hackerrank.algorithm.search;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Scanner;

/**
 * 
 * @author Administrator
 * @see https://www.hackerrank.com/challenges/count-luck
 *
 */

public class CountLuck {

	public static void main(String[] args) throws NumberFormatException, IOException {
		// TODO Auto-generated method stub

		Scanner in = new Scanner(System.in);
		
		BufferedReader reader = new BufferedReader(new FileReader("E:\\Workspace\\gitrepro\\opensource\\lmleetproject\\src\\michael\\hackerrank\\algorithm\\search\\countluckinput03.txt"));
		
		int nCase = Integer.parseInt(reader.readLine());
		
		//int nCase = in.nextInt();
		int N, M, K, L;
		for(int i = 0; i < nCase; i++){
			String[] values = reader.readLine().split(" ");
//			N = in.nextInt();
//			M = in.nextInt();
			
			N = Integer.parseInt(values[0]);
			M = Integer.parseInt(values[1]);
			
			char[][] matrix = new char[N][M];
			for(int j = 0; j < N; j++){
//				matrix[j] = in.next().toCharArray();
				matrix[j] = reader.readLine().toCharArray();
			}
//			L = in.nextInt();
			L = Integer.parseInt(reader.readLine());
			K = calculateWandwaved(matrix);
			
			System.out.println(L);
			System.out.println(K);
			if(K == L){
				System.out.println("Impressed");
			}else{
				System.out.println("Oops!");
			}
		}
		in.close();
	}

	private static int calculateWandwaved(char[][] matrix) {
		// TODO Auto-generated method stub
		int K = 0, count;
		for(int i = 0; i < matrix.length; i++){
			for(int j = 0; j < matrix[0].length; j++){
				count = 0;
				if(matrix[i][j] != '.' && matrix[i][j] != 'M') continue;
				if(i - 1 >= 0 && isPath(matrix[i-1][j])) count++;
				if(j - 1 >= 0 && isPath(matrix[i][j-1])) count++;
				if(i + 1 < matrix.length && isPath(matrix[i+1][j])) count++;
				if(j + 1 < matrix[0].length && isPath(matrix[i][j+1])) count++;
				if(checkCount(matrix[i][j], count)){
					if(count == 2){
						if(i-1 >= 0 && i+1 < matrix.length){
							if(isInLine(matrix[i-1][j], matrix[i+1][j])) continue;
						}
						if(j-1 >= 0 && j+1 < matrix[0].length){
							if(isInLine(matrix[i][j-1], matrix[i][j+1])) continue;
						}
						
					}
					K++;
				}
			}
		}
		return K;
	}
	
	private static boolean isPath(char value){
		if(value == '.' || value == '*') return true;
		return false;
	}
	
	private static boolean isInLine(char A, char B){
		if((A == '.' || A == '*') && (B == '.' || B == '*')) return true;
		return false;
	}

	private static boolean checkCount(char cell, int count) {
		// TODO Auto-generated method stub
		if(cell == '.' && count >= 3) return true;
		if(cell == 'M' && count >= 2) return true;
		return false;
	}
}
