package onehao.threads;

/***
 * 设计4个线程，其中两个线程每次对j 增加1，另外两 个线程对j 每次减少1。写出程序。
 * 
 * 如果每个线程执行的代码相同，可以使用同一个Runnable对象，这个Runnable对象中那个共享数据。例如， 买票系统可以这么做。
 * 
 * 如果执行代码不同，如本例，需要两个run方法。 1.将共享数据封装在另外一个对象中，然后将这个对象逐一传递给各个Runnable对象。
 * 2.将Runnable对象作为某一个类中的内部类，共享数据作为这个外部类中的成员变量。
 * 
 * @author Michael Wan
 *
 */
public class MultiThreadShareData {
	private static ShareData1 data3 = new ShareData1();

	public static void main(String[] args) {
		final ShareData1 data2 = new ShareData1();
		final ShareData1 data1 = new ShareData1();

		new Thread(new MyRunnable1(data2)).start();
		new Thread(new MyRunnable2(data2)).start();

		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				data1.decrement();
				;
			}
		}).start();
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				data1.increment();
			}
		}).start();
	}
}

class MyRunnable1 implements Runnable {
	private ShareData1 data;

	public MyRunnable1(ShareData1 data) {
		this.data = data;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		data.decrement();
	}
}

class MyRunnable2 implements Runnable {
	private ShareData1 data;

	public MyRunnable2(ShareData1 data) {
		this.data = data;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		data.decrement();
	}
}

class ShareData1 implements Runnable {

	private int j = 0;

	public synchronized void increment() {
		j++;
	}

	public synchronized void decrement() {
		j--;
	}

	private int count = 100;

	@Override
	public void run() {
		while (true) {
			count--;
		}
	}
}
