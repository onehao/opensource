package onehao.threads;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 
 * @author Michael Wan.
 *
 */
public class BlockingQueueCommunication {

	public static void main(String[] args) {
		final Business business = new Business();
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (int j = 0; j < 50; j++) {
					business.sub(j);
				}
			}
		}).start();
		for (int j = 0; j < 50; j++) {
			business.main(j);
		}

		// new Thread(new Runnable() {
		// @Override
		// public void run() {
		// for (int j = 0; j < 50; j++) {
		// synchronized (TraditionalThreadCommunication.class) {
		// for (int i = 0; i < 10; i++) {
		// System.out.println("sub thread sequence of " + i
		// + ", loop of " + j);
		// }
		// }
		// }
		// }
		// }).start();
		// for (int j = 0; j < 50; j++) {
		// synchronized (TraditionalThreadCommunication.class) {
		// for (int i = 0; i < 10; i++) {
		// System.out.println("main thread sequence of " + i
		// + ", loop of " + j);
		// }
		// }
		// }
		// new Thread().start();
	}

	static class Business {
		BlockingQueue<Integer> queue1 = new ArrayBlockingQueue<Integer>(1);
		BlockingQueue<Integer> queue2 = new ArrayBlockingQueue<Integer>(1);

		// 匿名构造方法。运行在任何构造方法之前。
		{
			try {
				queue2.put(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void sub(int j) {
			try {
				queue1.put(1);

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			for (int i = 0; i < 10; i++) {
				System.out.println("sub thread sequence of " + i + ", loop of "
						+ j);
			}
			try {
				queue2.take();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void main(int j) {
			try {
				queue2.put(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (int i = 1; i <= 100; i++) {
				System.out.println("main thread sequence of " + i
						+ ", loop of " + j);
			}
			try {
				queue1.take();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
