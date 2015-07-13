package onehao.threads;

import java.util.Random;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/***
 * 
 * @author Michael Wan. Lock & Condition 读写锁，多个读锁不互斥，读锁与写锁互斥。
 * 
 *         //不管有没有记录都会返回对象代理， User$proxy extends User { private Integer id = id;
 *         User readUser = null; getName(){ if(readUser == null){ realUser =
 *         session.get(id); if(realUser == null)throw Exception } return
 *         realUser.getName(); } } User user = session.load(id, User.class);
 * 
 *         数据库取对象，有数据返回，没有返回空 User user = session.get(id, User.class);
 * 
 * 
 *         查看ReentrantReadWriteLock缓存系统。
 *
 */
public class ReadWriteLockTest {
	public static void main(String[] args) {
		final Queue3 q3 = new Queue3();
		for (int i = 0; i < 3; i++) {
			new Thread() {
				public void run() {
					while (true) {
						q3.get();
					}
				}

			}.start();

			new Thread() {
				public void run() {
					while (true) {
						q3.put(new Random().nextInt(10000));
					}
				}

			}.start();
		}

	}
}

class Queue3 {
	private Object data = null;// 共享数据，只能有一个线程能写该数据，但可以有多个线程同时读该数据。
	ReadWriteLock rwl = new ReentrantReadWriteLock();

	public void get() {
		rwl.readLock().lock();
		try {
			System.out.println(Thread.currentThread().getName()
					+ " be ready to read data!");
			Thread.sleep((long) (Math.random() * 1000));
			System.out.println(Thread.currentThread().getName()
					+ "have read data :" + data);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			rwl.readLock().unlock();
		}
	}

	public void put(Object data) {

		rwl.writeLock().lock();
		try {
			System.out.println(Thread.currentThread().getName()
					+ " be ready to write data!");
			Thread.sleep((long) (Math.random() * 1000));
			this.data = data;
			System.out.println(Thread.currentThread().getName()
					+ " have write data: " + data);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			rwl.writeLock().unlock();
		}

	}
}
