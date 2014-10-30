package hackerrank.may13;
//����һ��ʵ��
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
class Entry2 implements Comparable<Entry> {

	    public static int[] dmap = {0, 1, 2, 3, 4, 5, 6, 7, 8};  //Ԥ��������״̬
	    public int[] map = new int[9];   //���ڴ�ŵ�ǰ״̬
	    public Entry parent = null;     //������¼��״̬
	    public int priority = 0;           //��¼��ǰ״̬������ֵ
	    public int direction = 0;        //��¼��ǰ��������丸����ɺ��ַ�ʽ��չ����
	    public int deep = 0;         //������
	    public int redirection = -1; //����
	    //���ڿͻ�������

	    public int compareTo(Entry o) {
	        return this.priority - o.priority;  //��������
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

	    public static int[] totalPath; //�������·��

	    //��������Ƿ񵽴���Ŀ��״̬
	    public boolean check(int[] map) {
	        for (int i = 0; i < 9; i++) {
	            if (map[i] != Entry.dmap[i]) {
	                return false;
	            }
	        }
	        return true;
	    }

	    //������ֵ���й���
	    public int calPriority(int[] map) {
	        int wrongNum = 0;  //��¼��λ��
	        int totalNum = 0;  //��¼��Ҫ�ƶ����ܲ���
	        int overNum = 0;   //��¼��Ҫ�ߵ�����Ŀ
	        for (int i = 0; i < 9; i++) {
	            if (map[i] != Entry.dmap[i]) {
	                wrongNum++;   //�ۼӴ�λ��
	                totalNum = Math.abs(map[i] - Entry.dmap[i]); //�ۼ��ƶ�����
	            }

	            if (i != 0) {
	                if ((map[i] == map[i - 1] + 1) || (map[i] == map[i - 1] - 1)) {
	                    overNum += 2;   //�ۼӵߵ��Ĳ���
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

	    //�Խ�ռ�������
	    public boolean search(int[] map) {
	        PriorityQueue<Entry> queue = new PriorityQueue<Entry>(326888);  //������Ż���
	        Set<Entry> set = new HashSet<Entry>(326888);   //������������
	        Set<Entry> liveSet = new HashSet<Entry>(326888); //���ʵ��˫��ϣ����
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
	            set.add(entry);         //�����Ϊ�����
	            if (this.check(entry.map)) { //�Ѵ�Ŀ��
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
	                //System.out.println("�ƶ����ܲ���Ϊ�� " + sum + "  ");
	                System.out.println(sum);
	                while(!tplist.empty())
	                {
	                	printDirection(tplist.pop());
	                }
	                
	                NPuzzle.totalPath = new int[tplist.size()];
	                for (int pk = tplist.size() - 1, pkk = 0; pk >= 0; pk--) {
	                	NPuzzle.totalPath[pkk++] = tplist.get(pk);  //������Ȼ��ȡ���ƶ�����
	                }
	                return true;

	            } else { //δ��Ŀ��
	                //Ѱ�ҿհ׿�
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
	                int pos = posx * 3 + posy;  //��¼��ǰλ
	                //���ĸ������������
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
	                    //������״̬
	                    nextEntry.map = Arrays.copyOf(entry.map, entry.map.length);
	                    //����λ�ò����ƶ�Ч��
	                    int newPos = newi * 3 + newj, temp;
	                    temp = nextEntry.map[newPos];
	                    nextEntry.map[newPos] = nextEntry.map[pos];
	                    nextEntry.map[pos] = temp;
	                    if (set.contains(nextEntry)) { //����չ�Ľ��Ϊ�����
	                        continue;
	                    }
	                    if (liveSet.contains(nextEntry)) {//��������ݲ�����
	                        continue;
	                    } else {    //��������
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
	    //���ڷ�������

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

