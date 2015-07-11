package onehao.threads;

import java.util.concurrent.atomic.AtomicInteger;

/***
 * investigate the concurrent.atomic package.
 * 
 * @author Michael Wan
 *
 */
public class ConcurrentAtomicTest {
	public static void main(String[] args) {
		AtomicInteger i = new AtomicInteger(0);
		for (; i.get() < 50; i.incrementAndGet()) {
			System.out.println(i);
		}

	}
}
