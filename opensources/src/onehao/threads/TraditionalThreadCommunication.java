package onehao.threads;

public class TraditionalThreadCommunication {

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

}

class Business {
	private boolean isSub = true;

	public synchronized void sub(int j) {
		while (!isSub) { // or if(!isSub)
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		for (int i = 0; i < 10; i++) {
			System.out
					.println("sub thread sequence of " + i + ", loop of " + j);
		}
		isSub = !isSub;
		this.notify();
	}

	public synchronized void main(int j) {
		while (isSub) { // or if(!isSub) 推荐使用while防止伪唤醒。
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for (int i = 1; i <= 100; i++) {
			System.out.println("main thread sequence of " + i + ", loop of "
					+ j);
		}
		isSub = !isSub;
		this.notify();
	}
}
