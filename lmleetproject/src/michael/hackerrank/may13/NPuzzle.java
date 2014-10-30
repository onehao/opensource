package michael.hackerrank.may13;
//锟斤拷锟斤拷一锟斤拷实锟斤拷
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
class Entry2 implements Comparable<Entry> {

	    public static int[] dmap = {0, 1, 2, 3, 4, 5, 6, 7, 8};  //预锟斤拷锟斤拷锟斤拷锟斤拷状态
	    public int[] map = new int[9];   //锟斤拷锟节达拷诺锟角白刺�
	    public Entry parent = null;     //锟斤拷锟斤拷锟斤拷录锟斤拷状态
	    public int priority = 0;           //锟斤拷录锟斤拷前状态锟斤拷锟斤拷锟斤拷值
	    public int direction = 0;        //锟斤拷录锟斤拷前锟斤拷锟斤拷锟斤拷锟斤拷涓革拷锟斤拷锟缴猴拷锟街凤拷式锟斤拷展锟斤拷锟斤拷
	    public int deep = 0;         //锟斤拷锟斤拷锟斤拷
	    public int redirection = -1; //锟斤拷锟斤拷
	    //锟斤拷锟节客伙拷锟斤拷锟斤拷锟斤拷

	    public int compareTo(Entry o) {
	        return this.priority - o.priority;  //锟斤拷锟斤拷锟斤拷锟斤拷
	    }

	    @Override
	    public boolean equals(Object obj) {
	        Entry entry = (Entry) obj;
	        for (int i = 0; i < 9; i++) {
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

	public class NPuzzle { 

	    public static int[] totalPath; //锟斤拷锟斤拷锟斤拷锟铰凤拷锟�

	    //锟斤拷锟斤拷锟斤拷锟斤拷欠竦酱锟斤拷锟侥匡拷锟阶刺�
	    public boolean check(int[] map) {
	        for (int i = 0; i < 9; i++) {
	            if (map[i] != Entry2.dmap[i]) {
	                return false;
	            }
	        }
	        return true;
	    }

	    //锟斤拷锟斤拷锟斤拷值锟斤拷锟叫癸拷锟斤拷
	    public int calPriority(int[] map) {
	        int wrongNum = 0;  //锟斤拷录锟斤拷位锟斤拷
	        int totalNum = 0;  //锟斤拷录锟斤拷要锟狡讹拷锟斤拷锟杰诧拷锟斤拷
	        int overNum = 0;   //锟斤拷录锟斤拷要锟竭碉拷锟斤拷锟斤拷目
	        for (int i = 0; i < 9; i++) {
	            if (map[i] != Entry2.dmap[i]) {
	                wrongNum++;   //锟桔加达拷位锟斤拷
	                totalNum = Math.abs(map[i] - Entry2.dmap[i]); //锟桔硷拷锟狡讹拷锟斤拷锟斤拷
	            }

	            if (i != 0) {
	                if ((map[i] == map[i - 1] + 1) || (map[i] == map[i - 1] - 1)) {
	                    overNum += 2;   //锟桔加颠碉拷锟侥诧拷锟斤拷
	                }
	            }


	        }
	        return wrongNum + totalNum + overNum;
	    }
	    
	    private void printDirection(int x)
	    {
	    	if(x == 2)
	    	{
	    		System.out.println("Down");
	    	}
	    	else if( x == 0)
	    	{
	    		System.out.println("UP");
	    	}
	    	
	    	else if(x == 1)
	    	{
	    		System.out.println("Right");
	    	}
	    	else // x == 3
	    	{
	    		System.out.println("Left");
	    	}
	    	
	    }

	    //锟皆斤拷占锟斤拷锟斤拷锟斤拷锟�
	    public boolean search(int[] map) {
	        PriorityQueue<Entry> queue = new PriorityQueue<Entry>(326888);  //锟斤拷锟斤拷锟斤拷呕锟斤拷锟�
	        Set<Entry> set = new HashSet<Entry>(326888);   //锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
	        Set<Entry> liveSet = new HashSet<Entry>(326888); //锟斤拷锟绞碉拷锟剿�拷锟较ｏ拷锟斤拷锟�
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
	            set.add(entry);         //锟斤拷锟斤拷锟轿�拷锟斤拷锟斤拷
	            if (this.check(entry.map)) { //锟窖达拷目锟斤拷
	                int sum = 0;
	                Stack<Integer> tplist = new Stack<Integer>();
	                while (entry.parent != null) {
	                    //System.out.println(entry.direction);
	                    //printDirection(entry.direction);
	                    tplist.add(entry.direction);
	                    entry = entry.parent;
	                    sum++;
	                }
	                //System.out.println(entry.direction);
	                //printDirection(entry.direction);
	                //System.out.println("---------------------");
	                //System.out.println("锟狡讹拷锟斤拷锟杰诧拷锟斤拷为锟斤拷 " + sum + "  ");
	                System.out.println(sum);
	                while(!tplist.empty())
	                {
	                	printDirection(tplist.pop());
	                }
	                
	                NPuzzle.totalPath = new int[tplist.size()];
	                for (int pk = tplist.size() - 1, pkk = 0; pk >= 0; pk--) {
	                	NPuzzle.totalPath[pkk++] = tplist.get(pk);  //锟斤拷锟斤拷锟斤拷然锟斤拷取锟斤拷锟狡讹拷锟斤拷锟斤拷
	                }
	                return true;

	            } else { //未锟斤拷目锟斤拷
	                //寻锟揭空白匡拷
	                int posx = 0, posy = 0;
	                for (int i = 0; i < 9; i++) {
	                    if (entry.map[i] == 0) {
	                        posx = i / 3;
	                        posy = i % 3;
	                        break;
	                    }
	                }
	                int[] fx = {-1, 0, 1, 0};
	                int[] fy = {0, 1, 0, -1};
	                int pos = posx * 3 + posy;  //锟斤拷录锟斤拷前位
	                //锟斤拷锟侥革拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�
	                for (int k = 0; k < 4; k++) {
	                    if (k == entry.redirection) {
	                        continue;
	                    }
	                    int newi = posx + fx[k];
	                    int newj = posy + fy[k];
	                    if (newi < 0 || newi >= 3) {
	                        continue;
	                    }
	                    if (newj < 0 || newj >= 3) {
	                        continue;
	                    }
	                    
	                    Entry nextEntry = new Entry();
	                    //锟斤拷锟斤拷锟斤拷状态
	                    nextEntry.map = Arrays.copyOf(entry.map, entry.map.length);
	                    //锟斤拷锟斤拷位锟矫诧拷锟斤拷锟狡讹拷效锟斤拷
	                    int newPos = newi * 3 + newj, temp;
	                    temp = nextEntry.map[newPos];
	                    nextEntry.map[newPos] = nextEntry.map[pos];
	                    nextEntry.map[pos] = temp;
	                    if (set.contains(nextEntry)) { //锟斤拷锟斤拷展锟侥斤拷锟轿�拷锟斤拷锟斤拷
	                        continue;
	                    }
	                    if (liveSet.contains(nextEntry)) {//锟斤拷锟斤拷锟斤拷锟斤拷莶锟斤拷锟斤拷锟�
	                        continue;
	                    } else {    //锟斤拷锟斤拷锟斤拷锟斤拷
	                        nextEntry.parent = entry;
	                        nextEntry.direction = k;
	                        if (k + 2 < 3) {
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
	    //锟斤拷锟节凤拷锟斤拷锟斤拷锟斤拷

	    public static void main(String[] args) {
	        
	    	Scanner in = new Scanner(System.in);
	        int n = in.nextInt();
	        n = n * n;
	        int[] mymap = new int[n];
	        
	        for(int i = 0; i < n; i++)
	        {
	        	mymap[i] = in.nextInt();
	        }
	        
	        //int[] mymap = {0, 3, 8, 4, 1, 7, 2, 6, 5};
	        NPuzzle napt = new NPuzzle();
	        napt.search(mymap);
	        //System.out.println("Game Over!");
	    }
	}

