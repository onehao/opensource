package onehao.threads;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * design a cache system.
 * 
 * @author Michael Wan.
 *
 */

public class CacheDemo {

	private Map<String, Object> cache = new HashMap<String, Object>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public synchronized Object getData2(String key) {
		Object value = cache.get(key);
		if (value == null) {
			value = "vFromDB";// queryDB.
		}
		return value;
	}

	private ReadWriteLock rwl = new ReentrantReadWriteLock();

	public synchronized Object getData(String key) {
		rwl.readLock().lock();
		Object value = null;
		try {
			value = cache.get(key);
			if (value == null) {
				rwl.readLock().unlock();
				rwl.writeLock().lock();
				try {
					if (value == null) {
						value = "vFromDB";// queryDB.
					}
				} finally {
					rwl.writeLock().unlock();
				}
				rwl.readLock().lock();
			}
		} finally {
			rwl.readLock().unlock();
		}
		return value;
	}

}
