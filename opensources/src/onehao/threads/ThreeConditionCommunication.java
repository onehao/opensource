package onehao.threads;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreeConditionCommunication {

	public static void main(String[] args) {
		final ThreeConditionCommunication.Business business = new ThreeConditionCommunication.Business();
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (int j = 0; j < 50; j++) {
					business.sub2(j);
				}
			}
		}).start();
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (int j = 0; j < 50; j++) {
					business.sub3(j);
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
		Condition condition1 = lock.newCondition();
		Condition condition2 = lock.newCondition();
		Condition condition3 = lock.newCondition();
		private int isSub = 1;

		
		public/* synchronized */void sub2(int j) {
			lock.lock();
			try {
				while (isSub != 2) { // or if(!isSub)
					try {
						// this.wait();
						condition2.await();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				for (int i = 0; i < 10; i++) {
					System.out.println("sub2 thread sequence of " + i
							+ ", loop of " + j);
				}
				isSub = 3;
				// this.notify();
				condition3.signal();
			} finally {
				lock.unlock();
			}
		}
		
		public/* synchronized */void sub3(int j) {
			lock.lock();
			try {
				while (isSub != 3) { // or if(!isSub)
					try {
						// this.wait();
						condition3.await();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				for (int i = 0; i < 20; i++) {
					System.out.println("sub3 thread sequence of " + i
							+ ", loop of " + j);
				}
				isSub = 1;
				// this.notify();
				condition1.signal();
			} finally {
				lock.unlock();
			}
		}

		public/* synchronized */void main(int j) {
			lock.lock();
			try {
				while (isSub != 1) { // or if(!isSub) 推荐使用while防止伪唤醒。
					try {
						// this.wait();
						condition1.await();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				for (int i = 1; i <= 100; i++) {
					System.out.println("main thread sequence of " + i
							+ ", loop of " + j);
				}
				isSub = 2;
				// this.notify();
				condition2.signal();
			} finally {
				lock.unlock();
			}
		}
	}

}
