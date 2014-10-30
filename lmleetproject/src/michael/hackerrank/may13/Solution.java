package michael.hackerrank.may13;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
class Entry implements Comparable<Entry> {
		public static int n = 3;
		//public static int[] dmap;
	    
	    public static HashMap<Integer, int[]> mymap = new HashMap<Integer, int[]>();
	    static
	    {
	    	int[] dmap1 = {0, 1, 2, 3, 4, 5, 6, 7, 8}; 
		    int[] dmap2 = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
		    int[] dmap3 = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24};
	    	mymap.put(3, dmap1);
	    	mymap.put(4, dmap2);
	    	mymap.put(5, dmap3);

	    }
	    
	    public int[] map = new int[n*n];   
	    public Entry parent = null;     
	    public int priority = 0;          
	    public int direction = 0;        
	    public int deep = 0;         
	    public int redirection = -1;
	    

	    public int compareTo(Entry o) {
	        return this.priority - o.priority; 
	    }

	    @Override
	    public boolean equals(Object obj) {
	        Entry entry = (Entry) obj;
	        for (int i = 0; i < n*n; i++) {
	            if (this.map[i] != entry.map[i]) {
	                return false;
	            }
	        }
	        return true;
	    }

	    @Override
	    public int hashCode() {
	        return Arrays.toString(this.map).hashCode();
	    }
	}

	public class Solution { 

	    public static int[] totalPath; 
	    
	    public static int n = 3;
	    
	    public boolean check(int[] map) {
	        for (int i = 0; i < n*n; i++) {
	            if (map[i] != Entry.mymap.get(n)[i]) {
	                return false;
	            }
	        }
	        return true;
	    }


	    public int calPriority(int[] map) {
	        int wrongNum = 0;  
	        int totalNum = 0; 
	        int overNum = 0;  
	        for (int i = 0; i < n*n; i++) {
	            if (map[i] != Entry.mymap.get(n)[i]) {
	                wrongNum++;  
	                totalNum = Math.abs(map[i] - Entry.mymap.get(n)[i]); 
	            }

	            if (i != 0) {
	                if ((map[i] == map[i - 1] + 1) || (map[i] == map[i - 1] - 1)) {
	                    overNum += 2;  
	                }
	            }


	        }
	        return wrongNum + totalNum + overNum;
	    }
	    
	    private void printDirection(int x)
	    {
	    	if(x == 2)
	    	{
	    		System.out.println("DOWN");
	    	}
	    	else if( x == 0)
	    	{
	    		System.out.println("UP");
	    	}
	    	
	    	else if(x == 1)
	    	{
	    		System.out.println("RIGHT");
	    	}
	    	else // x == 3
	    	{
	    		System.out.println("LEFT");
	    	}
	    	
	    }


	    public boolean search(int[] map) {
	        PriorityQueue<Entry> queue = new PriorityQueue<Entry>(326888);  
	        Set<Entry> set = new HashSet<Entry>(326888);   
	        Set<Entry> liveSet = new HashSet<Entry>(326888); 
	        Entry entry = new Entry();
	        entry.map = Arrays.copyOf(map, map.length);
	        entry.direction = 0;
	        entry.parent = null;
	        entry.priority = 0;
	        entry.deep = 0;
	        queue.offer(entry);
	        liveSet.add(entry);
	        List<Integer> dirList = new ArrayList<Integer>();
	        while (!queue.isEmpty()) {
	            entry = queue.poll();
	            dirList.add(entry.direction);
	            liveSet.remove(entry);
	            set.add(entry);         
	            if (this.check(entry.map)) {
	                int sum = 0;
	                Stack<Integer> tplist = new Stack<Integer>();
	                while (entry.parent != null) {
	                    //System.out.println(entry.direction);
	                    //printDirection(entry.direction);
	                    tplist.add(entry.direction);
	                    entry = entry.parent;
	                    sum++;
	                }

	                System.out.println(sum);
	                while(!tplist.empty())
	                {
	                	printDirection(tplist.pop());
	                }
	                
	                Solution.totalPath = new int[tplist.size()];
	                for (int pk = tplist.size() - 1, pkk = 0; pk >= 0; pk--) {
	                	Solution.totalPath[pkk++] = tplist.get(pk);  
	                }
	                return true;

	            } else { 
	                
	                int posx = 0, posy = 0;
	                for (int i = 0; i < n*n; i++) {
	                    if (entry.map[i] == 0) {
	                        posx = i / n;
	                        posy = i % n;
	                        break;
	                    }
	                }
	                int[] fx = {-1, 0, 1, 0};
	                int[] fy = {0, 1, 0, -1};
	                int pos = posx * n + posy;  
	               
	                for (int k = 0; k < 4; k++) {
	                    if (k == entry.redirection) {
	                        continue;
	                    }
	                    int newi = posx + fx[k];
	                    int newj = posy + fy[k];
	                    if (newi < 0 || newi >= n) {
	                        continue;
	                    }
	                    if (newj < 0 || newj >= n) {
	                        continue;
	                    }
	                    
	                    Entry nextEntry = new Entry();
	                    
	                    nextEntry.map = Arrays.copyOf(entry.map, entry.map.length);
	                    
	                    int newPos = newi * n + newj, temp;
	                    temp = nextEntry.map[newPos];
	                    nextEntry.map[newPos] = nextEntry.map[pos];
	                    nextEntry.map[pos] = temp;
	                    if (set.contains(nextEntry)) {
	                        continue;
	                    }
	                    if (liveSet.contains(nextEntry)) {
	                        continue;
	                    } else {
	                        nextEntry.parent = entry;
	                        nextEntry.direction = k;
	                        if (k + 2 < n) {
	                            nextEntry.redirection = k + 2;
	                        } else {
	                            nextEntry.redirection = k - 2;
	                        }
	                        nextEntry.deep = entry.deep + 1;
	                        nextEntry.priority = this.calPriority(nextEntry.map) + nextEntry.deep;
	                        queue.offer(nextEntry);
	                        liveSet.add(nextEntry);
	                    }
	                }
	            }
	        }
	        return false;
	    }


	    public static void main(String[] args) {
	        
	    	Scanner in = new Scanner(System.in);
	        int n = in.nextInt();
	        Entry.n = n;
	        Solution.n = n;
	        n = n * n;
	        int[] mymap = new int[n];
	        
	        for(int i = 0; i < n; i++)
	        {
	        	mymap[i] = in.nextInt();
	        }
	        
	        //int[] mymap = {0, 3, 8, 4, 1, 7, 2, 6, 5};
	        Solution napt = new Solution();

	        napt.search(mymap);
	        //System.out.println("Game Over!");
	    }
	}

