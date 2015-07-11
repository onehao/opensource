package onehao.threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Executors 使用
 * 
 * @author Michael Wan.
 *
 */
public class ThreadPoolTest {
	public static void main(String[] args) {
		// ExecutorService threadPool = Executors.newFixedThreadPool(3);
		ExecutorService threadPool = Executors.newCachedThreadPool();
		for (int i = 0; i < 10; i++) {
			final int task = i;
			threadPool.execute(new Runnable() {
				@Override
				public void run() {
					for (int j = 1; j <= 10; j++) {
						try {
							Thread.sleep(20);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.out.println(Thread.currentThread().getName()
								+ " is looping of " + j + " for task of "
								+ task);
					}
				}
			});
		}
		System.out.println("all of 10 tasks have committed.");
		threadPool.shutdown();
		
		Executors.newScheduledThreadPool(3).schedule(new Runnable(){
			@Override
			public void run() {
				System.out.println("bombing!");
				
			}
		}, 10, TimeUnit.SECONDS);
		
		Executors.newScheduledThreadPool(3).scheduleAtFixedRate(new Runnable(){
			@Override
			public void run() {
				System.out.println("bombing!");
				
			}
		}, 10, 2, TimeUnit.SECONDS);
	}
}
