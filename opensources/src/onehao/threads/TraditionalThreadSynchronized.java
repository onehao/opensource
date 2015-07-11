package onehao.threads;

public class TraditionalThreadSynchronized {

	public static void main(String[] args) {

		new TraditionalThreadSynchronized().init();

	}

	public void init() {
		final Outputer out = new Outputer();
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					out.output("hello----------------");
				}

			}
		}).start();

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					out.output("world");
				}

			}
		}).start();
	}

	static class Outputer {
		public synchronized void output(String name) { // lock this object, so
														// sync with output3
			int len = name.length();
			for (int i = 0; i < len; i++) {
				System.out.print(name.charAt(i));
			}
			System.out.println();

		}

		public static synchronized void output4(String name) { // lock this
																// object
			int len = name.length();
			for (int i = 0; i < len; i++) {
				System.out.print(name.charAt(i));
			}
			System.out.println();

		}

		public void output5(String name) { // sync with output4,字节码对象。
			int len = name.length();
			synchronized (Outputer.class) {
				for (int i = 0; i < len; i++) {
					System.out.print(name.charAt(i));
				}
				System.out.println();
			}

		}

		public void output2(String name) {
			String sync = "";
			int len = name.length();
			synchronized (sync) {
				for (int i = 0; i < len; i++) {
					System.out.print(name.charAt(i));
				}
				System.out.println();
			}

		}

		public void output3(String name) {
			int len = name.length();
			synchronized (this) {
				for (int i = 0; i < len; i++) {
					System.out.print(name.charAt(i));
				}
				System.out.println();
			}

		}
	}
}
