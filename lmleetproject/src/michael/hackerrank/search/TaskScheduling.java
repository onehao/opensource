package michael.hackerrank.search;

import java.util.Scanner;
import java.util.TreeSet;

public class TaskScheduling {
	
	public static void main(String[] args)
	{
		Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int[] ar = new int[n];
        TreeSet<TaskSchedule> set = new TreeSet<TaskSchedule>();
        int[] os = new int[n];
        for(int i=0;i<n;i++){
           int di = in.nextInt();
           int mi = in.nextInt();
           
           set.add(new TaskSchedule(di, mi));
           
           
           int totalMi = 0, overshoot = 0;
           for (TaskSchedule ts : set)
           {
               totalMi += ts.mi;
               int temp = totalMi - ts.di;
               overshoot = (temp > overshoot) ? temp : overshoot;
               os[i] = overshoot;
           }
           
           
        }
        for (int i = 0; i < n; i++)
        {
            System.out.println(os[i]);
        }

	}

}

class TaskSchedule implements Comparable<TaskSchedule>
{
    public TaskSchedule(int di, int mi)
    {
        this.di = di;
        this.mi = mi;
    }
    int di;
    int mi;
	@Override
	public int compareTo(TaskSchedule y) {
		if (this.di > y.di)
            return 1;
        else if (this.di < y.di)
            return -1;
        else
        {
            int temp = this.mi - y.mi;
            return temp > 0 ? 1 : temp < 0 ? -1 : 0;
        }
	}
}
