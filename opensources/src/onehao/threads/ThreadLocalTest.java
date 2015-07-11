package onehao.threads;

import java.util.Random;

public class ThreadLocalTest {
	// 一个threadlocal只能放一个变量
	static ThreadLocal<Integer> local = new ThreadLocal<Integer>();
	static ThreadLocal<MyThreadScopeData> myThreadScopeData = new ThreadLocal<MyThreadScopeData>();

	public static void main(String[] args) {
		for (int i = 0; i < 2; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					int dataa = new Random().nextInt();
					System.out.println(Thread.currentThread().getName()
							+ " has put data : " + dataa);
					local.set(dataa);
					// MyThreadScopeData mydata = new MyThreadScopeData();
					// mydata.setName("name" + dataa);
					// mydata.setAge(dataa);
					// myThreadScopeData.set(mydata);
					MyThreadScopeData.getThreadInstance().setName(
							"name" + dataa);
					MyThreadScopeData.getThreadInstance().setAge(dataa);
					new A().get();
					new B().get();
				}
			}).start();
		}
	}

	static class A {
		public void get() {
			int data = local.get();
			System.out.println("A from " + Thread.currentThread().getName()
					+ " has get data : " + data);
			// MyThreadScopeData mydata = myThreadScopeData.get();
			// System.out.println("A from " + mydata.getName() + "," +
			// mydata.getAge());
			// MyThreadScopeData mydata = MyThreadScopeData.getInstance();
			// System.out.println("A from " + mydata.getName() + "," +
			// mydata.getAge());
			MyThreadScopeData myData = MyThreadScopeData.getThreadInstance();
			System.out
					.println("A from " + Thread.currentThread().getName()
							+ " getMyData: " + myData.getName() + ","
							+ myData.getAge());
		}
	}

	static class B {
		public void get() {
			int data = local.get();
			System.out.println("B from " + Thread.currentThread().getName()
					+ " has get data : " + data);
			// MyThreadScopeData mydata = myThreadScopeData.get();
			// System.out.println("B from " + mydata.getName() + "," +
			// mydata.getAge());
			// MyThreadScopeData mydata = MyThreadScopeData.getInstance();
			// System.out.println("B from " + mydata.getName() + ","
			// + mydata.getAge());
			MyThreadScopeData myData = MyThreadScopeData.getThreadInstance();
			System.out
					.println("B from " + Thread.currentThread().getName()
							+ " getMyData: " + myData.getName() + ","
							+ myData.getAge());
		}
	}
}

class MyThreadScopeData {
	private MyThreadScopeData() {
	}

	public static/* synchronized */MyThreadScopeData getThreadInstance() {
		MyThreadScopeData instance = map.get();
		if (instance == null) {
			instance = new MyThreadScopeData();
			map.set(instance);
		}
		return instance;
	}

	// private static MyThreadScopeData instance = null;//new
	// MyThreadScopeData();

	private static ThreadLocal<MyThreadScopeData> map = new ThreadLocal<MyThreadScopeData>();

	private String name;
	private int age;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
}