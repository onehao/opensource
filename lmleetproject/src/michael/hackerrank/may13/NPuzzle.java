package hackerrank.may13;
//定义一个实体
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
class Entry2 implements Comparable<Entry> {

	    public static int[] dmap = {0, 1, 2, 3, 4, 5, 6, 7, 8};  //预存入最终状态
	    public int[] map = new int[9];   //用于存放当前状态
	    public Entry parent = null;     //用来记录父状态
	    public int priority = 0;           //记录当前状态的启发值
	    public int direction = 0;        //记录当前结点是由其父结点由何种方式扩展而来
	    public int deep = 0;         //结点深度
	    public int redirection = -1; //反向
	    //用于客户端排序

	    public int compareTo(Entry o) {
	        return this.priority - o.priority;  //升序排列
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

	    public static int[] totalPath; //存放最终路径

	    //用来检测是否到达了目标状态
	    public boolean check(int[] map) {
	        for (int i = 0; i < 9; i++) {
	            if (map[i] != Entry.dmap[i]) {
	                return false;
	            }
	        }
	        return true;
	    }

	    //对启发值进行估算
	    public int calPriority(int[] map) {
	        int wrongNum = 0;  //记录错位数
	        int totalNum = 0;  //记录需要移动的总步数
	        int overNum = 0;   //记录需要颠倒的数目
	        for (int i = 0; i < 9; i++) {
	            if (map[i] != Entry.dmap[i]) {
	                wrongNum++;   //累加错位数
	                totalNum = Math.abs(map[i] - Entry.dmap[i]); //累加移动步数
	            }

	            if (i != 0) {
	                if ((map[i] == map[i - 1] + 1) || (map[i] == map[i - 1] - 1)) {
	                    overNum += 2;   //累加颠倒的步数
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

	    //对解空间行搜索
	    public boolean search(int[] map) {
	        PriorityQueue<Entry> queue = new PriorityQueue<Entry>(326888);  //用来存放活结点
	        Set<Entry> set = new HashSet<Entry>(326888);   //用来存放死结点
	        Set<Entry> liveSet = new HashSet<Entry>(326888); //配合实现双哈希作用
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
	            set.add(entry);         //标记已为死结点
	            if (this.check(entry.map)) { //已达目标
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
	                //System.out.println("移动的总步数为： " + sum + "  ");
	                System.out.println(sum);
	                while(!tplist.empty())
	                {
	                	printDirection(tplist.pop());
	                }
	                
	                NPuzzle.totalPath = new int[tplist.size()];
	                for (int pk = tplist.size() - 1, pkk = 0; pk >= 0; pk--) {
	                	NPuzzle.totalPath[pkk++] = tplist.get(pk);  //到此已然获取了移动策略
	                }
	                return true;

	            } else { //未达目标
	                //寻找空白块
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
	                int pos = posx * 3 + posy;  //记录当前位
	                //对四个方向进行搜索
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
	                    //构造新状态
	                    nextEntry.map = Arrays.copyOf(entry.map, entry.map.length);
	                    //交换位置产生移动效果
	                    int newPos = newi * 3 + newj, temp;
	                    temp = nextEntry.map[newPos];
	                    nextEntry.map[newPos] = nextEntry.map[pos];
	                    nextEntry.map[pos] = temp;
	                    if (set.contains(nextEntry)) { //新扩展的结点为死结点
	                        continue;
	                    }
	                    if (liveSet.contains(nextEntry)) {//此种情况暂不考查
	                        continue;
	                    } else {    //新增活结点
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
	    //用于方法测试

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

