package onehao.threads;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionCommunication {

	public static void main(String[] args) {
		final ConditionCommunication.Business business = new ConditionCommunication.Business();
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
		Lock lock = new ReentrantLock();
		Condition condition = lock.newCondition();
		private boolean isSub = true;

		public/* synchronized */void sub(int j) {
			lock.lock();
			try {
				while (!isSub) { // or if(!isSub)
					try {
						// this.wait();
						condition.await();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				for (int i = 0; i < 10; i++) {
					System.out.println("sub thread sequence of " + i
							+ ", loop of " + j);
				}
				isSub = !isSub;
				// this.notify();
				condition.signal();
			} finally {
				lock.unlock();
			}
		}

		public/* synchronized */void main(int j) {
			lock.lock();
			try {
				while (isSub) { // or if(!isSub) 推荐使用while防止伪唤醒。
					try {
						// this.wait();
						condition.await();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				for (int i = 1; i <= 100; i++) {
					System.out.println("main thread sequence of " + i
							+ ", loop of " + j);
				}
				isSub = !isSub;
				// this.notify();
				condition.signal();
			} finally {
				lock.unlock();
			}
		}
	}

}
