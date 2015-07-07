package onehao.threads;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ThreadScopeShareData {

	// private static int data = 0;
	private static Map<Thread, Integer> threadData = new HashMap<Thread, Integer>();

	public static void main(String[] args) {
		for (int i = 0; i < 2; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					int dataa = new Random().nextInt();
					System.out.println(Thread.currentThread().getName()
							+ " has put data : " + dataa);
					threadData.put(Thread.currentThread(), dataa);
					new A().get();
					new B().get();
				}
			}).start();
		}
	}

	static class A {
		public void get() {
			if (threadData.containsKey(Thread.currentThread())) {
				int data = threadData.get(Thread.currentThread());
				System.out.println("A from " + Thread.currentThread().getName()
						+ " has get data : " + data);
			} else {
				System.out.println("A: the key hasn't been added yet.");
			}
		}
	}

	static class B {
		public void get() {
			if (threadData.containsKey(Thread.currentThread())) {
				int data = threadData.get(Thread.currentThread());
				System.out.println("B from " + Thread.currentThread().getName()
						+ " has get data : " + data);
			}else {
				System.out.println("B: the key hasn't been added yet.");
			}
		}
	}
}
